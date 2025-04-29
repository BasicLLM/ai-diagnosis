package com.basicllm.agent.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 提示词读取器
 */
public class PromptReader {

    /**
     * 读取指定文件的提示词内容。
     *
     * @param promptPath 项目目录下 {@code /prompt/} 下的提示词文件路径
     * @return 提示词文件内容
     */
    public static String readPrompt(String promptPath) {
        String path = System.getProperty("user.dir") + "/prompt/" + promptPath;
        return readTextFile(path);
    }

    /**
     * 读取指定模型对应的提示词，如果不存在指定模型的提示词，那么就返回默认路径下的提示词
     *
     * @param model      模型名
     * @param promptPath 项目目录下 {@code /prompt/{model}/} 下的提示词文件路径
     * @return 提示词文件内容
     */
    public static String readPrompt(String model,String promptPath) {
        String path = System.getProperty("user.dir") + "/prompt/" + model + "/" + promptPath;
        if (Files.exists(Path.of(path))) {
            return readTextFile(path);
        } else {
            return readPrompt(promptPath);
        }
    }

    /**
     * 读取指定路径的文本文件
     *
     * @param filePath 文件路径
     * @return 文件内容
     */
    private static String readTextFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n"); // 追加每行内容
            }
        } catch (IOException e) {
            System.err.println("读取文件时发生错误：" + e.getMessage());
        }
        return content.toString();
    }

}
