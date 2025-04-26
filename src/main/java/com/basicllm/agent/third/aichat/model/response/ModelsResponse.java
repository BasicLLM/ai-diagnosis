package com.basicllm.agent.third.aichat.model.response;

import com.basicllm.agent.third.aichat.model.component.AiModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModelsResponse {

    private String object;

    private ArrayList<AiModel> data;

}
