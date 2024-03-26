package org.example.factory.answer;

import org.example.model.answer.Answer;
import org.example.model.answer.SingleAnswer;
import org.example.model.dto.AnswerDTO;

/**
 * @author SYuan03
 * @date 2024/3/26
 */
public class SingleAnswerFactory implements AnswerFactory {
    @Override
    public Answer createAnswer(AnswerDTO answerDTO) {
        return new SingleAnswer((String) answerDTO.getAnswer());
    }
}
