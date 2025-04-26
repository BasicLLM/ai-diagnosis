package com.basicllm.agent.third.aichat.model.request;

import com.basicllm.agent.third.aichat.model.component.ChatMessage;
import com.basicllm.agent.third.aichat.model.component.Tool;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 聊天补全请求
 * <p>
 *     <a href="https://platform.openai.com/docs/api-reference/chat/create">Openai chat completion API</a>
 * </p>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatCompletionRequest {

    /**
     * <b>[必选]</b> 请求的模型ID
     */
    private String model;

    /**
     * <b>[必选]</b> 生成聊天补全的消息依据，
     * 使用<a href="https://platform.openai.com/docs/guides/chat/introduction">聊天格式</a>
     */
    private List<ChatMessage> messages;

    /**
     * <b>[可选]</b> temperature 采样
     * <p>
     * <b>[默认值]</b> 1.0
     * </p>
     * 使用哪个 temperature 采样值，介于0和2之间。较高的值，如0.8会使输出更随机，而较低的
     * 值，如0.2会使输出更加聚焦和确定性。
     * <p color = "green">
     * 我们通常建议更改这个或 {@code top_p} ，但不是两者都更改。
     * </p>
     */
    private Double temperature;

    /**
     * <b>[可选]</b> 核心采样
     * <p>
     * <b>[默认值]</b> 1.0
     * </p>
     * 一种替代 temperature 采样的方法称为核心采样，其中模型考虑到具有 {@code top_p}概率
     * 质量的 token 的结果。因此，0.1意味着只考虑组成前10％概率质量的 tokens。
     * <p color = "green">
     * 我们通常建议更改这个或 {@code temperature} ，但不是两者都更改。
     * </p>
     */
    @JsonProperty("top_p")
    private Double topP;

    /**
     * <b>[可选]</b> 返回待选消息数量
     * <p>
     * <b>[默认值]</b> 1
     * </p>
     * 返回多少个待选消息
     */
    private Integer n;

    /**
     * <b>[可选]</b> 流开关
     * <p>
     * <b>[默认值]</b> {@code false}
     * </p>
     * 如果设置，将发送部分消息增量，就像在 ChatGPT 中一样。token 将在可用时作为仅数据服务器发送的事
     * 件发送，流以 {@code data:[DONE]} 消息终止。
     */
    private Boolean stream;

    /**
     * <b>[可选]</b> 停止序列
     * <p>
     * <b>[默认值]</b> {@code null}
     * </p>
     * 最多4个序列，API将停止生成更多 token。
     */
    private List<String> stop;

    /**
     * <b>[可选]</b> 最大 token 数
     * <p>
     * <b>[默认值]</b> 4096
     * </p>
     * 生成的答案允许的最大 token 数。默认情况下，模型可以返回的 token 数为（4096个提示 token）。
     */
    @JsonProperty("max_tokens")
    private Integer maxTokens;

    /**
     * <b>[可选]</b> 存在惩罚参数
     * <p>
     * <b>[默认值]</b> 0.0
     * </p>
     * 数字介于-2.0和2.0之间。正值根据到目前为止是否出现在文本中来惩罚新标记，从而增加模型谈论新主题的
     * 可能性。
     */
    @JsonProperty("presence_penalty")
    private Double presencePenalty;

    /**
     * <b>[可选]</b> 频率惩罚参数
     * <p>
     * <b>[默认值]</b> 0.0
     * </p>
     * 数字介于-2.0和2.0之间。正值根据文本中的现有频率惩罚新标记，从而降低模型逐字重复同一行的可能性。
     */
    @JsonProperty("frequency_penalty")
    private Double frequencyPenalty;

    /**
     * <b>[可选]</b> logit 偏置
     * <p>
     * <b>[默认值]</b> {@code null}
     * </p>
     * <p>
     * 修改完成时出现指定标记的可能性。
     * </p>
     * 接受一个json对象，该对象将 token（由 tokenizer 分词器中的 token ID指定）映射到-100到100之间的
     * 相关偏差值。在数学上，在采样之前，将偏差添加到模型生成的逻辑中。每个模型的确切效果会有所不同，但介于-1
     * 和1之间的值应该会降低或增加选择的可能性；像-100或100这样的值应该会导致相关令牌的禁止或独占选择。
     */
    @JsonProperty("logit_bias")
    private Map<String,Double> logitBias;

    /**
     * <b>[可选]</b> 用户
     * <p>
     * 代表最终用户的唯一标识符，可帮助OpenAI监控和检测滥用。了解更多信息。
     * </p>
     */
    private String user;

    /**
     * <b>[可选]</b> 工具集
     */
    private List<Tool> tools;

}
