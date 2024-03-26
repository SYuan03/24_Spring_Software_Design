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
        SingleChoiceQuestion singleChoiceQuestion = SingleChoiceQuestion.builder()
                .id(questionDTO.getId())
                .description(questionDTO.getQuestion())
                .points(questionDTO.getPoints())
                .options(questionDTO.getOptions())
                .build();
        // 对answer进行处理
        // questionDTO.getAnswer()返回的是Object类型，需要根据实际情况进行类型转换
        if (questionDTO.getAnswer() instanceof Integer) {
            singleChoiceQuestion.setAnswer((Integer) questionDTO.getAnswer());
        } else if (questionDTO.getAnswer() instanceof String) {
            // Tag: 似乎xml的时候就会读到String类型
            // 如果answer是String类型，说明是字母，需要转换成数字
            String answer = (String) questionDTO.getAnswer();
            singleChoiceQuestion.setAnswer(answer.charAt(0) - 'A');
        } else {
            throw new IllegalArgumentException("Answer type is incorrect");
        }

        return singleChoiceQuestion;
    }
}
