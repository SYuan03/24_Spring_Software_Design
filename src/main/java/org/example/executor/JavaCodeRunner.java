package org.example.executor;

import org.example.model.runner.CompileResult;
import org.example.model.runner.ExecutionResult;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * @author SYuan03
 * @date 2024/4/11
 */
public class JavaCodeRunner implements CodeRunner {
    @Override
    public CompileResult compile(String sourceCodePath) {
        // 自定义编译输出目录
        Path outputDirectory = Paths.get(sourceCodePath).getParent().resolve("classes");
        // 确保输出目录存在
        try {
            Files.createDirectories(outputDirectory);
        } catch (IOException e) {
            // 异常处理, 可能权限不够
            e.printStackTrace();
        }

        // 使用更安全的方法直接调用 javac，指定输出目录和源文件
        ProcessBuilder processBuilder = new ProcessBuilder("javac", "-d", outputDirectory.toString(), sourceCodePath);

        try {
            Process process = processBuilder.start();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getErrorStream()));

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

    @Override
    public ExecutionResult execute(String compiledCodePath, String mainClassName, String input) {
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-cp", compiledCodePath, mainClassName);
        try {
            Process process = processBuilder.start();

            // 写入输入数据
            if (input != null) {
                try (OutputStream stdin = process.getOutputStream()) {
                    stdin.write(input.getBytes());
                    stdin.flush();
                }
            }

            // 读取输出结果
            String output = new BufferedReader(new InputStreamReader(process.getInputStream()))
                    .lines().collect(Collectors.joining("\n"));

            // 读取错误结果
            String errors = new BufferedReader(new InputStreamReader(process.getErrorStream()))
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
