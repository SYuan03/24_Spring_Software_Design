package org.example.factory.answer;

import org.example.model.answer.Answer;
import org.example.model.dto.AnswerDTO;

/**
 * @author admin
 * @date 2024/3/26
 */
public interface AnswerFactory {
    Answer createAnswer(AnswerDTO answerDTO);

    static AnswerFactory getFactory() {
        // 目前只有一种答案类型SingleAnswer
        return new SingleContentAnswerFactory();
    }
}
