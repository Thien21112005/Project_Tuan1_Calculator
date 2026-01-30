package com.example.calculator.model;

import com.example.calculator.CalculatorContract;

import java.util.Locale;
import java.util.Stack;

public class CalculatorModel implements CalculatorContract.Model {
    private String expression = "";
    private String previousExpression = "";
    private boolean isResultShown = false;

    @Override
    public void inputNumber(String number) {
        if (isResultShown) {
            expression = "";
            previousExpression = "";
            isResultShown = false;
        }

        if (number.equals(".")) {
            if (expression.isEmpty() || isLastCharOperator() || expression.equals("-")) {
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
        // Cho phép bắt đầu bằng số âm
        if (expression.isEmpty() && operator.equals("-")) {
            expression = "-";
            return;
        }

        // Chặn nhập operator nếu chỉ có dấu "-"
        if (expression.length() == 1 && expression.equals("-")) {
            return;
        }

        if (expression.isEmpty()) return;

        if (isResultShown) {
            isResultShown = false;
            previousExpression = "";
        }

        char lastChar = expression.charAt(expression.length() - 1);

        if (isOperator(lastChar) && expression.length() != 1) {
            expression = expression.substring(0, expression.length() - 1);
        }

        expression += operator;
    }

    @Override
    public void percent() {
        if (expression.isEmpty()) return;

        // Chặn nhập % nếu chỉ có dấu "-"
        if (expression.equals("-")) {
            return;
        }

        if (isResultShown) {
            isResultShown = false;
            previousExpression = "";
        }

        char lastChar = expression.charAt(expression.length() - 1);
        if (isOperator(lastChar)) return;
        expression += "%";
    }

    @Override
    public void calculate() {
        if (expression.isEmpty()) return;

        // Chặn tính toán nếu chỉ có dấu "-"
        if (expression.equals("-")) {
            throw new RuntimeException("Invalid expression");
        }

        try {
            previousExpression = expression;
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
        previousExpression = "";
        isResultShown = false;
    }

    @Override
    public void delete() {
        if (expression.isEmpty() || isResultShown) {
            expression = "";
            previousExpression = "";
            isResultShown = false;
            return;
        }

        // Nếu xóa hết đến chỉ còn dấu "-"
        if (expression.length() == 2 && expression.charAt(0) == '-') {
            expression = "";
            return;
        }

        expression = expression.substring(0, expression.length() - 1);
    }

    @Override
    public String getCurrentExpression() {
        if (isResultShown && !previousExpression.isEmpty()) {
            return previousExpression;
        }
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

    // Private helper methods
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '×' || c == '÷';
    }

    private boolean isPercent(char c) {
        return c == '%';
    }

    private boolean isLastCharOperator() {
        if (expression.isEmpty()) return false;
        char lastChar = expression.charAt(expression.length() - 1);

        // Nếu biểu thức chỉ là "-", không coi là toán tử
        if (expression.equals("-")) {
            return false;
        }

        return isOperator(lastChar);
    }

    private boolean hasDotInCurrentNumber() {
        for (int i = expression.length() - 1; i >= 0; i--) {
            char c = expression.charAt(i);
            if (c == '.') return true;
            if (isOperator(c)) return false;
        }
        return false;
    }

    // Fixed: Xử lý số âm ở đầu và sau toán tử
    private double evaluateExpression(String expr) {
        Stack<Double> values = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == ' ') continue;

            // Xử lý số (bao gồm số âm ở đầu và sau toán tử)
            if (Character.isDigit(c) || c == '.' ||
                    (c == '-' && (i == 0 || isOperator(expr.charAt(i - 1))))) {

                StringBuilder sb = new StringBuilder();

                // Nếu là số âm (unary minus)
                if (c == '-') {
                    sb.append('-');
                    i++; // Bỏ qua dấu '-'

                    // Kiểm tra nếu đã hết chuỗi
                    if (i >= expr.length()) {
                        throw new RuntimeException("Invalid expression");
                    }
                }
                // Parse phần số
                while (i < expr.length() &&
                        (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    sb.append(expr.charAt(i++));
                }
                i--; // Lùi lại 1 vị trí
                values.push(Double.parseDouble(sb.toString()));
            }
            else if (isPercent(c)) {
                if (values.isEmpty()) {
                    throw new RuntimeException("Invalid percent");
                }
                values.push(values.pop() / 100);
            }
            // Xử lý toán tử binary (không phải unary minus)
            else if (isOperator(c)) {
                while (!operators.isEmpty() && hasPrecedence(c, operators.peek())) {
                    values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
                }
                operators.push(c);
            }
        }
        // Xử lý các toán tử còn lại
        while (!operators.isEmpty()) {
            values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
        }
        return values.pop();
    }

    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') return false;
        if ((op1 == '×' || op1 == '÷') && (op2 == '+' || op2 == '-')) return false;
        return true;
    }

    private double applyOperator(char operator, double b, double a) {
        switch (operator) {
            case '+': return a + b;
            case '-': return a - b;
            case '×': return a * b;
            case '÷':
                if (b == 0) throw new ArithmeticException("Cannot divide by zero");
                return a / b;
            default: return 0;
        }
    }

    private String formatNumber(double number) {
        if (Double.isNaN(number)) return "Error";
        if (Double.isInfinite(number)) return "∞";
        double rounded = Math.round(number * 1e10) / 1e10;
        String result = String.format(Locale.US, "%.10f", rounded);
        return result.replaceAll("\\.0+$|0+$|\\.$", "");
    }
}