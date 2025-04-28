package com.basicllm.agent.third.bailian.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "bailian-knowledge-base")
public class BailianKnowledgeBaseConfig {

    /**
     * 业务空间ID
     */
    private String workspaceId;

    /**
     * 知识库 ID
     */
    private String indexId;

}
