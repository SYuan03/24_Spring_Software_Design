package org.example.model.question;

import lombok.NoArgsConstructor;
import org.example.model.answer.Answer;
import org.example.model.answer.SingleAnswer;

import java.util.List;

/**
 * @author SYuan03
 * @date 2024/3/25
 */
@NoArgsConstructor
public class SingleChoiceQuestion extends Question {
    private List<String> options;
    private int answer;

    public SingleChoiceQuestion(int id, String description, int points, List<String> options, int answer) {
        super(id, description, points);
        this.options = options;
        this.answer = answer;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    @Override
    public int calculateScore(Answer answer) {
        // 先判断传入的是不是单一答案的答案
        if (!(answer instanceof SingleAnswer)) {
            throw new IllegalArgumentException("Answer type is incorrect");
        }
        SingleAnswer singleAnswer = (SingleAnswer) answer;
        // answer是index，比如0->A, 1->B
        // 而content是比如"A"
        // 将content从string转成char再转成int
        int studentAnswer = singleAnswer.getContent().charAt(0) - 'A';
        return studentAnswer == this.answer ? this.points : 0;
    }
}
