package org.example.model;

import java.util.List;

/**
 * @author SYuan03
 * @date 2024/3/25
 */
public class MultipleChoiceQuestion extends Question {
    private List<String> options;
    private List<Integer> answers;  // 多选题答案是一个index列表
    private String scoreMode;       // 评分模式，如"fix", "nothing", "partial"
    private int fixScore;           // 固定分数，仅在特定评分模式下使用

    public MultipleChoiceQuestion(int id, String description, int points, List<String> options, List<Integer> answers, String scoreMode, int fixScore) {
        super(id, description, points);
        this.options = options;
        this.answers = answers;
        this.scoreMode = scoreMode;
        this.fixScore = fixScore;
    }

    // getters and setters
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

    public String getScoreMode() {
        return scoreMode;
    }

    public void setScoreMode(String scoreMode) {
        this.scoreMode = scoreMode;
    }

    public int getFixScore() {
        return fixScore;
    }

    public void setFixScore(int fixScore) {
        this.fixScore = fixScore;
    }
}
