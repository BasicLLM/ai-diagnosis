package com.basicllm.agent.model;

import lombok.Data;

@Data
public class DiagnosisRequest {

    /**
     * 模型设置
     */
    private ModelSetting setting;

    /**
     * 病人情况
     */
    private PatientCondition condition;

}
