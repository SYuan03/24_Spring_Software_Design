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
 * 多选题的partial模式评分策略
 * 部分正确得分
 */
public class PartialScoringPolicy implements ScoringPolicy {

    @Override
    public int calculateScore(MultipleChoiceQuestion multipleChoiceQuestion, Answer answer) {
        // 先判断答案是否是SingleAnswer类型
        if (!(answer instanceof SingleAnswer)) {
            throw new IllegalArgumentException("Answer type is incorrect in PartialScoringPolicy");
        }
        SingleAnswer singleAnswer = (SingleAnswer) answer;
        // 使用partial模式评分策略
        // 部分正确得分
        int score = 0;
        // 得到的是index，比如0->A, 1->B
        List<Integer> correctAnswers = multipleChoiceQuestion.getAnswers();
        // content是比如"ABC"转成{0, 1, 2}
        List<Integer> studentAnswers = MultipleChoiceStudentAnswerConverter.convert(singleAnswer.getContent());
        // 获取每个选项选对的分数
        List<Integer> partialScores = multipleChoiceQuestion.getPartialScores();

        if (studentAnswers.equals(correctAnswers)) {
            score = multipleChoiceQuestion.getPoints();
        } else if (new HashSet<>(correctAnswers).containsAll(studentAnswers)) {
            // 对每个选项的分数求和
            for (Integer studentAnswer : studentAnswers) {
                score += partialScores.get(studentAnswer);
            }
        }
        return score;

    }
}
