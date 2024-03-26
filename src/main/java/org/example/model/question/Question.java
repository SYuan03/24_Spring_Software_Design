package org.example.model.question;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.deserializer.QuestionJsonDeserializer;
import org.example.model.answer.Answer;

/**
 * @author SYuan03
 * @date 2024/3/25
 */
@SuperBuilder
public abstract class Question {
    protected int id;
    protected String description;
    protected int points;

    // explain: 使用protected修饰符，使得子类可以访问
    // 抽象类没法实例化，所以构造方法只能被子类调用
    // 只是为了少写代码，不用在每个子类中写相同的赋值操作
//    protected Question(int id, String description, int points) {
//        this.id = id;
//        this.description = description;
//        this.points = points;
//    }

    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    // 计算得分
    public abstract int calculateScore(Answer answer);
}
