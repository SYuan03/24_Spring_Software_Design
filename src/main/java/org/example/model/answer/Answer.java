package org.example.model.answer;

/**
 * @author SYuan03
 * @date 2024/3/26
 * Answer类指的是学生回答的结果，是一个抽象类
 */
public abstract class Answer {
    protected int id;

    protected Answer(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
