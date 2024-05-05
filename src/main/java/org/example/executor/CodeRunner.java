package org.example.executor;

import org.example.model.runner.CompileResult;
import org.example.model.runner.ExecutionResult;

/**
 * @author admin
 * @date 2024/4/11
 */
public interface CodeRunner {
    CompileResult compile(String sourceCodePath);
    ExecutionResult execute(String compiledCodePath, String mainClassName, String input, int timeLimit);
}