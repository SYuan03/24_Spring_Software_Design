package org.example.strategies.multiplechoice;

import lombok.extern.slf4j.Slf4j;
import org.example.model.answer.Answer;
import org.example.model.answer.SingleAnswer;
import org.example.model.question.MultipleChoiceQuestion;
import org.example.util.MultipleChoiceStudentAnswerConverter;


import java.util.HashSet;
import java.util.List;

/**
 * @author SYuan03
 * @date 2024/3/26
 * 多选题的partial模式评分策略
 * 部分正确得分
 */
@Slf4j
public class PartialScoringPolicy implements ScoringPolicy {

    private final List<Integer> partialScores;

    public PartialScoringPolicy(List<Integer> partialScores) {
        this.partialScores = partialScores;
    }

    @Override
    public int calculateScore(List<Integer> answers, String content, int points) {
        int score = 0;
        // 先对content进行转换
        List<Integer> studentAnswers = MultipleChoiceStudentAnswerConverter.convert(content);

        log.debug("answers: {}", answers);
        log.debug("studentAnswers: {}", studentAnswers);
        log.debug("partialScores: {}", partialScores);

        if (studentAnswers.equals(answers)) {
            score = points;
        } else if (new HashSet<>(answers).containsAll(studentAnswers)) {
            // 对每个选项的分数求和
            for (Integer studentAnswer : studentAnswers) {
                score += partialScores.get(studentAnswer);
            }
        }
        return score;
    }
}
