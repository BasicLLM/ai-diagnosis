package com.basicllm.agent.third.aichat.model.component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Tool {

    private String type;

    private Function function;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Function {

        /**
         * 函数名
         */
        private String name;

        /**
         * 函数描述
         */
        private String description;

        /**
         * 参数
         */
        private Parameters parameters;

        @Data
        public static class Parameters {

            private Map<String,Property> properties;

        }

        @Data
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Property {

            /**
             * 类型
             */
            private String type;

            /**
             * 说明
             */
            private String description;

            /**
             * 枚举
             */
            @JsonProperty("enum")
            private List<Object> enumeration;

        }

        /**
         * 类型
         */
        private String type;
    }


}
