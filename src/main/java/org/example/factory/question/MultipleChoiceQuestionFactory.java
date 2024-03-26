package org.example.factory.question;

import org.example.model.dto.QuestionDTO;
import org.example.model.question.MultipleChoiceQuestion;
import org.example.model.question.Question;
import org.example.strategies.multiplechoice.FixScoringPolicy;
import org.example.strategies.multiplechoice.NothingScoringPolicy;
import org.example.strategies.multiplechoice.PartialScoringPolicy;
import org.example.strategies.multiplechoice.ScoringPolicy;

import java.util.List;

/**
 * @author SYuan03
 * @date 2024/3/26
 * 这个类会用上多选的策略模式设定
 */
public class MultipleChoiceQuestionFactory implements QuestionFactory {
    @Override
    public Question createQuestion(QuestionDTO questionDTO) {
        // 根据scoreMode字段的值，选择合适的评分策略
        ScoringPolicy scoringPolicy = chooseScoringPolicy(questionDTO);

        // 使用构建者模式创建Question对象，避免重复代码
        return MultipleChoiceQuestion.builder()
                .id(questionDTO.getId())
                .description(questionDTO.getQuestion())
                .points(questionDTO.getPoints())
                .options(questionDTO.getOptions())
                .answers((List<Integer>) questionDTO.getAnswer())
                .scoringPolicy(scoringPolicy)
                .build();
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
