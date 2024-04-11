package org.example.model.question;

import lombok.experimental.SuperBuilder;
import org.example.Main;
import org.example.executor.CodeRunner;
import org.example.factory.runner.CodeRunnerFactory;
import org.example.model.answer.Answer;
import org.example.model.answer.SingleContentAnswer;
import org.example.model.common.Sample;
import org.example.model.runner.CompileResult;
import org.example.model.runner.ExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author SYuan03
 * @date 2024/3/26
 */
@SuperBuilder
public class ProgrammingQuestion extends Question {
    private static final Logger log = LoggerFactory.getLogger(ProgrammingQuestion.class);
    private List<Sample> samples;
    private int timeLimit;

//    public ProgrammingQuestion(int id, String description, int points, List<Sample> samples, int timeLimit) {
//        super(id, description, points);
//        this.samples = samples;
//        this.timeLimit = timeLimit;
//    }

    // getters and setters
    public List<Sample> getSamples() {
        return samples;
    }

    public void setSamples(List<Sample> samples) {
        this.samples = samples;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    /**
     * @param answer
     * @return
     * 迭代一对于编程题的实现
     * ⽆论⽤户是否回答了编程题，以及回答是否正确，你都应该将编程题视为回答正确
     * Tag: 这个方法其实并没有被调用到，因为编程题不回答也是满分。
     *
     * 迭代二：编程题的分数计算
     */
    @Override
    public int calculateScore(Answer answer) {
        if (!(answer instanceof SingleContentAnswer)) {
            throw new IllegalArgumentException("Answer type is incorrect");
        }
        SingleContentAnswer singleContentAnswer = (SingleContentAnswer) answer;

        // 根据content（也就是文件路径）获取代码语言类型
        // 获取最后一个.之后的字符串
        String language = singleContentAnswer.getContent().substring(singleContentAnswer.getContent().lastIndexOf(".") + 1);
        CodeRunner codeRunner = CodeRunnerFactory.getFactory(language).createCodeRunner();

        // 1. 先编译，如果编译失败，直接返回0分，需要对路径与Main.answerPath进行拼接
        // [BUGFIX] 路径拼接，不能直接使用+，因为+是字符串拼接，会有相对路径问题
        Path studentAnswerPath = Paths.get(Main.answersPath).resolve(singleContentAnswer.getContent());
        String className = singleContentAnswer.getContent().substring(singleContentAnswer.getContent().lastIndexOf("/") + 1, singleContentAnswer.getContent().lastIndexOf("."));
        log.info("studentAnswerPath: {}", studentAnswerPath);
        log.info("className: {}", className);
        CompileResult compileResult = codeRunner.compile(studentAnswerPath.toString());

        if (!compileResult.isSuccess()) {
            log.error("Compile failed: {}", compileResult.getErrorMessage());
            return 0;
        }

        // 2. 编译成功，执行代码
        for (Sample sample : samples) {
            String input = sample.getInput();
            String output = sample.getOutput();

            String compiledClassesPath = studentAnswerPath.getParent().resolve("classes").toString();
            ExecutionResult executionResult = codeRunner.execute(compiledClassesPath, className, input);
            if (!executionResult.isSuccess()) {
                log.error("Execution failed: {}", executionResult.getErrorMessage());
                return 0;
            }
            if (!executionResult.getOutput().equals(output)) {
                log.error("Output not match: expected {}, but got {}", output, executionResult.getOutput());
                return 0;
            }
        }

        // 3. 返回满分
        return points;
    }

}
