package com.basicllm.agent.third.aichat.model.config;

import lombok.Data;

@Data
public class AuthenticationConfig {

    /**
     * 组织ID
     */
    private String organizationId;

    /**
     * 密钥
     */
    private String key;

}
