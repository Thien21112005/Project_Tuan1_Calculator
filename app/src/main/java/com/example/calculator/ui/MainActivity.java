package com.example.calculator.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast; // Đừng quên import Toast để hiện lỗi

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.calculator.CalculatorContract;
import com.example.calculator.R;
import com.example.calculator.presenter.CalculatorPresenter;

public class MainActivity extends AppCompatActivity implements CalculatorContract.View {

    private TextView tvResult;
    private TextView tvEquation;
    private CalculatorContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Ánh xạ View
        tvResult = findViewById(R.id.tv_result);
        tvEquation = findViewById(R.id.tv_equation);

        // 2. Khởi tạo Presenter
        presenter = new CalculatorPresenter(this);

        // 3. Cài đặt sự kiện (PHẦN NÀY ĐÃ THAY ĐỔI NHIỀU NHẤT)
        setButtonListeners();
    }

    // --- HÀM GÁN SỰ KIỆN MỚI ---
    private void setButtonListeners() {

        // NHÓM 1: CÁC NÚT SỐ (0-9 và dấu chấm)
        // Gom ID các nút số vào một mảng riêng
        int[] numberIds = {
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
                R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9,
                R.id.btn_dot
        };

        // Tạo bộ lắng nghe chuyên cho số
        View.OnClickListener numberListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button btn = (Button) view;
                // Gọi hàm onNumberClicked bên Presenter
                presenter.onNumberClicked(btn.getText().toString());
            }
        };

        // Gán sự kiện cho từng nút số trong mảng
        for (int id : numberIds) {
            findViewById(id).setOnClickListener(numberListener);
        }

        // ---------------------------------------------------------

        // NHÓM 2: CÁC NÚT PHÉP TÍNH (+ - * /)
        // Gom ID các nút phép tính vào mảng riêng
        int[] operatorIds = {
                R.id.btn_add, R.id.btn_sub, R.id.btn_mul, R.id.btn_div
                // Lưu ý: Nút % (btn_percent) nếu muốn dùng cũng có thể coi là phép tính
                // hoặc xử lý riêng tùy logic bạn muốn.
        };

        // Tạo bộ lắng nghe chuyên cho phép tính
        View.OnClickListener operatorListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button btn = (Button) view;
                // Gọi hàm onOperatorClicked bên Presenter
                presenter.onOperatorClicked(btn.getText().toString());
            }
        };

        for (int id : operatorIds) {
            findViewById(id).setOnClickListener(operatorListener);
        }

        // ---------------------------------------------------------

        // NHÓM 3: CÁC NÚT CHỨC NĂNG ĐẶC BIỆT (Xử lý riêng lẻ từng cái)

        // Nút AC (Xóa hết) -> Gọi onClearClicked
        findViewById(R.id.btn_ac).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onClearClicked();
            }
        });

        // Nút DEL (Xóa 1 ký tự) -> Gọi onDeleteClicked
        findViewById(R.id.btn_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onDeleteClicked();
            }
        });

        // Nút Bằng (=) -> Gọi onEqualClicked
        findViewById(R.id.btn_equal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onEqualClicked();
            }
        });

        // Lưu ý: Nút btn_neg (+/-) và btn_percent (%) hiện tại chưa có hàm trong Interface Presenter
        // Nếu bạn muốn dùng, bạn cần thêm hàm vào Interface trước, sau đó gọi ở đây.
    }

    // --- CÁC HÀM CỦA INTERFACE VIEW (Không thay đổi) ---

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
        // Thay vì set text, nên dùng Toast cho đẹp hoặc set text tùy ý bạn
        tvResult.setText(message);
        // Toast.makeText(this, message, Toast.LENGTH_SHORT).show(); // Có thể dùng dòng này
    }
}