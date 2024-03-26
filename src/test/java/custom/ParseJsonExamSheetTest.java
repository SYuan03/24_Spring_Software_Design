package custom;

import org.example.model.exam.ExamSheet;
import org.example.model.question.Question;
import org.example.parser.JsonParser;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author SYuan03
 * @date 2024/3/26
 * 测试parseExamSheet
 */
public class ParseJsonExamSheetTest {

    @Test
    void testParseJsonExamSheet() {
        // 使用ClassLoader安全地获取resources目录下的测试文件路径
        URL resource = getClass().getClassLoader().getResource("cases/exams/1.json");
        assertNotNull(resource, "测试文件未找到");

        String fileName = resource.getFile();

        ExamSheet examSheet = JsonParser.parseExamSheet(fileName);

        // 断言examSheet不为null
        assertNotNull(examSheet, "解析后的ExamSheet对象为null");

        // 打印第二个question的所有字段
        List<Question> questions = examSheet.getQuestions();
        System.out.println(questions);
    }

}
