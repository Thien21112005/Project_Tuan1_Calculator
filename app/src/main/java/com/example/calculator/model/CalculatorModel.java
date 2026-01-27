package com.example.calculator.model;

public class CalculatorModel {

    // Hàm thực hiện phép tính
    // Trả về kết quả kiểu double
    public double calculate(double num1, double num2, String operator) {
        switch (operator) {
            case CalculatorConstants.ADD:
                return num1 + num2;
            case CalculatorConstants.SUBTRACT:
                return num1 - num2;
            case CalculatorConstants.MULTIPLY:
                return num1 * num2;
            case CalculatorConstants.DIVIDE:
                if (num2 == 0) throw new ArithmeticException("Divide by zero"); // Báo lỗi nếu chia 0
                return num1 / num2;
            default:
                return 0;
        }
    }
}