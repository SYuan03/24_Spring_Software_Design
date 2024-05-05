package org.example.model.question;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.Main;
import org.example.executor.CodeRunner;
import org.example.factory.runner.CodeRunnerFactory;
import org.example.model.answer.Answer;
import org.example.model.answer.SingleContentAnswer;
import org.example.model.common.Sample;
import org.example.model.runner.CompileResult;
import org.example.model.runner.ExecutionResult;
import org.example.threadpool.SimpleThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author SYuan03
 * @date 2024/3/26
 */
@Setter
@Getter
@SuperBuilder
public class ProgrammingQuestion extends Question {

    // 静态变量，控制是否使用线程池
    private static boolean useThreadPool = true; // 默认使用线程池

    private static final Logger log = LoggerFactory.getLogger(ProgrammingQuestion.class);
    // getters and setters
    private List<Sample> samples;
    private int timeLimit;

    // TAG: 需要手动为静态变量useThreadPool添加setter方法
    public static void setUseThreadPool(boolean useThreadPool) {
        ProgrammingQuestion.useThreadPool = useThreadPool;
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

        // 合并编译逻辑，减少方法数量
        CompileResult compileResult = compileCode(codeRunner, studentAnswerPath);
        if (!compileResult.isSuccess()) {
            log.error("Compile failed: {}", compileResult.getErrorMessage());
            return 0;
        }

        // 合并执行测试逻辑
        if (!executeTests(codeRunner, studentAnswerPath, className)) {
            return 0;
        }

        return points;
    }

    private CompileResult compileCode(CodeRunner codeRunner, Path studentAnswerPath) {
        if (useThreadPool) {
            // 线程池执行编译逻辑
            // CountDownLatch 用于等待编译完成再执行测试
            CountDownLatch compileLatch = new CountDownLatch(1);
            AtomicReference<CompileResult> compileResultRef = new AtomicReference<>();
            SimpleThreadPool threadPool = SimpleThreadPool.getInstance();

            threadPool.submit(() -> {
                CompileResult compileResult = codeRunner.compile(studentAnswerPath.toString());
                compileResultRef.set(compileResult);
                compileLatch.countDown();
            });

            try {
                compileLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return new CompileResult(false, "Compilation interrupted");
            }

            return compileResultRef.get();
        } else {
            return codeRunner.compile(studentAnswerPath.toString());
        }
    }

    private boolean executeTests(CodeRunner codeRunner, Path studentAnswerPath, String className) {
        if (useThreadPool) {
            // 线程池执行测试逻辑
            // [ITER3] 新增timeLimit控制sample执行时间
            int totalSamples = samples.size();
            CountDownLatch executeLatch = new CountDownLatch(totalSamples);
            AtomicBoolean executionSuccess = new AtomicBoolean(true);
            SimpleThreadPool threadPool = SimpleThreadPool.getInstance();

            for (Sample sample : samples) {
                threadPool.submit(() -> {
                    String compiledClassesPath = studentAnswerPath.getParent().resolve("classes").toString();
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    Future<ExecutionResult> future = executor.submit(() -> codeRunner.execute(compiledClassesPath, className, sample.getInput()));
                    try {
                        ExecutionResult executionResult = future.get(timeLimit, TimeUnit.MILLISECONDS);

                        if (!executionResult.isSuccess() || !executionResult.getOutput().equals(sample.getOutput())) {
                            executionSuccess.set(false);
                        }
                    } catch (TimeoutException e) {
                        executionSuccess.set(false);
                    } catch (InterruptedException | ExecutionException e) {
                        Thread.currentThread().interrupt();
                        executionSuccess.set(false);
                    } finally {
                        // 保证代码超时或者执行失败时，停止线程，关闭程序
                        executor.shutdownNow();
                        executeLatch.countDown();
                    }
                });
            }

            try {
                executeLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }

            return executionSuccess.get();
        } else {    // 不开线程池也做了下时间限制
            for (Sample sample : samples) {
                String compiledClassesPath = studentAnswerPath.getParent().resolve("classes").toString();
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Future<ExecutionResult> future = executor.submit(() -> codeRunner.execute(compiledClassesPath, className, sample.getInput()));
                try {
                    ExecutionResult executionResult = future.get(timeLimit, TimeUnit.MILLISECONDS); // timeLimit should be your actual time limit

                    if (!executionResult.isSuccess() || !executionResult.getOutput().equals(sample.getOutput())) {
                        return false;
                    }
                } catch (TimeoutException e) {
                    return false;
                } catch (InterruptedException | ExecutionException e) {
                    Thread.currentThread().interrupt();
                    return false;
                } finally {
                    executor.shutdownNow();
                }
            }
            return true;
        }
    }

}
