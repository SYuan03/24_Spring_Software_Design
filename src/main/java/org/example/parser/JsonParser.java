package org.example.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.ExamSheetParsingException;
import org.example.model.answer.AnswerSheet;
import org.example.model.exam.ExamSheet;

import java.io.File;

/**
 * @author SYuan03
 * @date 2024/3/26
 */
@Slf4j
public class JsonParser implements Parser {

    public ExamSheet parseExamSheet(File file) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(file, ExamSheet.class);
        } catch (Exception e) {
            log.error("Failed to parse exam sheet from file: {}", file.getAbsolutePath());
            throw new ExamSheetParsingException("Failed to parse exam sheet from file: " + file.getAbsolutePath(), e);
        }
    }

    public AnswerSheet parseAnswerSheet(File file) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(file, AnswerSheet.class);
        } catch (Exception e) {
            log.error("Failed to parse answer sheet from file: {}", file.getAbsolutePath());
            throw new ExamSheetParsingException("Failed to parse answer sheet from file: " + file.getAbsolutePath(), e);
        }
    }
}
