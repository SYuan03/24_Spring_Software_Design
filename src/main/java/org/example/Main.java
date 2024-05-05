package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.executor.CodeRunner;
import org.example.factory.parser.ParserFactory;
import org.example.factory.runner.CodeRunnerFactory;
import org.example.model.answer.Answer;
import org.example.model.answer.AnswerSheet;
import org.example.model.answer.SingleContentAnswer;
import org.example.model.exam.ExamSheet;
import org.example.model.question.ProgrammingQuestion;
import org.example.model.question.Question;
import org.example.model.runner.CompileResult;
import org.example.parser.Parser;
import org.example.util.CyclomaticComplexityCalculator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Main {
    public static String examsPath;
    public static String answersPath;
    public static boolean useThreadPool = true;

    public static void main(String[] args) {
        String casePath = args[0];
        // 题目文件夹路径
        String examsPath = casePath + System.getProperty("file.separator") + "exams";
        // 答案文件夹路径
        String answersPath = casePath + System.getProperty("file.separator") + "answers";
        // 输出文件路径
        String output = args[1];

        // 是否开启编程题的线程池
        ProgrammingQuestion.setUseThreadPool(Main.useThreadPool);

        // 赋值，全局变量
        Main.examsPath = examsPath;
        Main.answersPath = answersPath;

        log.debug("examsPath: {}", examsPath);
        log.debug("answersPath: {}", answersPath);
        log.debug("output: {}", output);

        Path path = Paths.get(output);
        // 创建全新文件
        try {
            Files.deleteIfExists(path);
            Files.createFile(path);
        } catch (IOException e) {
            log.error("IOException:", e);
        }

        // 判断是哪个任务，根据output路径的最后一段判断是计算分数还是计算复杂度
        // 这一段实现很丑陋，但是测试就是这么写的，不确定是否有更好的方法
        String outputFileName = path.getFileName().toString();
        if (outputFileName.endsWith("output_complexity.csv")) {
            writeComplexity(path, answersPath);
        } else if (outputFileName.endsWith("output.csv")) {
            writeScore(path, examsPath, answersPath);
        }
    }

    // 计算分数
    private static void writeScore(Path path, String examsPath, String answersPath) {
        // 先写入一行表头
        try {
            Files.write(path, Collections.singleton("examId, stuId, score"), java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            log.error("IOException:", e);
        }
        // 1. 先把所有的ExamSheet读出来存在map里
        // Tag: 其实可以用文件名对应关系，但没说一定是一一对应的，所以还是用map比较好
        Map<Integer, ExamSheet> examSheetMap = readAndParseExamSheets(examsPath);
        // 2. 对answersPath下的所有文件(目前只有json)进行读取并计算
        File answersDir = new File(answersPath);
        File[] answerFiles = answersDir.listFiles();
        if (answerFiles != null) {
            for (File answerFile : answerFiles) {
                log.debug("Parsing answer file: {}", answerFile.getName());
                Parser parser = ParserFactory.createParser(answerFile.getName());
                if (parser != null) {
                    AnswerSheet answerSheet = parser.parseAnswerSheet(answerFile);
                    // 3. 计算得分
                    log.debug("examSheet: {}", examSheetMap.get(answerSheet.getExamId()));
                    log.debug("answerSheet: {}", answerSheet);
                    int score = calculateSheetScore(examSheetMap.get(answerSheet.getExamId()), answerSheet);
                    // 4. 输出到文件
                    try {
                        log.info("Writing to file: {}", path);
                        log.info("ExamId: {}, StudentId: {}, Score: {}\n", answerSheet.getExamId(), answerSheet.getStuId(), score);
                        // 追加不是覆盖
                        Files.write(path, Collections.singleton(answerSheet.getExamId() + "," + answerSheet.getStuId() + "," + score), java.nio.file.StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        log.error("IOException:", e);
                    }
                }
            }
        }
    }

    // 计算圈复杂度，跟exam没什么关系了，只要编译answerPath下的所有文件，然后编译成功的计算圈复杂度就行
    private static void writeComplexity(Path path, String answersPath) {
        // 先写入一行表头
        try {
            Files.write(path, Collections.singleton("examId, stuId, qId, complexity"), java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            log.error("IOException:", e);
        }
        // 1. 对answersPath下的所有文件(目前只有java)进行读取并计算
        File answersDir = new File(answersPath);
        File[] answerFiles = answersDir.listFiles();
        if (answerFiles != null) {
            for (File answerFile : answerFiles) {
                log.debug("Parsing answer file: {}", answerFile.getName());
                Parser parser = ParserFactory.createParser(answerFile.getName());
                if (parser != null) {
                    AnswerSheet answerSheet = parser.parseAnswerSheet(answerFile);
                    // 3. 对answerSheet下的所有answer进行编译并计算圈复杂度
                    // 如果answer是"code-answer"开头的，就是编程题
                    for (Answer answer : answerSheet.getAnswers()) {
                        if (answer instanceof SingleContentAnswer) {
                            SingleContentAnswer singleContentAnswer = (SingleContentAnswer) answer;
                            // 这个地方也挺丑陋的
                            // [ITER3]
                            if (singleContentAnswer.getContent().startsWith("code-answer")) {
                                // 根据content（也就是文件路径）获取代码语言类型
                                // 获取最后一个.之后的字符串
                                String language = singleContentAnswer.getContent().substring(singleContentAnswer.getContent().lastIndexOf(".") + 1);
                                CodeRunner codeRunner = CodeRunnerFactory.getFactory(language).createCodeRunner();
                                // 1. 先编译，如果编译失败，直接返回0分，需要对路径与Main.answerPath进行拼接
                                Path studentAnswerPath = Paths.get(Main.answersPath).resolve(singleContentAnswer.getContent());
                                String className = singleContentAnswer.getContent().substring(singleContentAnswer.getContent().lastIndexOf("/") + 1, singleContentAnswer.getContent().lastIndexOf("."));
                                log.info("studentAnswerPath: {}", studentAnswerPath);
                                log.info("className: {}", className);
                                CompileResult compileResult = codeRunner.compile(studentAnswerPath.toString());
                                if (compileResult.isSuccess()) {
                                    CyclomaticComplexityCalculator calculator = new CyclomaticComplexityCalculator();
                                    int complexity = calculator.calculateClassComplexity(studentAnswerPath.toString());
                                    try {
                                        log.info("Writing to file: {}", path);
                                        log.info("ExamId: {}, StudentId: {}, QuestionId: {}, Complexity: {}", answerSheet.getExamId(), answerSheet.getStuId(), answer.getId(), complexity);
                                        // 追加不是覆盖
                                        Files.write(path, Collections.singleton(answerSheet.getExamId() + "," + answerSheet.getStuId() + "," + answer.getId() + "," + complexity), java.nio.file.StandardOpenOption.APPEND);
                                    } catch (IOException e) {
                                        log.error("IOException:", e);
                                    }
                                } else {
                                    // 失败就写入-1
                                    try {
                                        log.info("Writing to file: {}", path);
                                        log.info("ExamId: {}, StudentId: {}, QuestionId: {}, Complexity: -1", answerSheet.getExamId(), answerSheet.getStuId(), answer.getId());
                                        // 追加不是覆盖
                                        Files.write(path, Collections.singleton(answerSheet.getExamId() + "," + answerSheet.getStuId() + "," + answer.getId() + ",-1"), java.nio.file.StandardOpenOption.APPEND);
                                    } catch (IOException e) {
                                        log.error("IOException:", e);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // 根据ExamSheet 和 AnswerSheet 计算得分
    public static int calculateSheetScore(ExamSheet examSheet, AnswerSheet answerSheet) {
        // 提交时间早于考试开始时间或者晚于考试结束时间，得分为0
        if (answerSheet.getSubmitTime() < examSheet.getStartTime() || answerSheet.getSubmitTime() > examSheet.getEndTime()) {
            return 0;
        }
        // 遍历每一个题目
        int score = 0;
        for (int i = 0; i < examSheet.getQuestions().size(); i++) {
            Question question = examSheet.getQuestions().get(i);
            Answer answer = answerSheet.getAnswers().get(i);
            int questionScore = question.calculateScore(answer);
            score += questionScore;
            log.info("QuestionId: {}, Score: {}", question.getId(), questionScore);
        }

        return score;
    }

    private static Map<Integer, ExamSheet> readAndParseExamSheets(String examsPath) {
        Map<Integer, ExamSheet> examSheetMap = new HashMap<>();
        File examsDir = new File(examsPath);
        File[] examFiles = examsDir.listFiles();
        if (examFiles != null) {
            for (File examFile : examFiles) {
                Parser parser = ParserFactory.createParser(examFile.getName());
                if (parser != null) {
                    ExamSheet examSheet = parser.parseExamSheet(examFile);
                    log.info("Adding exam sheet to map, id: {}", examSheet.getId());
                    examSheetMap.put(examSheet.getId(), examSheet);
                }
            }
        }
        return examSheetMap;
    }
}