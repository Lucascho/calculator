package com.example.calculator;
import java.math.BigDecimal;
import java.util.Stack;

class CalculatorService {
    private BigDecimal currentResult = BigDecimal.ZERO;
    private final Stack<State> undoStack = new Stack<>();
    private final Stack<State> redoStack = new Stack<>();

    private static class State {
        BigDecimal result;
        String expression;

        State(BigDecimal result, String expression) {
            this.result = result;
            this.expression = expression;
        }
    }

    private String currentExpression = "";

    public BigDecimal compute(BigDecimal a, BigDecimal b, String operator) {
        undoStack.push(new State(currentResult, currentExpression));
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

        currentExpression = a.toPlainString() + " " + operator + " " + b.toPlainString();
        return currentResult;
    }

    public BigDecimal undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(new State(currentResult, currentExpression));
            State previous = undoStack.pop();
            currentResult = previous.result;
            currentExpression = previous.expression;
        }
        return currentResult;
    }

    public BigDecimal redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(new State(currentResult, currentExpression));
            State next = redoStack.pop();
            currentResult = next.result;
            currentExpression = next.expression;
        }
        return currentResult;
    }

    public BigDecimal getCurrentResult() {
        return currentResult;
    }

    public String getCurrentExpression() {
        return currentExpression;
    }
}
