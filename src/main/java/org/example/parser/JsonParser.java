package org.example.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.exception.ExamSheetParsingException;
import org.example.model.answer.AnswerSheet;
import org.example.model.exam.ExamSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author SYuan03
 * @date 2024/3/26
 */
public class JsonParser implements Parser {
    private final Logger logger = LoggerFactory.getLogger(JsonParser.class);

    public ExamSheet parseExamSheet(File file) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(file, ExamSheet.class);
        } catch (Exception e) {
            logger.error("Failed to parse exam sheet from file: {}", file.getAbsolutePath());
            throw new ExamSheetParsingException("Failed to parse exam sheet from file: " + file.getAbsolutePath(), e);
        }
    }

    public AnswerSheet parseAnswerSheet(File file) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(file, AnswerSheet.class);
        } catch (Exception e) {
            logger.error("Failed to parse answer sheet from file: {}", file.getAbsolutePath());
            throw new ExamSheetParsingException("Failed to parse answer sheet from file: " + file.getAbsolutePath(), e);
        }
    }
}
