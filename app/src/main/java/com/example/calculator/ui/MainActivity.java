package com.example.calculator.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.calculator.CalculatorContract;
import com.example.calculator.R;
import com.example.calculator.model.CalculatorModel;
import com.example.calculator.presenter.CalculatorPresenter;

public class MainActivity extends AppCompatActivity
        implements CalculatorContract.View {

    private TextView tvResult;
    private TextView tvEquation;
    private CalculatorContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupMVP();
        setButtonListeners();
    }

    private void initViews() {
        tvResult = findViewById(R.id.tv_result);
        tvEquation = findViewById(R.id.tv_equation);
    }

    private void setupMVP() {
        CalculatorContract.Model model = new CalculatorModel();
        presenter = new CalculatorPresenter(model);
        presenter.attachView(this);
    }

    private void setButtonListeners() {
        int[] numberIds = {
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
                R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9,
                R.id.btn_dot
        };

        View.OnClickListener numberListener = v -> {
            Button btn = (Button) v;
            presenter.onNumberClicked(btn.getText().toString());
        };

        for (int id : numberIds) {
            findViewById(id).setOnClickListener(numberListener);
        }

        int[] operatorIds = {
                R.id.btn_add, R.id.btn_sub, R.id.btn_mul, R.id.btn_div
        };

        View.OnClickListener operatorListener = v -> {
            Button btn = (Button) v;
            presenter.onOperatorClicked(btn.getText().toString());
        };

        for (int id : operatorIds) {
            findViewById(id).setOnClickListener(operatorListener);
        }

        findViewById(R.id.btn_ac).setOnClickListener(v ->
                presenter.onClearClicked());
        findViewById(R.id.btn_del).setOnClickListener(v ->
                presenter.onDeleteClicked());
        findViewById(R.id.btn_equal).setOnClickListener(v ->
                presenter.onEqualClicked());
    }

    @Override
    public void showResult(String result) {
        tvResult.setText(result);
    }

    @Override
    public void showEquation(String equation) {
        tvEquation.setText(equation);
    }

    @Override
    public void showError(String message) {
        tvResult.setText(message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
