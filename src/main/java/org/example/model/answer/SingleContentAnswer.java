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
 *
 * WHYY: 要不要再定义一个ProgramAnswer类呢
 * 理论上这两个类不一样，但其实由于提供的是filePath
 * 所以也可以用一个类来表示?（可以问问
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class SingleContentAnswer extends Answer {
    // @EqualsAndHashCode(callSuper = true)
    // 这个注解确保了子类在比较相等性或计算哈希码时，也会考虑到超类的字段
    private String content;
}
