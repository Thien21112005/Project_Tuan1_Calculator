package com.example.calculator;

import android.view.View;

public interface CalculatorContract {
    interface View {
        void showResult(String result);
        void showEquation(String equation);
        void showError(String message);
    }

    interface Presenter {
        // HÀM DUY NHẤT: Nhận vào View (nút vừa bấm) và Text (chữ trên nút)
        void onButtonClick(android.view.View v, String buttonText);
    }
}