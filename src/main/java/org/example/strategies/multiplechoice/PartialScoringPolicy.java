package org.example.strategies.multiplechoice;

import lombok.extern.slf4j.Slf4j;
import org.example.util.MultipleChoiceStudentAnswerConverter;


import java.util.HashSet;
import java.util.List;

/**
 * @author SYuan03
 * @date 2024/3/26
 * 多选题的partial模式评分策略
 * 部分正确得分
 */
@Slf4j
public class PartialScoringPolicy implements ScoringPolicy {

    private final List<Integer> partialScores;

    public PartialScoringPolicy(List<Integer> partialScores) {
        this.partialScores = partialScores;
    }

    @Override
    public int calculateScore(List<Integer> answers, String content, int points) {
        int score = 0;
        // 先对content进行转换
        List<Integer> studentAnswers = MultipleChoiceStudentAnswerConverter.convert(content);

//        for (Object obj : partialScores) {
//            log.debug("Element class of partialScores: {}", obj.getClass().getName());
//        }

        if (studentAnswers.equals(answers)) {
            log.info("PartialScoringPolicy: studentAnswers equals answers");
            score = points;
        } else if (new HashSet<>(answers).containsAll(studentAnswers)) {
            log.info("PartialScoringPolicy: studentAnswers is a subset of answers");
            for (int studentAnswer : studentAnswers) {
                // 找到studentAnswer在answers中的位置
                int index = answers.indexOf(studentAnswer);
                // 根据index找到对应的分数
                score += partialScores.get(index);
            }
        }
        log.info("PartialScoringPolicy: score: {}", score);
        return score;
    }
}
