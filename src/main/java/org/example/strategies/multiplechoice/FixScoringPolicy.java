package org.example.strategies.multiplechoice;

import org.example.model.answer.Answer;
import org.example.model.answer.SingleAnswer;
import org.example.model.question.MultipleChoiceQuestion;
import org.example.util.MultipleChoiceStudentAnswerConverter;

import java.util.HashSet;
import java.util.List;

/**
 * @author SYuan03
 * @date 2024/3/26
 * 多选题的fix模式评分策略
 */
public class FixScoringPolicy implements ScoringPolicy {
    private final int fixScore;

    public FixScoringPolicy(int fixScore) {
        this.fixScore = fixScore;
    }

    @Override
    public int calculateScore(List<Integer> answers, String content, int points) {
        int score = 0;
        // 先对content进行转换
        List<Integer> studentAnswers = MultipleChoiceStudentAnswerConverter.convert(content);

        if (studentAnswers.equals(answers)) {
            score = points;
        } else if (new HashSet<>(answers).containsAll(studentAnswers)) {
            score = fixScore;
        }
        return score;
    }
}
