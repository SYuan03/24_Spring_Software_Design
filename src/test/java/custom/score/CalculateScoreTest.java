package custom.score;
import org.example.Main;

import org.example.model.answer.AnswerSheet;
import org.example.model.exam.ExamSheet;
import org.example.parser.JsonParser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author SYuan03
 * @date 2024/3/26
 */
class CalculateScoreTest {
    @Test
    void testCalculateAnswerSheetScore() {
        JsonParser jsonParser = new JsonParser();

        URL resource = getClass().getClassLoader().getResource("cases/exams/1.json");
        assertNotNull(resource, "测试文件未找到");
        String fileName = resource.getFile();
        File file = new File(fileName);

        ExamSheet examSheet = jsonParser.parseExamSheet(file);

        URL resource2 = getClass().getClassLoader().getResource("cases/answers/1-1.json");
        assertNotNull(resource2, "测试文件未找到");
        String fileName2 = resource2.getFile();
        File file2 = new File(fileName2);

        AnswerSheet answerSheet = jsonParser.parseAnswerSheet(file2);

        int score = Main.calculateSheetScore(examSheet, answerSheet);

        assert score == 100;

    }
}

