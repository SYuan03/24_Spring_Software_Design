package org.example.model.exam;

import lombok.Data;
import org.example.model.question.Question;

import java.security.Timestamp;
import java.util.List;

/**
 * @author SYuan03
 * @date 2024/3/26
 */
@Data
public class ExamSheet {
    private int id;
    private String title;
    private long startTime;
    private long endTime;
    private List<Question> questions;
}
