package com.basicllm.agent.service;

import com.basicllm.agent.model.PatientCondition;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface DiagnosisService {

    /**
     * AI 诊断
     *
     * @param provider  AI 提供商
     * @param model     AI 模型
     * @param condition 病人病历
     * @return 诊断结果
     */
    SseEmitter diagnose(String provider,String model,PatientCondition condition);

}
