package org.example.factory.question;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.model.dto.QuestionDTO;
import org.example.model.question.MultipleChoiceQuestion;
import org.example.model.question.Question;
import org.example.strategies.multiplechoice.FixScoringPolicy;
import org.example.strategies.multiplechoice.NothingScoringPolicy;
import org.example.strategies.multiplechoice.PartialScoringPolicy;
import org.example.strategies.multiplechoice.ScoringPolicy;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author SYuan03
 * @date 2024/3/26
 * 这个类会用上多选的策略模式设定
 */
@Slf4j
public class MultipleChoiceQuestionFactory implements QuestionFactory {
    @Override
    public Question createQuestion(QuestionDTO questionDTO) {
        // 根据scoreMode字段的值，选择合适的评分策略
        ScoringPolicy scoringPolicy = chooseScoringPolicy(questionDTO);

        MultipleChoiceQuestion multipleChoiceQuestion = MultipleChoiceQuestion.builder()
                .id(questionDTO.getId())
                .description(questionDTO.getQuestion())
                .points(questionDTO.getPoints())
                .options(questionDTO.getOptions())
                .scoringPolicy(scoringPolicy)
                .build();

        log.debug("answer: {}", questionDTO.getAnswer());
        log.debug("answers: {}", questionDTO.getAnswers());
        // 打印各自的类型
        log.debug("answers type: {}", questionDTO.getAnswers().getClass());

        // Tag: 处理answer和answers
        // 主要是因为xml中多选题的答案是answers，不是answer
        if (questionDTO.getAnswers() != null) {
            // 将{answer=[0, 1, 2]}， LinkedHashMap类型转换为[0, 1, 2]
            LinkedHashMap<String, Object> answersMap = (LinkedHashMap<String, Object>) questionDTO.getAnswers();
            List<Integer> answerList = (List<Integer>) answersMap.get("answer");
            multipleChoiceQuestion.setAnswers(answerList);
        } else {
            multipleChoiceQuestion.setAnswers((List<Integer>) questionDTO.getAnswer());
        }

        return multipleChoiceQuestion;
    }

    private ScoringPolicy chooseScoringPolicy(QuestionDTO questionDTO) {
        switch (questionDTO.getScoreMode()) {
            case "fix":
                return new FixScoringPolicy(questionDTO.getFixScore());
            case "partial":
                return new PartialScoringPolicy(questionDTO.getPartialScores());
            case "nothing":
                return new NothingScoringPolicy();
            default:
                throw new IllegalArgumentException("Unknown score mode: " + questionDTO.getScoreMode());
        }
    }
}
