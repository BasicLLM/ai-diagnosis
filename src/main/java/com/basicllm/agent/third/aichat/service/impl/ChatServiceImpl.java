package com.basicllm.agent.third.aichat.service.impl;

import com.basicllm.agent.third.aichat.model.config.AiProxyServiceConfig;
import com.basicllm.agent.third.aichat.model.request.ChatCompletionRequest;
import com.basicllm.agent.third.aichat.model.response.ChatCompletionResponse;
import com.basicllm.agent.third.aichat.proxy.AiChatProxyService;
import com.basicllm.agent.third.aichat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;

@Service
public class ChatServiceImpl implements ChatService {

    public static final ParameterizedTypeReference<String> TYPE = new ParameterizedTypeReference<String>() {};

    @Autowired
    private AiChatProxyService aiChatProxyService;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public ChatCompletionResponse chatCompletion(String engine, ChatCompletionRequest request) {

        // (1) 获取 AI 代理服务配置
        AiProxyServiceConfig aiProxyServiceConfig = aiChatProxyService.getAiProxyServiceConfigById(engine);

        // (2) 获取一个随机的密钥
        String key = aiChatProxyService.getRandomKeyById(engine);

        // (3) 设置密钥
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s",key));

        // (4) 创建HttpEntity对象，包含headers和body（如果有）
        HttpEntity<ChatCompletionRequest> entity = new HttpEntity<>(request, headers);

        // (5) 请求服务
        return restTemplate.exchange(
                aiProxyServiceConfig.getChatService().getChatCompletionsUrl(),
                HttpMethod.POST,
                entity,
                ChatCompletionResponse.class
        ).getBody();

    }

    /**
     * 聊天请求接口
     * @param request 请求参数
     * @return 请求的数据
     */
    @Override
    public SseEmitter streamChatCompletion(String engine, ChatCompletionRequest request) {

        // (1) 创建 SseEmitter
        SseEmitter sseEmitter = new SseEmitter();

        // (2) 构建流推送端
        AiProxyServiceConfig aiProxyServiceConfig = aiChatProxyService.getAiProxyServiceConfigById(engine);
        WebClient webClient = WebClient.builder()
                .baseUrl(aiProxyServiceConfig.getChatService().getChatCompletionsUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.TEXT_EVENT_STREAM_VALUE)
                .build();

        // (3) 获取一个随机的密钥
        String key = aiChatProxyService.getRandomKeyById(engine);

        // (4) 发送请求
        Flux<String> eventStream = webClient
                .post()
                .header("Authorization", String.format("Bearer %s",key))
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(TYPE);

        // (5) 将数据发送给前端
        eventStream.subscribe(data->{
                    try {
                        sseEmitter.send(data);
                    } catch (IOException e) {
                        sseEmitter.completeWithError(e);
                    }
                });

        // (6) 完成请求
        eventStream.doOnComplete(sseEmitter::complete);

        // (7) 返回 SseEmitter
        return sseEmitter;

    }

}
