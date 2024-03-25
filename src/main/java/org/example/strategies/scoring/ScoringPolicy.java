package org.example.strategies.scoring;

/**
 * @author admin
 * @date 2024/3/26
 */
import org.example.model.question.Question;
import org.example.model.answer.Answer;

public interface ScoringPolicy {
    int calculateScore(Question question, Answer answer);
}
