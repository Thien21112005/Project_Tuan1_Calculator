package com.example.calculator.model;

import com.example.calculator.CalculatorContract;
import java.util.Stack;

public class CalculatorModel implements CalculatorContract.Model {

    private String expression = "";
    private boolean isResultShown = false;

    @Override
    public void inputNumber(String number) {
        if (isResultShown) {
            expression = "";
            isResultShown = false;
        }

        if (number.equals(".")) {
            if (expression.isEmpty() || isLastCharOperator()) {
                expression += "0.";
            } else if (!hasDotInCurrentNumber()) {
                expression += ".";
            }
        } else {
            expression += number;
        }
    }

    @Override
    public void inputOperator(String operator) {
        if (expression.isEmpty()) return;

        if (isResultShown) {
            isResultShown = false;
        }

        char lastChar = expression.charAt(expression.length() - 1);
        if (isOperator(String.valueOf(lastChar))) {
            expression = expression.substring(0, expression.length() - 1);
        }

        expression += operator;
    }

    @Override
    public void calculate() {
        if (expression.isEmpty()) return;

        try {
            double result = evaluateExpression(expression);
            expression = formatNumber(result);
            isResultShown = true;
        } catch (Exception e) {
            throw new RuntimeException("Invalid expression");
        }
    }

    @Override
    public void clear() {
        expression = "";
        isResultShown = false;
    }

    @Override
    public void delete() {
        if (expression.isEmpty() || isResultShown) {
            expression = "";
            isResultShown = false;
            return;
        }

        expression = expression.substring(0, expression.length() - 1);
    }

    @Override
    public String getCurrentExpression() {
        return expression;
    }

    @Override
    public String getCurrentResult() {
        return expression.isEmpty() ? "0" : expression;
    }

    @Override
    public boolean isResultShown() {
        return isResultShown;
    }

    // Private helpers
    private boolean isOperator(String s) {
        return "+-x/".contains(s);
    }

    private boolean isLastCharOperator() {
        if (expression.isEmpty()) return false;
        char lastChar = expression.charAt(expression.length() - 1);
        return isOperator(String.valueOf(lastChar));
    }

    private boolean hasDotInCurrentNumber() {
        for (int i = expression.length() - 1; i >= 0; i--) {
            char c = expression.charAt(i);
            if (c == '.') return true;
            if (isOperator(String.valueOf(c))) return false;
        }
        return false;
    }

    private double evaluateExpression(String expr) {
        Stack<Double> values = new Stack<>();
        Stack<Character> operators = new Stack<>();

        expr = expr.replace("x", "*");

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);

            if (c == ' ') continue;

            if (Character.isDigit(c) || c == '.') {
                StringBuilder sb = new StringBuilder();
                while (i < expr.length() &&
                        (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    sb.append(expr.charAt(i++));
                }
                i--;
                values.push(Double.parseDouble(sb.toString()));
            }
            else if (c == '+' || c == '-' || c == '*' || c == '/') {
                while (!operators.isEmpty() && hasPrecedence(c, operators.peek())) {
                    values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
                }
                operators.push(c);
            }
        }

        while (!operators.isEmpty()) {
            values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') return false;
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) return false;
        return true;
    }

    private double applyOperator(char operator, double b, double a) {
        switch (operator) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0) throw new ArithmeticException("Cannot divide by zero");
                return a / b;
        }
        return 0;
    }

    private String formatNumber(double number) {
        if (Double.isNaN(number)) return "Error";
        if (Double.isInfinite(number)) return "∞";

        String result = String.format("%.10f", number);
        result = result.replaceAll("0+$", "").replaceAll("\\.$", "");
        return result;
    }
}