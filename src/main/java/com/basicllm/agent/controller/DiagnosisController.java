package com.basicllm.agent.controller;

import com.basicllm.agent.model.PatientCondition;
import com.basicllm.agent.service.DiagnosisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/v1/ai")
public class DiagnosisController {

    @Autowired
    private DiagnosisService diagnosisService;

    /**
     * AI 诊断
     *
     * @param provider  AI 提供商
     * @param model     AI 模型
     * @param condition 病人病历
     * @return 诊断结果
     */
    @PostMapping("/diagnose")
    public SseEmitter diagnose(
            @RequestHeader("provider") String provider,
            @RequestHeader("model") String model,
            @RequestBody PatientCondition condition
    ) {
        return diagnosisService.diagnose(provider, model, condition);
    }

}
