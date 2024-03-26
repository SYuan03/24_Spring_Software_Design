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
    private static final Logger logger = LoggerFactory.getLogger(JsonParser.class);

    public static ExamSheet parseExamSheet(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(filePath), ExamSheet.class);
        } catch (Exception e) {
            logger.error("Failed to parse exam sheet from file: {}", filePath);
            throw new ExamSheetParsingException("Failed to parse exam sheet from file: " + filePath, e);
        }
    }

}
