package org.example.model.question;

import lombok.experimental.SuperBuilder;
import org.example.model.answer.Answer;
import org.example.model.common.Sample;

import java.util.List;

/**
 * @author SYuan03
 * @date 2024/3/26
 */
@SuperBuilder
public class ProgrammingQuestion extends Question {
    private List<Sample> samples;
    private int timeLimit;

//    public ProgrammingQuestion(int id, String description, int points, List<Sample> samples, int timeLimit) {
//        super(id, description, points);
//        this.samples = samples;
//        this.timeLimit = timeLimit;
//    }

    // getters and setters
    public List<Sample> getSamples() {
        return samples;
    }

    public void setSamples(List<Sample> samples) {
        this.samples = samples;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    /**
     * @param answer
     * @return
     * 迭代一对于编程题的实现
     * ⽆论⽤户是否回答了编程题，以及回答是否正确，你都应该将编程题视为回答正确
     */
    @Override
    public int calculateScore(Answer answer) {
        return this.points;
    }

}
