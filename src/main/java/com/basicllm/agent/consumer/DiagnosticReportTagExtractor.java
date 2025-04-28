package com.basicllm.agent.consumer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiagnosticReportTagExtractor {

    // 定义正则表达式，匹配 <diseases> 和 </diseases> 标签之间的内容
    public static final String DISEASES_REGEX = DiagnosticReportLabelTag.DISEASES_START + "(.*?)" + DiagnosticReportLabelTag.DISEASES_END;
    public static final Pattern DISEASES_PATTERN = Pattern.compile(DISEASES_REGEX, Pattern.DOTALL);

    // 静态方法，提取 <diseases> 标签内的内容
    public static String extractDiseasesContent(String input) {
        Matcher matcher = DISEASES_PATTERN.matcher(input);

        // 如果找到匹配的内容，返回提取的内容（去除首尾空白字符）
        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        // 如果未找到匹配的内容，返回空字符串
        return "";
    }

}
