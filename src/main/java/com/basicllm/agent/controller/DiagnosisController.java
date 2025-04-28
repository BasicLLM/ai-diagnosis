package com.basicllm.agent.controller;

import com.basicllm.agent.model.DiagnosisRequest;
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
     * @param diagnosisRequest 诊断请求对象
     * @return 诊断结果
     */
    @PostMapping("/diagnose")
    public SseEmitter diagnose(
            @RequestBody DiagnosisRequest diagnosisRequest
            ) {
        return diagnosisService.diagnose(
                diagnosisRequest.getSetting(),
                diagnosisRequest.getCondition()
        );
    }

}
