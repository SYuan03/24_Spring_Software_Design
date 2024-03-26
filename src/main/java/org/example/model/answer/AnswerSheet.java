package org.example.model.answer;

import lombok.Data;

import java.util.List;

/**
 * @author SYuan03
 * @date 2024/3/26
 */
@Data
public class AnswerSheet {
    private int examId;
    private int studentId;
    private long submitTime;
    private List<Answer> answers;
}
