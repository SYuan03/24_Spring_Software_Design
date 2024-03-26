package org.example.model.answer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.deserializer.AnswerJsonDeserializer;

/**
 * @author SYuan03
 * @date 2024/3/26
 * Answer类指的是学生回答的结果，是一个抽象类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonDeserialize(using = AnswerJsonDeserializer.class)
public abstract class Answer {
    protected int id;
}
