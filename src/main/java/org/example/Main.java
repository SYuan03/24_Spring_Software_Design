package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.factory.parser.ParserFactory;
import org.example.model.answer.Answer;
import org.example.model.answer.AnswerSheet;
import org.example.model.exam.ExamSheet;
import org.example.model.question.ProgrammingQuestion;
import org.example.model.question.Question;
import org.example.parser.Parser;

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
    public static void main(String[] args) {
        String casePath = args[0];
        // 题目文件夹路径
        String examsPath = casePath + System.getProperty("file.separator") + "exams";
        // 答案文件夹路径
        String answersPath = casePath + System.getProperty("file.separator") + "answers";
        // 输出文件路径
        String output = args[1];


        // ----以下为实现代码----
        Path path = Paths.get(output);
        // 先写入一行表头
        try {
            Files.write(path, Collections.singleton("examId, stuId, score"), java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
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
                        log.info("ExamId: {}, StudentId: {}, Score: {}", answerSheet.getExamId(), answerSheet.getStuId(), score);
                        // 追加不是覆盖
                        Files.write(path, Collections.singleton(answerSheet.getExamId() + "," + answerSheet.getStuId() + "," + score), java.nio.file.StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        e.printStackTrace();
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
        // Tag: 题目id一定从1开始自增吗？
        // 考虑这一点，选择先存储下所有的answer的id和index的对应关系
        Map<Integer, Integer> idToIndex = new HashMap<>();
        for (int i = 0; i < examSheet.getQuestions().size(); i++) {
            Question question = examSheet.getQuestions().get(i);
            idToIndex.put(question.getId(), i);
        }
        log.info("idToIndex: {}", idToIndex);
        // 正常遍历每一个题目，计算得分
        int score = 0;
        for (Question question : examSheet.getQuestions()) {
            if (question instanceof ProgrammingQuestion) {
                // 编程题不回答也是满分
                score += question.getPoints();
                continue;
            }
            if (idToIndex.containsKey(question.getId())) {
                int index = idToIndex.get(question.getId());
                Answer answer = answerSheet.getAnswers().get(index);
                score += question.calculateScore(answer);
            }
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