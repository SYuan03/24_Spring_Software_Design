package org.example.parser;

import org.example.model.answer.AnswerSheet;
import org.example.model.exam.ExamSheet;

import java.io.File;

/**
 * @author SYuan03
 * @date 2024/3/26
 */
public class XmlParser implements Parser {
    @Override
    public ExamSheet parseExamSheet(File file) {
        return null;
    }

    @Override
    public AnswerSheet parseAnswerSheet(File file) {
        throw new UnsupportedOperationException("XmlParser does not support parsing answer sheet");
    }
}
