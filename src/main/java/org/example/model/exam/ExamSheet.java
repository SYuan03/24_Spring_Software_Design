package org.example.model.exam;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.question.Question;

import java.util.List;

/**
 * @author SYuan03
 * @date 2024/3/26
 */
@Data
@NoArgsConstructor
public class ExamSheet {
    private int id;
    private String title;
    private long startTime;
    private long endTime;
    private List<Question> questions;
}
