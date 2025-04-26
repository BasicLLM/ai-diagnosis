package com.basicllm.agent.third.aichat.model.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * AI 请求组织及密钥的配置文件
 */
@Data
@Component
@ConfigurationProperties(prefix = "aichat")
public class AiChatConfig {

    /**
     * 认证系统
     */
    private List<AiProxyServiceConfig> proxies;

}
