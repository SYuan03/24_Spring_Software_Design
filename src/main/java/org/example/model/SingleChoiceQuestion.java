package org.example.model;

import java.util.List;

/**
 * @author SYuan03
 * @date 2024/3/25
 */
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
}
