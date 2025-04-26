package com.basicllm.agent.third.aichat.common;

import com.basicllm.agent.third.aichat.model.constant.ChatRoleEnum;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializerBase;

public class ChatRoleSerializer extends ToStringSerializerBase {

    public ChatRoleSerializer() {
        super(ChatRoleEnum.class);
    }

    @Override
    public String valueToString(Object o) {
        if (o instanceof ChatRoleEnum) {
            return ((ChatRoleEnum) o).value();
        }
        return o.toString();
    }

}
