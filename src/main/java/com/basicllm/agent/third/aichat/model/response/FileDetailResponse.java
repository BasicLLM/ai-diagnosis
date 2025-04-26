package com.basicllm.agent.third.aichat.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileDetailResponse {

    /**
     * 内容
     */
    private String content;

    /**
     * 文件类型
     */
    @JsonProperty("file_type")
    private String fileType;

    /**
     * 文件名
     */
    private String filename;

    /**
     * 标题
     */
    private String title;

    /**
     * 类型
     */
    private String type;

}
