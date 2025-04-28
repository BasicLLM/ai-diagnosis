package com.basicllm.agent.service;

import com.basicllm.agent.model.ModelSetting;
import com.basicllm.agent.model.PatientCondition;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface DiagnosisService {

    /**
     * AI 诊断
     *
     * @param setting   模型设置
     * @param condition 病人病历
     * @return 诊断结果
     */
    SseEmitter diagnose(ModelSetting setting, PatientCondition condition);

}
