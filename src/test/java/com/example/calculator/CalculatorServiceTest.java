package com.example.calculator;
import java.math.BigDecimal;
import java.util.Stack;

class CalculatorService {
    private BigDecimal currentResult = BigDecimal.ZERO;
    private final Stack<BigDecimal> undoStack = new Stack<>();
    private final Stack<BigDecimal> redoStack = new Stack<>();

    public BigDecimal compute(BigDecimal a, BigDecimal b, String operator) {
        undoStack.push(currentResult);
        redoStack.clear();

        switch (operator) {
            case "+":
                currentResult = a.add(b);
                break;
            case "-":
                currentResult = a.subtract(b);
                break;
            case "*":
                currentResult = a.multiply(b);
                break;
            case "/":
                if (b.compareTo(BigDecimal.ZERO) == 0) {
                    throw new ArithmeticException("不能除以零");
                }
                currentResult = a.divide(b, 10, BigDecimal.ROUND_HALF_UP);
                break;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
        return currentResult;
    }

    public BigDecimal undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(currentResult);
            currentResult = undoStack.pop();
        }
        return currentResult;
    }

    public BigDecimal redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(currentResult);
            currentResult = redoStack.pop();
        }
        return currentResult;
    }

    public BigDecimal getCurrentResult() {
        return currentResult;
    }
}
