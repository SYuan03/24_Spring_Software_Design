package org.example.strategies.multiplechoice;

/**
 * @author admin
 * @date 2024/3/26
 */
import org.example.model.question.MultipleChoiceQuestion;
import org.example.model.question.Question;
import org.example.model.answer.Answer;

public interface ScoringPolicy {
    int calculateScore(MultipleChoiceQuestion multipleChoiceQuestion, Answer answer);
}
