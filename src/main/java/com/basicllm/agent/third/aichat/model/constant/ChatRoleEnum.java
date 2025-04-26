package com.basicllm.agent.third.aichat.model.constant;

import com.basicllm.agent.third.aichat.common.StandardEnum;

import java.util.HashMap;
import java.util.Map;

public enum ChatRoleEnum implements StandardEnum<ChatRoleEnum> {

    SYSTEM    (0,"system"),
    ASSISTANT (1,"assistant"),
    USER      (2,"user"),
    TOOL      (3,"tool")
    ;

    private final int code;
    private final String value;

    private static Map<String, ChatRoleEnum> map;
    private static Map<Integer, ChatRoleEnum> codeMap;

    ChatRoleEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int code() {
        return code;
    }

    public String value() {
        return value;
    }

    public static ChatRoleEnum valueOf(int code) {
        if (codeMap == null) {
            codeMap = new HashMap<>();
            for (ChatRoleEnum chatRoleEnum : ChatRoleEnum.values()) {
                codeMap.put(chatRoleEnum.code(), chatRoleEnum);
            }
        }
        return codeMap.get(code);
    }

    public static ChatRoleEnum get(String modelName) {
        if (map == null) {
            map = new HashMap<>();
            for (ChatRoleEnum chatRoleEnum : ChatRoleEnum.values()) {
                map.put(chatRoleEnum.value(), chatRoleEnum);
            }
        }
        return map.get(modelName);
    }

}
