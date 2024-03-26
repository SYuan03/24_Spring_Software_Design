package org.example.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SYuan03
 * @date 2024/3/26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerDTO {
    private int id;
    private Object answer;
}
