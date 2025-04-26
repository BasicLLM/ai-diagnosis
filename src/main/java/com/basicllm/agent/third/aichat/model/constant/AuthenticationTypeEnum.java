package com.basicllm.agent.third.aichat.model.constant;

import com.basicllm.agent.third.aichat.common.StandardEnum;

import java.util.HashMap;
import java.util.Map;

public enum AuthenticationTypeEnum implements StandardEnum<AuthenticationTypeEnum> {

    /**
     * 免费的密钥
     */
    FREE (1,"free"),
    /**
     * 付费的密钥
     */
    PLUS (2,"plus"),
    ;

    private final int code;
    private final String value;
    private static Map<Integer, AuthenticationTypeEnum> codeMap;

    AuthenticationTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int code() {
        return code;
    }

    public String value() {
        return value;
    }

    public static AuthenticationTypeEnum valueOf(int code) {
        if (codeMap == null) {
            codeMap = new HashMap<>();
            for (AuthenticationTypeEnum type : AuthenticationTypeEnum.values()) {
                codeMap.put(type.code(), type);
            }
        }
        return codeMap.get(code);
    }

}
