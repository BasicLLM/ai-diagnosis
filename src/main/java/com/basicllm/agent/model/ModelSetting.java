package com.basicllm.agent.model;

import lombok.Data;

@Data
public class ModelSetting {

    /**
     * 提供商
     */
    private String provider;

    /**
     * 模型
     */
    private String model;

    /**
     * 是否启用 RAG
     */
    private Boolean useRag;

}
