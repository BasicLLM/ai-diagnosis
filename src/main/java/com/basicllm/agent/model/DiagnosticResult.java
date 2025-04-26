package com.basicllm.agent.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DiagnosticResult {

    /**
     * 疾病
     */
    private List<String> diseases;

    /**
     * 病因
     */
    private String reasons;

}
