package org.example.parser;

import org.example.model.answer.AnswerSheet;
import org.example.model.exam.ExamSheet;

import java.io.File;

/**
 * @author admin
 * @date 2024/3/26
 * 为了保持灵活
 * 有的Parser可以解析ExamSheet，有的可以解析AnswerSheet
 */
public interface Parser {
    ExamSheet parseExamSheet(File file);

    AnswerSheet parseAnswerSheet(File file);

}
