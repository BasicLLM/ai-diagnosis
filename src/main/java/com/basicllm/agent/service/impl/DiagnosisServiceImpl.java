package com.basicllm.agent.service.impl;

import com.basicllm.agent.consumer.DiagnosticConsumer;
import com.basicllm.agent.model.PatientCondition;
import com.basicllm.agent.service.DiagnosisService;
import com.basicllm.agent.third.aichat.model.component.ChatMessage;
import com.basicllm.agent.third.aichat.model.config.AiProxyServiceConfig;
import com.basicllm.agent.third.aichat.model.constant.ChatRoleEnum;
import com.basicllm.agent.third.aichat.model.request.ChatCompletionRequest;
import com.basicllm.agent.third.aichat.model.response.ChatCompletionResponse;
import com.basicllm.agent.third.aichat.proxy.AiChatProxyService;
import com.basicllm.agent.util.PromptReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private AiChatProxyService aiChatProxyService;

    private ObjectMapper objectMapper;

    @PostConstruct
    private void init() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * AI 诊断
     *
     * @param provider  AI 提供商
     * @param model     AI 模型
     * @param condition 病人病历
     * @return 诊断结果
     */
    public SseEmitter diagnose(String provider,String model,PatientCondition condition){

        // (1) 创建 SseEmitter
        SseEmitter sseEmitter = new SseEmitter();

        // (2) 构建流推送端
        AiProxyServiceConfig aiProxyServiceConfig = aiChatProxyService.getAiProxyServiceConfigById(provider);
        WebClient webClient = WebClient.builder()
                .baseUrl(aiProxyServiceConfig.getChatService().getChatCompletionsUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.TEXT_EVENT_STREAM_VALUE)
                .build();

        // (3) 获取一个随机的密钥
        String key = aiChatProxyService.getRandomKeyById(provider);

        // (4) 构建请求
        ChatCompletionRequest request = buildRequest(model,condition);

        // (5) 发送请求
        Flux<String> eventStream = webClient
                .post()
                .header("Authorization", String.format("Bearer %s",key))
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(TYPE);

        // (6) 将数据发送给前端
        DiagnosticConsumer consumer = new DiagnosticConsumer(sseEmitter);
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

        // (7) 完成请求
        eventStream.doOnComplete(sseEmitter::complete);

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
    private ChatCompletionRequest buildRequest(String model,PatientCondition condition) {

        ChatCompletionRequest request = new ChatCompletionRequest();

        // 设置模型
        request.setModel(model);

        // 构建访问 Prompt
        List<ChatMessage> chatMessages = new ArrayList<>();
        String systemPrompt = PromptReader.readPrompt("diagnose.prompt");
        chatMessages.add(ChatMessage.create(
                ChatRoleEnum.SYSTEM,
                systemPrompt
        ));
        chatMessages.add(ChatMessage.create(
                ChatRoleEnum.USER,
                condition.report()
        ));
        request.setMessages(chatMessages);
        request.setStream(true);

        return request;

    }

}
