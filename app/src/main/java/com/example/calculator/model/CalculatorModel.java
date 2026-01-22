//package com.example.calculator.model;
//
//import java.util.Optional;
//
//public class CalculatorModel {
//
//    private double firstNumber = 0;
//    private double secondNumber = 0;
//    private String operator = "";
//    private String currentNumber = "";
//    private String fullExpression = "";
//    private boolean isNewOperation = true;
//    private boolean hasDecimal = false;
//
//    public void handleNumber(String number) {
//        if (isNewOperation) {
//            currentNumber = number;
//            isNewOperation = false;
//            fullExpression = number;
//        } else if (currentNumber.length() < 15) {
//            currentNumber += number;
//            fullExpression += number;
//        }
//    }
//
//    public void handleDecimal() {
//        if (isNewOperation) {
//            currentNumber = "0.";
//            fullExpression = "0.";
//            isNewOperation = false;
//            hasDecimal = true;
//        } else if (!hasDecimal && currentNumber.length() < 15) {
//            if (currentNumber.isEmpty()) {
//                currentNumber = "0.";
//                fullExpression += "0.";
//            } else {
//                currentNumber += ".";
//                fullExpression += ".";
//            }
//            hasDecimal = true;
//        }
//    }
//
//    public void handleOperator(String op) {
//        if (!currentNumber.isEmpty()) {
//            if (!operator.isEmpty()) {
//                performCalculation(); // Chain if previous operator exists
//            }
//            firstNumber = safeParseDouble(currentNumber);
//            operator = op;
//            fullExpression = currentNumber + " " + op + " ";
//        } else if (!fullExpression.isEmpty()) {
//            fullExpression = fullExpression.replaceAll("[+−×÷]\\s*$", op + " ");
//            operator = op;
//        }
//        currentNumber = "";
//        isNewOperation = true;
//        hasDecimal = false;
//    }
//
//    public void handleEquals() {
//        if (!currentNumber.isEmpty() && !operator.isEmpty()) {
//            secondNumber = safeParseDouble(currentNumber);
//            double result = performCalculation();
//            if (!Double.isNaN(result) && !Double.isInfinite(result)) {
//                fullExpression = formatNumber(firstNumber) + " " + operator + " " + formatNumber(secondNumber) + " =";
//            }
//        }
//    }
//
//    public double performCalculation() {
//        if (operator.isEmpty() || currentNumber.isEmpty()) return firstNumber;
//
//        secondNumber = safeParseDouble(currentNumber);
//        double result = Optional.ofNullable(CalculatorConstants.OPERATION_MAP.get(operator))
//                .map(op -> op.apply(firstNumber, secondNumber))
//                .orElse(Double.NaN);
//
//        if (Double.isNaN(result) || Double.isInfinite(result)) {
//            return Double.NaN;
//        }
//
//        firstNumber = result;
//        currentNumber = String.valueOf(result);
//        fullExpression = currentNumber;
//        operator = "";
//        isNewOperation = true;
//        hasDecimal = currentNumber.contains(".");
//        return result;
//    }
//
//    public void handlePercent() {
//        if (!currentNumber.isEmpty()) {
//            try {
//                double value = safeParseDouble(currentNumber);
//                value /= 100;
//                currentNumber = String.valueOf(value);
//                fullExpression = currentNumber;
//            } catch (Exception e) {
//                // Ignore invalid
//            }
//        }
//    }
//
//    public void handlePlusMinus() {
//        if (!currentNumber.isEmpty() && !currentNumber.equals("0")) {
//            if (currentNumber.startsWith("-")) {
//                currentNumber = currentNumber.substring(1);
//            } else {
//                currentNumber = "-" + currentNumber;
//            }
//        }
//    }
//
//    public void handleScientific(String function) {
//        if (currentNumber.isEmpty()) return;
//
//        try {
//            double value = safeParseDouble(currentNumber);
//            double result = Optional.ofNullable(CalculatorConstants.SCIENTIFIC_MAP.get(function))
//                    .map(f -> f.apply(value))
//                    .orElse(Double.NaN);
//
//            if (Double.isNaN(result)) {
//                fullExpression = "Error";
//                currentNumber = "";
//                return;
//            }
//
//            String display = getScientificDisplay(function, value);
//            fullExpression = display;
//            currentNumber = String.valueOf(result);
//            isNewOperation = true;
//            hasDecimal = currentNumber.contains(".");
//        } catch (Exception e) {
//            fullExpression = "Invalid input";
//        }
//    }
//
//    private String getScientificDisplay(String function, double value) {
//        String formatted = formatNumber(value);
//        return switch (function) {
//            case "sin" -> "sin(" + formatted + "°)";
//            case "cos" -> "cos(" + formatted + "°)";
//            case "tan" -> "tan(" + formatted + "°)";
//            case "log" -> "log(" + formatted + ")";
//            case "ln" -> "ln(" + formatted + ")";
//            case "power" -> formatted + "²";
//            case "sqrt" -> "√(" + formatted + ")";
//            case "factorial" -> formatted + "!";
//            default -> "";
//        };
//    }
//
//    public void handleConstant(double constant, String name) {
//        currentNumber = String.valueOf(constant);
//        fullExpression = name;
//        isNewOperation = false;
//        hasDecimal = true;
//    }
//
//    public void handleParenthesis(String paren) {
//        if (paren.equals("(")) {
//            if (isNewOperation || currentNumber.isEmpty()) {
//                fullExpression += "(";
//            }
//        } else if (paren.equals(")")) {
//            if (!currentNumber.isEmpty()) {
//                fullExpression += ")";
//            }
//        }
//    }
//
//    public void handleClear() {
//        clearAll();
//    }
//
//    public void handleDelete() {
//        if (!currentNumber.isEmpty() && !isNewOperation) {
//            if (currentNumber.endsWith(".")) hasDecimal = false;
//            currentNumber = currentNumber.substring(0, currentNumber.length() - 1);
//            fullExpression = fullExpression.substring(0, fullExpression.length() - 1);
//            if (currentNumber.isEmpty()) {
//                currentNumber = "0";
//                isNewOperation = true;
//            }
//        }
//    }
//
//    public String getResultDisplay() {
//        if (currentNumber.isEmpty()) return "0";
//        try {
//            double value = safeParseDouble(currentNumber);
//            if (Double.isNaN(value) || Double.isInfinite(value)) return "Error";
//            return formatNumber(value);
//        } catch (Exception e) {
//            return currentNumber;
//        }
//    }
//
//    public String getExpression() {
//        return fullExpression;
//    }
//
//    public void clearAll() {
//        currentNumber = "";
//        operator = "";
//        firstNumber = 0;
//        secondNumber = 0;
//        isNewOperation = true;
//        hasDecimal = false;
//        fullExpression = "";
//    }
//
//    private double safeParseDouble(String s) {
//        try {
//            return Double.parseDouble(s);
//        } catch (Exception e) {
//            return 0;
//        }
//    }
//
//    private String formatNumber(double number) {
//        if (Double.isNaN(number)) return "Error";
//        if (Double.isInfinite(number)) return "∞";
//        if (Math.abs(number) < 0.000001 && number != 0) {
//            return String.format("%.6e", number);
//        }
//        String result = CalculatorConstants.DECIMAL_FORMAT.format(number);
//        if (result.contains(".")) {
//            result = result.replaceAll("0+$", "").replaceAll("\\.$", "");
//        }
//        return result;
//    }
//}