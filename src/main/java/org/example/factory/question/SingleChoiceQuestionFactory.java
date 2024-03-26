package org.example.factory.question;

import org.example.model.dto.QuestionDTO;
import org.example.model.question.Question;
import org.example.model.question.SingleChoiceQuestion;

/**
 * @author SYuan03
 * @date 2024/3/26
 */
public class SingleChoiceQuestionFactory implements QuestionFactory {
    @Override
    public Question createQuestion(QuestionDTO questionDTO) {
        // 从questionDTO中提取信息，创建SingleChoiceQuestion对象
        return SingleChoiceQuestion.builder()
                .id(questionDTO.getId())
                .description(questionDTO.getQuestion())
                .points(questionDTO.getPoints())
                .options(questionDTO.getOptions())
                .answer((Integer) questionDTO.getAnswer())
                .build();
    }
}
