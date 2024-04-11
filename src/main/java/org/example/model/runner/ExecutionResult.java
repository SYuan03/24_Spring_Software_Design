package org.example.model.runner;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * @author SYuan03
 * @date 2024/4/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecutionResult {
    private boolean success;
    private String output;
    private String errorMessage;
}
