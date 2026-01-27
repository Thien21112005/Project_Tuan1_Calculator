package com.example.calculator;

public interface CalculatorContract {
    // 1. View (Giao diện) cần làm gì?
    interface View {
        void updateResult(String result);     // Hiển thị kết quả (số to)
        void updateEquation(String equation); // Hiển thị phép tính (số nhỏ màu xám)
    }

    // 2. Presenter (Bộ não) cần xử lý những hành động nào?
    interface Presenter {
        void onNumberClick(String number);    // Khi bấm số 0-9 và dấu chấm
        void onOperatorClick(String op);      // Khi bấm +, -, x, /
        void onEqualClick();                  // Khi bấm =
        void onClearClick();                  // Khi bấm AC (Xóa hết)
        void onDeleteClick();                 // Khi bấm DEL (Xóa 1 ký tự)
        void onPercentClick();                // Khi bấm %
        void onNegateClick();                 // Khi bấm +/- (Đổi dấu âm dương)
    }
}