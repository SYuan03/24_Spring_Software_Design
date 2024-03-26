package org.example.factory.question;

import org.example.model.dto.QuestionDTO;
import org.example.model.question.ProgrammingQuestion;
import org.example.model.question.Question;

/**
 * @author SYuan03
 * @date 2024/3/26
 */
public class ProgrammingQuestionFactory implements QuestionFactory {
    @Override
    public Question createQuestion(QuestionDTO questionDTO) {
        return ProgrammingQuestion.builder()
                .id(questionDTO.getId())
                .description(questionDTO.getQuestion())
                .points(questionDTO.getPoints())
                .samples(questionDTO.getSamples())
                .timeLimit(questionDTO.getTimeLimit())
                .build();
    }
}
