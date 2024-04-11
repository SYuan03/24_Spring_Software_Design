package org.example.factory.runner;

import lombok.AllArgsConstructor;
import org.example.executor.CodeRunner;
import org.example.executor.JavaCodeRunner;


/**
 * @author SYuan03
 * @date 2024/4/11
 */
@AllArgsConstructor
public class JavaCodeRunnerFactory implements CodeRunnerFactory {

    @Override
    public CodeRunner createCodeRunner() {
        return new JavaCodeRunner();
    }
}

