package org.example.util;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author SYuan03
 * @date 2024/5/5
 */
@Slf4j
public class CyclomaticComplexityCalculator {
    private int totalComplexity = 0;

    public int calculateClassComplexity(String sourceCodePath) {
        try {
            CompilationUnit cu = StaticJavaParser.parse(Files.newInputStream(Paths.get(sourceCodePath))); // 解析Java源代码
            List<MethodDeclaration> methods = cu.findAll(MethodDeclaration.class); // 找到所有的函数
            for (MethodDeclaration method : methods) {
                int complexityCounter = 1; // 初始值为1
                complexityCounter = calculateComplexity(method, complexityCounter); // 计算每个函数的复杂度
                totalComplexity += complexityCounter;
            }
            return totalComplexity;
        } catch (IOException e) {
            log.error("Failed to calculate cyclomatic complexity", e);
            return -1;
        }
    }

    private int calculateComplexity(Node node, int complexityCounter) {
        // 按照作业亚要求里面的计算方式，只有if、for、while、do、三目运算符、逻辑运算符and/or会增加复杂度
        if (node instanceof IfStmt || node instanceof ForStmt || node instanceof WhileStmt || node instanceof DoStmt
                || node instanceof ConditionalExpr || isAndOrOperator(node)) {
            complexityCounter++;
        }

        for (Node child : node.getChildNodes()) {
            complexityCounter = calculateComplexity(child, complexityCounter);
        }

        return complexityCounter;
    }

    private boolean isAndOrOperator(Node node) {
        if (node instanceof BinaryExpr) {
            BinaryExpr.Operator operator = ((BinaryExpr) node).getOperator();
            return operator == BinaryExpr.Operator.AND || operator == BinaryExpr.Operator.OR;
        }
        return false;
    }
}