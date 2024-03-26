package org.example.model.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.common.Sample;

import java.util.List;

/**
 * @author SYuan03
 * @date 2024/3/26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDTO {
    private int id;
    private int type;
    private String question;

    @JacksonXmlElementWrapper(localName = "options")
    @JacksonXmlProperty(localName = "option")
    private List<String> options; // 适用于单选和多选题

    private Object answer; // 这里使用 Object 类型来兼容单选题的 Integer 类型和多选题的 List<Integer> 类型
    private Object answers; // Tag: xml中多选题好像是answers，不是answer

    private int points;
    private String scoreMode; // 特有于多选题
    private Integer fixScore; // 特有于多选题，使用包装类以便于处理null值
    // Tag:
    private List<Integer> partialScores; // WHYY: 特有于多选题，为什么这个不用加注解也行啊！！！
    private List<Sample> samples; // 特有于编程题
    private Integer timeLimit; // 特有于编程题，使用包装类以便于处理null值
}