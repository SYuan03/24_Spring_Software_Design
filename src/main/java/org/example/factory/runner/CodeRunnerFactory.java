package org.example.factory.runner;

import org.example.executor.CodeRunner;

/**
 * @author admin
 * @date 2024/4/11
 */
public interface CodeRunnerFactory {
    CodeRunner createCodeRunner();

    static CodeRunnerFactory getFactory(String language) {
        switch (language) {
            case "java":
                return new JavaCodeRunnerFactory();
            default:
                throw new IllegalArgumentException("Invalid language: " + language);
        }
    }

}
