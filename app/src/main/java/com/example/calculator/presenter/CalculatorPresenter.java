package com.example.calculator.presenter;

import android.view.View;
import com.example.calculator.CalculatorContract;
import com.example.calculator.model.CalculatorModel;
import com.example.calculator.utils.NumberFormatter;

public class CalculatorPresenter implements CalculatorContract.Presenter {

    private CalculatorContract.View view;
    private CalculatorModel model;
    private String expression = ""; // Chuỗi biểu thức (VD: 5+6x3)
    private boolean isResultShown = false;

    public CalculatorPresenter(CalculatorContract.View view) {
        this.view = view;
        this.model = new CalculatorModel();
    }

    // --- HÀM XỬ LÝ CHÍNH ---
    @Override
    public void onButtonClick(View v, String buttonText) {

        // 1. Kiểm tra xem nút bấm là loại gì?

        if (isNumber(buttonText)) {
            handleNumber(buttonText);
        }
        else if (isOperator(buttonText)) {
            handleOperator(buttonText);
        }
        else if (buttonText.equals("=")) {
            handleEqual();
        }
        else if (buttonText.equals("AC")) {
            handleClear();
        }
        else if (buttonText.equals("DEL")) {
            handleDelete();
        }
        // Bạn có thể thêm else if cho %, +/- ở đây...
    }

    // --- CÁC HÀM XỬ LÝ RIÊNG (Private) ---
    // (Mình tách logic ra các hàm con này cho code gọn, dễ nhìn)

    private void handleNumber(String number) {
        if (isResultShown) {
            expression = "";
            isResultShown = false;
            view.showEquation("");
        }
        expression += number;
        view.showResult(expression);
    }

    private void handleOperator(String op) {
        if (expression.isEmpty()) return;
        if (isResultShown) {
            isResultShown = false;
            view.showEquation("");
        }
        // Kiểm tra thay thế dấu nếu bấm nhầm (VD: 5+ rồi bấm - -> thành 5-)
        char lastChar = expression.charAt(expression.length() - 1);
        if (isOperator(String.valueOf(lastChar))) {
            expression = expression.substring(0, expression.length() - 1);
        }
        expression += op;
        view.showResult(expression);
    }

    private void handleEqual() {
        if (expression.isEmpty()) return;
        try {
            double result = model.evaluate(expression);
            String finalResult = NumberFormatter.format(result);
            view.showResult(finalResult);
            view.showEquation(expression + " =");
            expression = finalResult;
            isResultShown = true;
        } catch (Exception e) {
            view.showError("Lỗi");
            expression = "";
        }
    }

    private void handleClear() {
        expression = "";
        view.showResult("0");
        view.showEquation("");
    }

    private void handleDelete() {
        if (expression.isEmpty() || isResultShown) {
            expression = "";
            view.showResult("0");
            return;
        }
        expression = expression.substring(0, expression.length() - 1);
        view.showResult(expression.isEmpty() ? "0" : expression);
    }

    // --- HÀM HỖ TRỢ KIỂM TRA ---

    // Kiểm tra xem chuỗi s có phải là số (0-9) hoặc dấu chấm không
    private boolean isNumber(String s) {
        return "0123456789.".contains(s);
    }

    // Kiểm tra xem chuỗi s có phải phép tính không
    private boolean isOperator(String s) {
        return "+-x/".contains(s);
    }
}