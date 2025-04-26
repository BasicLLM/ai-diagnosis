package com.basicllm.agent.third.aichat.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileInfoResponse {

    /**
     * 文件ID
     */
    private String id;

    /**
     * 对象类型
     */
    private String object;

    /**
     * 文件大小
     */
    private Integer bytes;

    /**
     * 创建时间
     */
    @JsonProperty("created_at")
    private Integer createdAt;

    /**
     * 文件名
     */
    private String filename;

    /**
     * 目的
     */
    private String purpose;

    /**
     * 状态
     */
    private String status;

    /**
     * 状态详情
     */
    @JsonProperty("status_details")
    private String statusDetails;

}

