package com.basicllm.agent.third.aichat.service;

import com.basicllm.agent.third.aichat.model.request.ChatCompletionRequest;
import com.basicllm.agent.third.aichat.model.response.ChatCompletionResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface ChatService {

    /**
     * 聊天请求接口
     * @param request 请求参数
     * @return 请求的数据
     */
    ChatCompletionResponse chatCompletion(String engine, ChatCompletionRequest request);

    /**
     * 流推送
     * @param request 请求参数
     * @return 流推送
     */
    SseEmitter streamChatCompletion(String engine,ChatCompletionRequest request);

}
