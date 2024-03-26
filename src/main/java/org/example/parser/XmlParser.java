package org.example.parser;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.deserializer.QuestionXmlDeserializer;
import org.example.exception.ExamSheetParsingException;
import org.example.model.answer.AnswerSheet;
import org.example.model.exam.ExamSheet;
import org.example.model.question.Question;

import java.io.File;

/**
 * @author SYuan03
 * @date 2024/3/26
 */
@Slf4j
public class XmlParser implements Parser {
    @Override
    public ExamSheet parseExamSheet(File file) {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new SimpleModule().addDeserializer(Question.class, new QuestionXmlDeserializer()));
        try {
            return xmlMapper.readValue(file, ExamSheet.class);
        } catch (Exception e) {
            log.error("Failed to parse exam sheet from file: {}", file.getAbsolutePath());
            throw new ExamSheetParsingException("Failed to parse exam sheet from file: " + file.getAbsolutePath(), e);
        }
    }

    @Override
    public AnswerSheet parseAnswerSheet(File file) {
        throw new UnsupportedOperationException("XmlParser does not support parsing answer sheet");
    }
}
