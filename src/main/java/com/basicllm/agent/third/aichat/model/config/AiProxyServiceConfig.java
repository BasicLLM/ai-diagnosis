package com.basicllm.agent.third.aichat.model.config;

import lombok.Data;

import java.util.List;

@Data
public class AiProxyServiceConfig {

    private String id;

    private String name;

    private ChatServiceConfig chatService;

    @Data
    public static class ChatServiceConfig {

        private String chatCompletionsUrl;

    }

    private FileServiceConfig fileService;

    @Data
    public static class FileServiceConfig {

        private String fileUploadUrl;

        private String fileQueryUrl;

        private String fileContentUrl;

    }

    private List<AuthenticationConfig> authentications;

}
