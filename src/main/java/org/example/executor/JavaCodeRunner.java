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
        Path outputDirectory = Paths.get(sourceCodePath).getParent().resolve("classes");
        // 确保输出目录存在
        try {
            Files.createDirectories(outputDirectory);
            log.info("Output directory created: {}", outputDirectory);
        } catch (IOException e) {
            // 异常处理, 可能权限不够
            e.printStackTrace();
        }

        // 检查源文件是否存在
        if (!Files.exists(Paths.get(sourceCodePath))) {
            return CompileResult.builder()
                    .success(false)
                    .errorMessage("Source code file does not exist")
                    .build();
        }

        // 使用更安全的方法直接调用 javac，指定输出目录和源文件
        ProcessBuilder processBuilder = new ProcessBuilder("javac", "-J-Dfile.encoding=UTF-8", "-encoding", "UTF-8", "-d", outputDirectory.toString(), sourceCodePath);


        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));
            String line;
            StringBuilder errorOutput = new StringBuilder();
            while ((line = reader.readLine()) != null) {
//                System.out.println("hello");
//                System.out.println(line);
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
        } catch (IOException | InterruptedException e) {
            // 处理异常情况，包括重置线程中断状态
            Thread.currentThread().interrupt();
            return CompileResult.builder()
                    .success(false)
                    .errorMessage("Exception occurred during the compile process: " + e.getMessage())
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
            e.printStackTrace();
            return new ExecutionResult(false, null, "Exception occurred: " + e.getMessage());
        }
    }

}
