package com.basicllm.agent.third.aichat.common;

import com.fasterxml.jackson.annotation.JsonValue;

public interface StandardEnum<T extends Enum<T>> {

    int code();

    @JsonValue
    String value();

}
