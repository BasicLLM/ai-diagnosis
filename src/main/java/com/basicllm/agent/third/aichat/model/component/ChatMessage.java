package com.basicllm.agent.third.aichat.model.component;

import com.basicllm.agent.third.aichat.common.ChatRoleSerializer;
import com.basicllm.agent.third.aichat.model.constant.ChatRoleEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.List;

/**
 * 聊天消息
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatMessage {

    /**
     * 聊天角色
     */
    private ChatRoleEnum role;

    /**
     * 聊天内容
     */
    private String content;

    /**
     * partial 模式
     */
    private Boolean partial;

    /**
     * 工具调用
     */
    @JsonProperty("tool_calls")
    private List<ToolCall> toolCalls;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ToolCall {

        private Integer index;

        private String id;

        private String type;

        private Function function;

        @Data
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Function {

            private String name;

            private String arguments;

        }

    }


    public ChatMessage() {}

    public ChatMessage(ChatRoleEnum role, String content) {
        this.role =role;
        this.content = content;
    }

    public static ChatMessage create(ChatRoleEnum role, String content) {
        return new ChatMessage(role, content);
    }

}
