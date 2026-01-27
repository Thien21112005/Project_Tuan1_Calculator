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
import com.example.calculator.presenter.CalculatorPresenter;

// QUAN TRỌNG: Phải implements CalculatorContract.View để nhận lệnh hiển thị
public class MainActivity extends AppCompatActivity implements CalculatorContract.View {

    // --- KHAI BÁO BIẾN GIAO DIỆN ---
    private TextView tvResult;      // Hiển thị kết quả to
    private TextView tvEquation;    // Hiển thị biểu thức nhỏ

    // --- KHAI BÁO BỘ NÃO (PRESENTER) ---
    private CalculatorContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Code xử lý tràn viền của bạn (Giữ nguyên)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Ánh xạ View (Tìm các thành phần trên màn hình)
        tvResult = findViewById(R.id.tv_result);
        tvEquation = findViewById(R.id.tv_equation);

        // 2. Khởi tạo Presenter (Giao chính Activity này cho Presenter quản lý)
        presenter = new CalculatorPresenter(this);

        // 3. Cài đặt sự kiện bấm nút
        setButtonListeners();
    }

    // Hàm này giúp gom gọn việc gán sự kiện click
    private void setButtonListeners() {
        // Mảng chứa ID của TẤT CẢ các nút trong máy tính
        int[] buttonIds = {
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
                R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9,
                R.id.btn_dot,       // Dấu chấm
                R.id.btn_add, R.id.btn_sub, R.id.btn_mul, R.id.btn_div, // Phép tính
                R.id.btn_equal,     // Dấu bằng
                R.id.btn_ac,        // Xóa hết
                R.id.btn_del,       // Xóa 1 số
                R.id.btn_percent,   // Phần trăm
                R.id.btn_neg        // Đổi dấu +/-
        };

        // Tạo 1 người nghe sự kiện chung cho mọi nút
        View.OnClickListener commonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ép kiểu View về Button để lấy chữ trên nút
                Button btn = (Button) view;
                String buttonText = btn.getText().toString();

                // Gửi thông tin sang cho Presenter xử lý
                // "Ê Presenter, người dùng vừa bấm nút có chữ [buttonText] nè!"
                presenter.onButtonClick(view, buttonText);
            }
        };

        // Gán người nghe này cho từng nút trong danh sách
        for (int id : buttonIds) {
            View v = findViewById(id);
            if (v != null) {
                v.setOnClickListener(commonListener);
            }
        }
    }

    // --- CÁC HÀM CỦA INTERFACE (Presenter sẽ gọi các hàm này) ---

    @Override
    public void showResult(String result) {
        tvResult.setText(result); // Cập nhật số to
    }

    @Override
    public void showEquation(String equation) {
        tvEquation.setText(equation); // Cập nhật dòng biểu thức nhỏ
    }

    @Override
    public void showError(String message) {
        tvResult.setText(message); // Hiện thông báo lỗi (VD: "Lỗi")
    }
}