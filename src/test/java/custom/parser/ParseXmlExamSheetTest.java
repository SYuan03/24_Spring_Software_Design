package custom.parser;

import org.example.model.exam.ExamSheet;
import org.example.model.question.MultipleChoiceQuestion;
import org.example.model.question.Question;
import org.example.parser.XmlParser;
import org.example.strategies.multiplechoice.PartialScoringPolicy;
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
class ParseXmlExamSheetTest {

    @Test
    void testParseXmlExamSheet1() {
        URL resource = getClass().getClassLoader().getResource("cases-iter1/exams/2.xml");
        assertNotNull(resource, "测试文件未找到");
        File file = new File(resource.getFile());

        XmlParser xmlParser = new XmlParser();
        ExamSheet examSheet = xmlParser.parseExamSheet(file);

        assertNotNull(examSheet, "解析后的ExamSheet对象为null");

        List<Question> questions = examSheet.getQuestions();

        MultipleChoiceQuestion secondQuestion = (MultipleChoiceQuestion) questions.get(1); // 获取第二个问题
        ScoringPolicy scoringPolicy = secondQuestion.getScoringPolicy(); // 获取评分策略

        boolean isPartialScoringPolicy = scoringPolicy instanceof PartialScoringPolicy;

        assert isPartialScoringPolicy;
    }
}
