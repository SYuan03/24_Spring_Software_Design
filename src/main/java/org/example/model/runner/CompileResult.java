package org.example.model.runner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SYuan03
 * @date 2024/4/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompileResult {
    private boolean success;
    private String errorMessage;
}
