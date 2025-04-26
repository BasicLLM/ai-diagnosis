package com.basicllm.agent.third.aichat.model.constant;

import com.basicllm.agent.third.aichat.common.StandardEnum;

import java.util.HashMap;
import java.util.Map;

public enum ChatMessageFinishReasonEnum implements StandardEnum<ChatMessageFinishReasonEnum> {

    STOP(0,"stop"),
    LENGTH(1,"length"),
    CONTENT_FILTER(2,"content_filter"),
    NULL(3,"null"),
    TOOL_CALLS(4,"tool_calls"),
    ;
    private final int code;
    private final String value;

    private static Map<Integer, ChatMessageFinishReasonEnum> codeMap;

    ChatMessageFinishReasonEnum(int code,String value) {
        this.code = code;
        this.value = value;
    }

    public int code() {
        return code;
    }

    public String value() {
        return value;
    }

    public static ChatMessageFinishReasonEnum valueOf(int code) {
        if (codeMap == null) {
            codeMap = new HashMap<>();
            for (ChatMessageFinishReasonEnum reasonEnum : ChatMessageFinishReasonEnum.values()) {
                codeMap.put(reasonEnum.code(), reasonEnum);
            }
        }
        return codeMap.get(code);
    }

}
