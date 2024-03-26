package org.example.factory;

import org.example.model.question.MultipleChoiceQuestion;
import org.example.model.question.ProgrammingQuestion;
import org.example.model.question.Question;
import org.example.model.question.SingleChoiceQuestion;

/**
 * @author SYuan03
 * @date 2024/3/26
 */
public class QuestionFactory {
    public static Question createQuestion(Object... parameters) {
        int id = (Integer) parameters[0];
        int type = (Integer) parameters[1];
        String question = (String) parameters[2];

    }
}
