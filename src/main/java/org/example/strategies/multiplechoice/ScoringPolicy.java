package org.example.strategies.multiplechoice;

/**
 * @author admin
 * @date 2024/3/26
 */
import org.example.model.answer.Answer;

import java.util.List;

public interface ScoringPolicy {
    int calculateScore(List<Integer> answers, String content, int points);
}
