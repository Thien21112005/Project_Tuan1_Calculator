package com.example.calculator.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calculator.CalculatorContract;
import com.example.calculator.R;
import com.example.calculator.presenter.CalculatorPresenter;
import com.google.android.material.button.MaterialButton;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements CalculatorContract.View {

    private TextView tvResult, tvExpression;
    private CalculatorPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);
        tvExpression = findViewById(R.id.tvExpression);

        presenter = new CalculatorPresenter(this);

        setupButtonListeners();
    }

    private void setupButtonListeners() {
        int[] buttonIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
                R.id.btnDot, R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply,
                R.id.btnDivide, R.id.btnEquals, R.id.btnClear, R.id.btnDelete,
                R.id.btnPercent, R.id.btnPlusMinus, R.id.btnSin, R.id.btnCos,
                R.id.btnTan, R.id.btnLog, R.id.btnLn, R.id.btnPower,
                R.id.btnSqrt, R.id.btnPi, R.id.btnE, R.id.btnOpenParen,
                R.id.btnCloseParen, R.id.btnFactorial
        };

        Arrays.stream(buttonIds).forEach(id -> {
            MaterialButton button = findViewById(id);
            if (button != null) {
                button.setOnClickListener(v ->
                        presenter.onButtonClick(v, button.getText().toString()));
            }
        });
    }

    @Override
    public void updateResult(String result) {
        tvResult.setText(result);
    }

    @Override
    public void updateExpression(String expression) {
        tvExpression.setText(expression);
    }

    @Override
    public void showError(String message) {
        tvExpression.setText(message);
        tvResult.setText("Error");
    }
}