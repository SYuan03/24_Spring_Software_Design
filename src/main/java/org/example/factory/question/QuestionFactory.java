package org.example.factory.question;

import org.example.model.dto.QuestionDTO;
import org.example.model.question.Question;

/**
 * @author SYuan03
 * @date 2024/3/26
 */
public interface QuestionFactory {
    Question createQuestion(QuestionDTO questionDTO);

    static QuestionFactory getFactory(int questionType) {
        switch (questionType) {
            case 1:
                return new SingleChoiceQuestionFactory();
            case 2:
                return new MultipleChoiceQuestionFactory();
            case 3:
                return new ProgrammingQuestionFactory();
            default:
                throw new IllegalArgumentException("Invalid question type: " + questionType);
        }
    }
}

