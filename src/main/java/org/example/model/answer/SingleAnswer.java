package org.example.model.answer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author SYuan03
 * @date 2024/3/26
 * Single指除了一个answer字段(content)之外没有别的了
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class SingleAnswer extends Answer {
    // @EqualsAndHashCode(callSuper = true)
    // 这个注解确保了子类在比较相等性或计算哈希码时，也会考虑到超类的字段
    private String content;
}
