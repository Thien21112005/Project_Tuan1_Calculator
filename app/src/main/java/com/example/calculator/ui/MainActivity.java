package com.example.calculator.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.calculator.R;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    // --- KHAI BÁO BIẾN ---
    TextView tvResult;      // Hiển thị số to (kết quả)
    TextView tvEquation;    // MỚI: Hiển thị biểu thức (9 + 9)

    String operator = "";
    double firstNumber = 0;
    boolean isNewOp = true;

    // MỚI: Biến lưu toàn bộ biểu thức để hiển thị dòng trên
    String expressionString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Code xử lý tràn viền của bạn
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}