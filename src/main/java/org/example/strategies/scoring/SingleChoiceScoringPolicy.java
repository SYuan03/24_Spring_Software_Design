package org.example.strategies.scoring;

import org.example.model.answer.Answer;
import org.example.model.answer.SingleChoiceAnswer;
import org.example.model.question.Question;
import org.example.model.question.SingleChoiceQuestion;

/**
 * @author SYuan03
 * @date 2024/3/26
 */
public class SingleChoiceScoringPolicy implements ScoringPolicy {
    @Override
    public int calculateScore(Question question, Answer answer) {
        if (!(question instanceof SingleChoiceQuestion) || !(answer instanceof SingleChoiceAnswer)) {
            throw new IllegalArgumentException("Invalid question or answer type.");
        }
        SingleChoiceQuestion singleChoiceQuestion = (SingleChoiceQuestion) question;
        SingleChoiceAnswer singleChoiceAnswer = (SingleChoiceAnswer) answer;
        if (singleChoiceAnswer.getSelectedOption() == singleChoiceQuestion.getAnswer()) {
            return singleChoiceQuestion.getPoints();
        } else {
            return 0;
        }
    }
}
