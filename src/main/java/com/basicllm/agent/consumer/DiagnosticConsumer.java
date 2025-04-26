package com.basicllm.agent.consumer;

import com.basicllm.agent.model.DiagnosticResult;
import com.basicllm.agent.third.aichat.model.component.ChatMessage;
import com.basicllm.agent.third.aichat.model.constant.ChatMessageFinishReasonEnum;
import com.basicllm.agent.third.aichat.model.response.ChatCompletionResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DiagnosticConsumer implements Consumer<ChatCompletionResponse> {

    private final SseEmitter sseEmitter;

    // 诊断的疾病
    private final StringBuilder diseasesBuilder = new StringBuilder();

    private boolean diseaseDone = false;

    public DiagnosticConsumer(SseEmitter sseEmitter) {
        this.sseEmitter = sseEmitter;
    }

    @Override
    public void accept(ChatCompletionResponse response) {

        try {
            ChatMessageFinishReasonEnum finishReason =
                    response.getChoices().get(0).getFinishReason();

            if (finishReason != null && finishReason.equals(ChatMessageFinishReasonEnum.STOP)) {
                return;
            }

            // 获取已经传入的疾病文本
            ChatMessage message = response.getChoices().get(0).getDelta();
            String content = message.getContent();

            DiagnosticResult result = new DiagnosticResult();

            // 如果疾病没有输出完毕
            if (!diseaseDone) {

                // 拼接疾病文本
                diseasesBuilder.append(content);
                String diseasesText = diseasesBuilder.toString();

                // 检查是否存在疾病结束标签
                if (diseasesText.contains(DiagnosticReportLabelTag.DISEASES_END)) {

                    // 去除疾病开始标签
                    String tContent = content.replace(DiagnosticReportLabelTag.DISEASES_START,"");

                    // 将疾病和诊断原因分开
                    String[] segments = tContent.split(DiagnosticReportLabelTag.DISEASES_END);

                    // 提取疾病
                    List<String> extractedDiseaseList = extractDiseases(diseasesText);
                    result.setDiseases(extractedDiseaseList);

                    // 提取病因
                    if (segments.length > 1) {
                        result.setReasons(segments[1]);
                    }

                    diseaseDone = true;

                } else {
                    return;
                }

            } else {
                // 如果疾病标签已经完成，那么直接将其加入病因中
                result.setReasons(content);
            }

            sseEmitter.send(result);

        } catch (IOException e) {
            sseEmitter.completeWithError(e);
        }

    }

    /**
     * 提取疾病
     *
     * @param text 文本
     * @return 提取的疾病列表
     */
    private List<String> extractDiseases(String text) {

        // 去除标签
        String diseasesText = text
                .replace(DiagnosticReportLabelTag.DISEASES_START,"")
                .replace(DiagnosticReportLabelTag.DISEASES_END,"");

        // 将疾病进行分割
        String[] diseases = diseasesText.split(DiagnosticReportLabelTag.SEPARATOR);

        // 将最后未截取全的疾病放在 diseaseBuilder 中
        List<String> extractedDiseaseList = new ArrayList<>();

        for (String disease: diseases) {
            String diseaseTrim = disease.trim();
            if (!diseaseTrim.isEmpty()) {
                extractedDiseaseList.add(diseaseTrim);
            }
        }

        return extractedDiseaseList;
    }

}
