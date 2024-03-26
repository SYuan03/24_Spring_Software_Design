package org.example.factory.answer;

import lombok.extern.slf4j.Slf4j;
import org.example.model.answer.Answer;
import org.example.model.answer.SingleAnswer;
import org.example.model.dto.AnswerDTO;

/**
 * @author SYuan03
 * @date 2024/3/26
 */
@Slf4j
public class SingleAnswerFactory implements AnswerFactory {
    @Override
    public Answer createAnswer(AnswerDTO answerDTO) {
        return SingleAnswer.builder()
                .id(answerDTO.getId())
                .content((String) answerDTO.getAnswer())
                .build();
    }
}
