package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.factory.parser.ParserFactory;
import org.example.model.answer.AnswerSheet;
import org.example.model.exam.ExamSheet;
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
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        String casePath = args[0];
        // 题目文件夹路径
        String examsPath = casePath + System.getProperty("file.separator") + "exams";
        // 答案文件夹路径
        String answersPath = casePath + System.getProperty("file.separator") + "answers";
        // 输出文件路径
        String output = args[1];

        // ----以下为实现代码----
        // 1. 先把所有的ExamSheet读出来存在map里
        // Tag: 其实可以用文件名对应关系，但没说一定是一一对应的，所以还是用map比较好
        Map<Integer, ExamSheet> examSheetMap = readAndParseExamSheets(examsPath);
        // 2. 对answersPath下的所有文件(目前只有json)进行读取并计算
        File answersDir = new File(answersPath);
        File[] answerFiles = answersDir.listFiles();
        if (answerFiles != null) {
            for (File answerFile : answerFiles) {
                Parser parser = ParserFactory.createParser(answerFile.getName());
                if (parser != null) {
                    AnswerSheet answerSheet = parser.parseAnswerSheet(answerFile);
                    // 3. 计算得分
                    int score = calculateSheetScore(examSheetMap.get(answerSheet.getExamId()), answerSheet);
                    // 4. 输出到文件
                    try {
                        Path path = Paths.get(output);
                        logger.info("Writing to file: {}", path);
                        logger.info("ExamId: {}, StudentId: {}, Score: {}", answerSheet.getExamId(), answerSheet.getStuId(), score);
                        Files.write(path, Collections.singletonList(answerSheet.getExamId() + "," + answerSheet.getStuId() + "," + score));
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
        // 正常遍历每一题
        int score = 0;
        for (int i = 0; i < examSheet.getQuestions().size(); i++) {
            // BUG：可能有没作答的题目，这里需要判断一下
            int temp = examSheet.getQuestions().get(i).calculateScore(answerSheet.getAnswers().get(i));
            log.info("QuestionId: {}, Score: {}", examSheet.getQuestions().get(i).getId(), temp);
            score += temp;
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
                    examSheetMap.put(examSheet.getId(), examSheet);
                }
            }
        }
        return examSheetMap;
    }
}