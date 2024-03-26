package org.example.model.question;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.model.answer.Answer;
import org.example.model.answer.SingleAnswer;
import org.example.strategies.multiplechoice.ScoringPolicy;

import java.util.List;

/**
 * @author SYuan03
 * @date 2024/3/25
 */
@SuperBuilder
public class MultipleChoiceQuestion extends Question {
    private List<String> options;
    private List<Integer> answers;          // 多选题答案是一个index列表
    // 将这些移到ScoringPolicy接口的实现类中
//    private int fixScore;                   // 固定分数，"fix"评分模式下使用
//    private List<Integer> partialScores;    // 部分得分，"partial"评分模式下使用

    // 策略模式
    private ScoringPolicy scoringPolicy;

//    public MultipleChoiceQuestion(int id, String description, int points, List<String> options, List<Integer> answers) {
//        super(id, description, points);
//        this.options = options;
//        this.answers = answers;
//    }

    // getters and setters
    public ScoringPolicy getScoringPolicy() {
        return scoringPolicy;
    }

    public void setScoringPolicy(ScoringPolicy scoringPolicy) {
        this.scoringPolicy = scoringPolicy;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public List<Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Integer> answers) {
        this.answers = answers;
    }


    @Override
    public int calculateScore(Answer answer) {
        if (!(answer instanceof SingleAnswer)) {
            throw new IllegalArgumentException("Answer type is incorrect");
        }
        SingleAnswer singleAnswer = (SingleAnswer) answer;
        return scoringPolicy.calculateScore(answers, singleAnswer.getContent(), points);
    }
}
