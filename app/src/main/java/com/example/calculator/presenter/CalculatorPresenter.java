package com.example.calculator.presenter;

import com.example.calculator.CalculatorContract;
import com.example.calculator.model.CalculatorConstants;
import com.example.calculator.model.CalculatorModel;
import com.example.calculator.utils.NumberFormatter;
public abstract class CalculatorPresenter implements CalculatorContract.Presenter{
    private CalculatorContract.View view;   // Để ra lệnh cho giao diện
    private CalculatorModel model;          // Để nhờ tính toán

    // Các biến lưu trữ tạm thời
    private String currentInput = "";       // Số đang nhập
    private String operator = "";           // Phép tính
    private double firstNumber = 0;         // Số thứ nhất
    private boolean isResultShown = false;  // Biến cờ dùng để đánh dấu hiển thị kết quả

    // Constructor
    public CalculatorPresenter(CalculatorContract.View view) {
        this.view = view;
        this.model = new CalculatorModel(); // Khởi tạo cỗ máy tính toán
    }
    
}