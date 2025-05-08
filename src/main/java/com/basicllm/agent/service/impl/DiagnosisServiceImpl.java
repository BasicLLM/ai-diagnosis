package com.basicllm.agent.service.impl;

import com.alibaba.dashscope.aigc.generation.*;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.basicllm.agent.consumer.DiagnosticConsumer;
import com.basicllm.agent.model.ModelSetting;
import com.basicllm.agent.model.PatientCondition;
import com.basicllm.agent.rag.KnowledgeBaseService;
import com.basicllm.agent.service.DiagnosisService;
import com.basicllm.agent.third.aichat.model.component.ChatMessage;
import com.basicllm.agent.third.aichat.model.config.AiProxyServiceConfig;
import com.basicllm.agent.third.aichat.model.constant.ChatMessageFinishReasonEnum;
import com.basicllm.agent.third.aichat.model.constant.ChatRoleEnum;
import com.basicllm.agent.third.aichat.model.request.ChatCompletionRequest;
import com.basicllm.agent.third.aichat.model.response.ChatCompletionResponse;
import com.basicllm.agent.third.aichat.proxy.AiChatProxyService;
import com.basicllm.agent.util.PromptReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Flowable;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DiagnosisServiceImpl implements DiagnosisService {

    public static final ParameterizedTypeReference<String> TYPE =
            new ParameterizedTypeReference<String>() {};

    @Resource
    private AiChatProxyService aiChatProxyService;

    @Resource
    private KnowledgeBaseService knowledgeBaseService;

    private ObjectMapper objectMapper;

    @PostConstruct
    private void init() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * AI 诊断
     *
     * @param setting   模型设置
     * @param condition 病人病历
     * @return 诊断结果
     */
    public SseEmitter diagnose(ModelSetting setting, PatientCondition condition){

        // (1) 创建 SseEmitter
        SseEmitter sseEmitter = new SseEmitter();

        // (2) 获取代理信息
        AiProxyServiceConfig aiProxyServiceConfig = aiChatProxyService.getAiProxyServiceConfigById(setting.getProvider());

        // (3) 获取一个随机的密钥
        String key = aiChatProxyService.getRandomKeyById(setting.getProvider());

        // (4) 构建请求
        ChatCompletionRequest request = buildRequest(setting.getModel(),setting.getUseRag(),condition);

        // (5) 构建消费者
        DiagnosticConsumer consumer = new DiagnosticConsumer(sseEmitter);

        if (aiProxyServiceConfig.isDashboardApi()) {

            // 构建推送端
            Generation gen = new Generation();

            // 转换请求
            GenerationParam param = toGenerationParam(request, key);

            // 发送请求
            Flowable<GenerationResult> flowableResult = null;
            try {
                flowableResult = gen.streamCall(param);
            } catch (NoApiKeyException | InputRequiredException e) {
                throw new RuntimeException(e);
            }

            // 推流
            flowableResult.subscribe(data -> {
                consumer.accept(toChatCompletionResponse(data,true));
            });

            // 完成请求
            flowableResult.doOnComplete(sseEmitter::complete);

        } else {

            // 构建推送端
            WebClient webClient = WebClient.builder()
                    .baseUrl(aiProxyServiceConfig.getChatService().getChatCompletionsUrl())
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultHeader(HttpHeaders.ACCEPT, MediaType.TEXT_EVENT_STREAM_VALUE)
                    .build();

            // 发送请求
            Flux<String> eventStream = webClient
                    .post()
                    .header("Authorization", String.format("Bearer %s",key))
                    .bodyValue(request)
                    .retrieve()
                    .bodyToFlux(TYPE);

            // 将数据发送给前端
            eventStream.subscribe(data -> {

                if (data.equals("[DONE]")) {
                    sseEmitter.complete();
                    return;
                }

                ChatCompletionResponse response;
                try {
                    response = objectMapper.readValue(data,ChatCompletionResponse.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                consumer.accept(response);

            });

            // 完成请求
            eventStream.doOnComplete(sseEmitter::complete);
        }

        // (8) 返回 SseEmitter
        return sseEmitter;

    }

    /**
     * 构建请求
     *
     * @param model     AI 模型
     * @param condition 病历
     * @return 构建的请求
     */
    private ChatCompletionRequest buildRequest(String model,boolean useRag,PatientCondition condition) {

        ChatCompletionRequest request = new ChatCompletionRequest();

        // 设置模型
        request.setModel(model);

        List<ChatMessage> chatMessages = new ArrayList<>();

        // 构建系统提示词
        String systemPrompt;
        if (useRag) {
            systemPrompt = PromptReader.readPrompt(model,"diagnose-rag.prompt");
        } else {
            systemPrompt = PromptReader.readPrompt(model,"diagnose.prompt");
        }
        chatMessages.add(ChatMessage.create(
                ChatRoleEnum.SYSTEM,
                systemPrompt
        ));

        // 构建用户报告提示词
        String userPrompt;
        if (useRag) {

            userPrompt = PromptReader.readPrompt(model,"user-rag.prompt");

            // 获取知识库内容，并填充知识库
            String knowledge = getKnowledgeFromRAG(condition);
            userPrompt = userPrompt.replaceAll("\\{knowledge}",knowledge);

        } else {
            userPrompt = PromptReader.readPrompt(model,"user.prompt");
        }

        userPrompt = userPrompt.replaceAll("\\{patient-condition}",condition.report());

        chatMessages.add(ChatMessage.create(
                ChatRoleEnum.USER,
                userPrompt
        ));
        request.setMessages(chatMessages);
        request.setStream(true);

        return request;

    }

    /**
     * 从 RAG 中获取知识
     *
     * @param condition 病历
     * @return 组装后的知识
     */
    private String getKnowledgeFromRAG(PatientCondition condition) {

        String queryPrompt = PromptReader.readPrompt("knowledge-base-query.prompt");
        queryPrompt = queryPrompt.replaceAll("\\{concise-patient-condition}",condition.conciseReport());
        List<String> searchResultList =  knowledgeBaseService.simpleSearch(queryPrompt);

        if (searchResultList != null) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < searchResultList.size(); i ++) {
                builder.append("【检索到的条目").append(i + 1).append("】\n");
                builder.append(searchResultList.get(i)).append("\n");
            }
            return builder.toString();
        } else {
            return "未检索到相关内容";
        }

    }

    /**
     * DashScope 平台请求参数
     *
     * @param request 兼容请求
     * @param key     密钥
     * @return DashScope 平台请求参数
     */
    private GenerationParam toGenerationParam(ChatCompletionRequest request, String key) {

        // 构建消息列表
        List<Message> messages = new ArrayList<>();
        for (ChatMessage chatMessage : request.getMessages()) {
            Message message = Message.builder()
                    .role(chatMessage.getRole().value())
                    .content(chatMessage.getContent())
                    .build();
            messages.add(message);
        }

        // 构建请求参数
        GenerationParam param = GenerationParam.builder()
                .apiKey(key)
                .model(request.getModel())
                .messages(messages)
                .incrementalOutput(true)
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();

        return param;

    }

    /**
     * 将 DashScope 平台请求响应转换为兼容请求响应
     *
     * @param result DashScope 平台请求响应
     * @param stream 是否为流式输出
     * @return 兼容响应
     */
    private ChatCompletionResponse toChatCompletionResponse(GenerationResult result, boolean stream) {
        GenerationOutput generationOutput = result.getOutput();

        // 转换 Choice
        List<ChatCompletionResponse.Choice> choices = new ArrayList<>();
        for (GenerationOutput.Choice choice : generationOutput.getChoices()) {

            ChatCompletionResponse.Choice newChoice = new ChatCompletionResponse.Choice();

            // 设置顺序
            newChoice.setIndex(choice.getIndex());

            // 设置消息
            ChatMessage chatMessage = toChatMessage(choice.getMessage());
            if (stream) {
                newChoice.setDelta(chatMessage);
            } else {
                newChoice.setMessage(chatMessage);
            }

            // 设置结束原因
            String finishReason = choice.getFinishReason();
            if (finishReason != null) {
                ChatMessageFinishReasonEnum finishReasonEnum = null;
                for (ChatMessageFinishReasonEnum reasonEnum : ChatMessageFinishReasonEnum.values()) {
                    if (reasonEnum.value().equals(finishReason)) {
                        finishReasonEnum = reasonEnum;
                    }
                }
                if (finishReasonEnum != null) {
                    newChoice.setFinishReason(finishReasonEnum);
                }
            }

            choices.add(newChoice);
        }

        // 转换 usage
        GenerationUsage generationUsage = result.getUsage();
        ChatCompletionResponse.Usage usage = new ChatCompletionResponse.Usage();
        usage.setPromptTokens(generationUsage.getInputTokens());
        usage.setCompletionTokens(generationUsage.getOutputTokens());
        usage.setTotalTokens(generationUsage.getTotalTokens());

        // 设置返回
        ChatCompletionResponse response = new ChatCompletionResponse();
        response.setId(result.getRequestId());
        response.setChoices(choices);
        response.setUsage(usage);

        return response;
    }

    /**
     * 将 DashScope 平台消息转换为兼容消息
     *
     * @param message DashScope 平台消息
     * @return 兼容消息
     */
    private ChatMessage toChatMessage(Message message) {

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRole(
                ChatRoleEnum.get(message.getRole())
        );
        chatMessage.setContent(message.getContent());

        return chatMessage;
    }

}
