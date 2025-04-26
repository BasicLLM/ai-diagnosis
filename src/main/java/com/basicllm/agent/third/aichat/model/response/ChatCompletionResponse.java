package com.basicllm.agent.third.aichat.model.response;

import com.basicllm.agent.third.aichat.model.component.ChatMessage;
import com.basicllm.agent.third.aichat.model.constant.ChatMessageFinishReasonEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatCompletionResponse {

    /**
     * 补全id
     */
    private String id;

    /**
     * 模型
     */
    private String model;

    private String object;

    /**
     * 创建时间
     */
    private Long created;

    /**
     * 补全可选项
     */
    private List<Choice> choices;

    /**
     * 可选项
     */
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Choice {

        /**
         * 选择的索引
         */
        private Integer index;

        /**
         * 请求参数stream为true返回是delta
         */
        private ChatMessage delta;

        /**
         * 请求参数stream为false返回是message
         */
        private ChatMessage message;

        /**
         * 结束原因
         */
        @JsonProperty("finish_reason")
        private ChatMessageFinishReasonEnum finishReason;

    }

    /**
     * 使用情况
     */
    private Usage usage;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Usage {

        /**
         * 提示token
         */
        @JsonProperty("prompt_tokens")
        private Integer promptTokens;

        /**
         * 补全token
         */
        @JsonProperty("completion_tokens")
        private Integer completionTokens;

        /**
         * 总token
         */
        @JsonProperty("total_tokens")
        private Integer totalTokens;

    }

}
