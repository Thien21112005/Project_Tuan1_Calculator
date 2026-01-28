package com.example.calculator;

public interface CalculatorContract {
    interface View {
        void showResult(String result);   // Hiển thị kết quả (số to)
        void showEquation(String equation); // Hiển thị phép tính (số nhỏ ở trên)
        void showError(String message);   // Hiển thị lỗi
    }

    interface Presenter {
        // Tách nhỏ các hành động ra:

        // 1. Khi bấm số (0-9, dấu chấm)
        void onNumberClicked(String number);

        // 2. Khi bấm phép tính (+ - x /)
        void onOperatorClicked(String operator);

        // 3. Khi bấm dấu bằng (=)
        void onEqualClicked();

        // 4. Khi bấm xóa tất cả (AC)
        void onClearClicked();

        // 5. Khi bấm xóa 1 ký tự (DEL)
        void onDeleteClicked();
    }
}