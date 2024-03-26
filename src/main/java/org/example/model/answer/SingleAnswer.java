package org.example.model.answer;

import lombok.NoArgsConstructor;

/**
 * @author SYuan03
 * @date 2024/3/26
 * 指除了一个answer字段(content)之外没有别的了
 */
@NoArgsConstructor
public class SingleAnswer extends Answer {
    private String content;

    public SingleAnswer(int id, String content) {
        super(id);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



}
