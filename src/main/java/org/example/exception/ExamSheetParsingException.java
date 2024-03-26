package org.example.exception;

/**
 * @author SYuan03
 * @date 2024/3/26
 */
public class ExamSheetParsingException extends RuntimeException {
    public ExamSheetParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
