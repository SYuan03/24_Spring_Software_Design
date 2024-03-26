package org.example.util;

import java.util.List;

import java.util.ArrayList;

/**
 * @author SYuan03
 * @date 2024/3/26
 * 用于将"AB"这种转成List<Integer>的{0, 1}
 */
public class MultipleChoiceStudentAnswerConverter {
    public static List<Integer> convert(String studentAnswer) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < studentAnswer.length(); i++) {
            result.add(studentAnswer.charAt(i) - 'A');
        }
        return result;
    }
}
