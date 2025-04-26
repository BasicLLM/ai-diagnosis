package com.basicllm.agent.third.aichat.model.component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AiModel {

    private String id;

    private String object;

    private Long created;

    @JsonProperty("owned_by")
    private String ownedBy;

    private List<Permission> permission;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Permission {

        private String id;

        private String object;

        private Long created;

        @JsonProperty("allow_create_engine")
        private Boolean allowCreateEngine;

        @JsonProperty("allow_sampling")
        private Boolean allowSampling;

        @JsonProperty("allowLogprobs")
        private Boolean allow_logprobs;

        @JsonProperty("allow_search_indices")
        private Boolean allowSearchIndices;

        @JsonProperty("allow_view")
        private Boolean allowView;

        @JsonProperty("allow_fine_tuning")
        private Boolean allowFineTuning;

        private String organization;

        private String group;

        @JsonProperty("is_blocking")
        private Boolean isBlocking;

    }

    private String root;

    private String parent;

}
