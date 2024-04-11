package custom.parser;

import org.example.model.exam.ExamSheet;
import org.example.model.question.MultipleChoiceQuestion;
import org.example.model.question.Question;
import org.example.parser.JsonParser;
import org.example.strategies.multiplechoice.FixScoringPolicy;
import org.example.strategies.multiplechoice.ScoringPolicy;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author SYuan03
 * @date 2024/3/26
 * 测试parseExamSheet
 */
class ParseJsonExamSheetTest {

    @Test
    void testParseJsonExamSheet1() {
        // 使用ClassLoader安全地获取resources目录下的测试文件路径
        URL resource = getClass().getClassLoader().getResource("cases-iter1/exams/1.json");
        assertNotNull(resource, "测试文件未找到");
        File file = new File(resource.getFile());

        JsonParser jsonParser = new JsonParser();
        ExamSheet examSheet = jsonParser.parseExamSheet(file);

        // 断言examSheet不为null
        assertNotNull(examSheet, "解析后的ExamSheet对象为null");

        // 打印第二个question的所有字段
        List<Question> questions = examSheet.getQuestions();
        System.out.println(questions);

        MultipleChoiceQuestion secondQuestion = (MultipleChoiceQuestion) questions.get(1); // 获取第二个问题
        ScoringPolicy scoringPolicy = secondQuestion.getScoringPolicy(); // 获取评分策略

        // 检查评分策略是否是 FixScoringPolicy
        boolean isFixScoringPolicy = scoringPolicy instanceof FixScoringPolicy;

        // 断言评分策略是 FixScoringPolicy
        assert isFixScoringPolicy;
    }

}
