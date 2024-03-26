package org.example.factory;

import org.example.model.question.Question;
import org.example.model.question.SingleChoiceQuestion;
import org.example.model.dto.QuestionDTO;

/**
 * @author SYuan03
 * @date 2024/3/26
 */
public class QuestionFactory {
    public static Question createQuestion(QuestionDTO questionDTO) {
        // 先builder返回一个SingleChoiceQuestion对象
        return SingleChoiceQuestion.builder()
                .id(questionDTO.getId())
                .description(questionDTO.getQuestion())
                .options(questionDTO.getOptions())
                .answer(5)
                .build();
    }
}
