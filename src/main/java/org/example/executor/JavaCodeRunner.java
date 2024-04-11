package org.example.executor;

import lombok.extern.slf4j.Slf4j;
import org.example.model.runner.CompileResult;
import org.example.model.runner.ExecutionResult;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author SYuan03
 * @date 2024/4/11
 */
@Slf4j
public class JavaCodeRunner implements CodeRunner {
    @Override
    public CompileResult compile(String sourceCodePath) {
        // 自定义编译输出目录
        Path sourceFilePath = Paths.get(sourceCodePath);
        Path outputDirectory = sourceFilePath.getParent().resolve("classes");
        // 确保输出目录存在
        try {
            Files.createDirectories(outputDirectory);
            log.info("Output directory created: {}", outputDirectory);
        } catch (IOException e) {
            // 异常处理, 可能权限不够
            log.error("IOException occurred", e);
        }

        // 检查源文件是否存在
        if (!Files.exists(sourceFilePath)) {
            return CompileResult.builder()
                    .success(false)
                    .errorMessage("Source code file does not exist")
                    .build();
        }

        // [BUGFIX] 处理文件名与public class名不一致的情况
        // 使用正则表达式来处理，并且考虑先移除注释和字符串字面量，防止对于注释和字符串字面量的误判
        // 不使用解析库的原因是考虑到性能开销和简单性
        String sourceFileName = sourceFilePath.getFileName().toString();
        String expectedClassName = sourceFileName.substring(0, sourceFileName.lastIndexOf('.'));

        try {
            List<String> lines = Files.readAllLines(sourceFilePath, StandardCharsets.UTF_8);

            // 将注释和字符串字面量移除，减少正则表达式误判的情况
            String sourceWithoutCommentsAndStrings = removeCommentsAndStrings(String.join("\n", lines));

            // 使用正则表达式匹配公共类声明
            // 正则表达式现在需要检查文件名和类名是否匹配
            Pattern pattern = Pattern.compile("\\bpublic\\s+class\\s+" + Pattern.quote(expectedClassName) + "\\b");
            Matcher matcher = pattern.matcher(sourceWithoutCommentsAndStrings);

            if (matcher.find()) {
                // 匹配成功，公共类名与文件名匹配
                // 进行正常编译
                // 使用更安全的方法直接调用 javac，指定输出目录和源文件
                // ATTENTION: 这里新启了一个线程，不知道与外部的线程有没有关系
                ProcessBuilder processBuilder = new ProcessBuilder("javac", "-J-Dfile.encoding=UTF-8", "-encoding", "UTF-8", "-d", outputDirectory.toString(), sourceCodePath);

                Process process = processBuilder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));
                String line;
                StringBuilder errorOutput = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    errorOutput.append(line).append("\n");
                }

                int exitVal = process.waitFor();
                if (exitVal == 0) {
                    // 编译成功
                    return CompileResult.builder()
                            .success(true)
                            .errorMessage(null)
                            .build();
                } else {
                    // 编译失败，包含错误信息
                    log.error("Compilation failed in compile: {}", errorOutput);
                    return CompileResult.builder()
                            .success(false)
                            .errorMessage(errorOutput.toString())
                            .build();
                }
            } else {
                // 匹配失败，公共类名与文件名不匹配
                String errorMessage = "No public class name matches the file name: " + expectedClassName;
                log.error(errorMessage);
                return CompileResult.builder()
                        .success(false)
                        .errorMessage(errorMessage)
                        .build();
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt(); // 重置中断状态
            log.error("Exception occurred", e);
            return CompileResult.builder()
                    .success(false)
                    .errorMessage("Exception occurred: " + e.getMessage())
                    .build();
        }

    }

    // TAG: 这里题目要求通过命令行参数传递输入
    @Override
    public ExecutionResult execute(String compiledCodePath, String mainClassName, String input) {
        log.info("Compiled code path: {}", compiledCodePath);
        log.info("Executing main class: {}", mainClassName);
        log.info("Input: {}", input);
        // 使用utf-8编码执行java命令
        List<String> commands = new ArrayList<>();
        commands.add("java");
        commands.add("-Dfile.encoding=UTF-8");
        commands.add("-cp");
        commands.add(compiledCodePath);
        commands.add(mainClassName);
        // 添加命令行参数
        if (input != null) {
            String[] args = input.split(" ");
            Collections.addAll(commands, args);
        }
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        try {
            Process process = processBuilder.start();

            // 读取输出结果
            String output = new BufferedReader(new InputStreamReader(
                    process.getInputStream(), StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));

            // 读取错误结果
            String errors = new BufferedReader(new InputStreamReader(
                    process.getErrorStream(), StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));

            int exitVal = process.waitFor();
            if (exitVal == 0) { // 执行成功
                return new ExecutionResult(true, output, null);
            } else { // 执行失败
                return new ExecutionResult(false, output, errors);
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt(); // 重置中断状态
            log.error("Exception occurred", e);
            return new ExecutionResult(false, null, "Exception occurred: " + e.getMessage());
        }
    }

    // Helper method to remove comments and strings from Java source code
    private String removeCommentsAndStrings(String source) {
        // Patterns for block comments, line comments and string literals
        String blockCommentRegex = "/\\*.*?\\*/";
        String lineCommentRegex = "//.*";
        String stringLiteralRegex = "\"(?:\\\\.|[^\\\\\"])*\"";

        String combinedRegex = String.join("|", blockCommentRegex, lineCommentRegex, stringLiteralRegex);

        return source.replaceAll(combinedRegex, " ");
    }

}
