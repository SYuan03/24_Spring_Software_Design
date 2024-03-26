package custom.score;
import org.example.Main;

import org.example.model.answer.AnswerSheet;
import org.example.model.exam.ExamSheet;
import org.example.parser.JsonParser;
import org.example.parser.XmlParser;
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
    void testCalculateAnswerSheetScoreJsonExam() {
        JsonParser jsonParser = new JsonParser();

        URL resource = getClass().getClassLoader().getResource("cases/exams/3.json");
        assertNotNull(resource, "测试文件未找到");
        String fileName = resource.getFile();
        File file = new File(fileName);

        ExamSheet examSheet = jsonParser.parseExamSheet(file);

        URL resource2 = getClass().getClassLoader().getResource("cases/answers/3-2.json");
        assertNotNull(resource2, "测试文件未找到");
        String fileName2 = resource2.getFile();
        File file2 = new File(fileName2);

        AnswerSheet answerSheet = jsonParser.parseAnswerSheet(file2);

        int score = Main.calculateSheetScore(examSheet, answerSheet);

        assert score == 90;
    }

    @Test
    void testCalculateAnswerSheetScoreXmlExam() {
        XmlParser xmlParser = new XmlParser();

        URL resource = getClass().getClassLoader().getResource("cases/exams/2.xml");
        assertNotNull(resource, "测试文件未找到");
        String fileName = resource.getFile();
        File file = new File(fileName);

        ExamSheet examSheet = xmlParser.parseExamSheet(file);

        URL resource2 = getClass().getClassLoader().getResource("cases/answers/2-1.json");
        assertNotNull(resource2, "测试文件未找到");
        String fileName2 = resource2.getFile();
        File file2 = new File(fileName2);

        JsonParser jsonParser = new JsonParser();

        AnswerSheet answerSheet = jsonParser.parseAnswerSheet(file2);

        int score = Main.calculateSheetScore(examSheet, answerSheet);

        assert score == 95;
    }
}

