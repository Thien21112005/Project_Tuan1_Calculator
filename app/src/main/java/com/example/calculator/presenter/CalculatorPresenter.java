package com.example.calculator.presenter;

import com.example.calculator.CalculatorContract;
import com.example.calculator.model.CalculatorModel; // Giả sử bạn đã có class này
import com.example.calculator.utils.NumberFormatter; // Giả sử bạn đã có class này

public class CalculatorPresenter implements CalculatorContract.Presenter {

    private CalculatorContract.View view;
    private CalculatorModel model;

    // Biến lưu trữ trạng thái
    private String expression = ""; // Chuỗi phép tính hiện tại
    private boolean isResultShown = false; // Cờ đánh dấu: vừa bấm dấu bằng xong hay chưa

    public CalculatorPresenter(CalculatorContract.View view) {
        this.view = view;
        this.model = new CalculatorModel();
    }

    // --- 1. XỬ LÝ KHI BẤM SỐ ---
    @Override
    public void onNumberClicked(String number) {
        // Nếu trước đó vừa ra kết quả (VD: bấm = ra 10), giờ bấm số mới thì reset lại từ đầu
        if (isResultShown) {
            expression = "";
            isResultShown = false;
            view.showEquation("");
        }

        expression += number; // Nối thêm số vào chuỗi (VD: "5" -> "52")
        view.showResult(expression); // Hiển thị ra màn hình
    }

    // --- 2. XỬ LÝ KHI BẤM PHÉP TÍNH (+ - x /) ---
    @Override
    public void onOperatorClicked(String op) {
        if (expression.isEmpty()) return; // Chưa có số thì không bấm được phép tính

        // Nếu vừa có kết quả, lấy kết quả đó để tính tiếp
        if (isResultShown) {
            isResultShown = false;
            view.showEquation("");
        }

        // Logic thông minh: Kiểm tra ký tự cuối cùng
        // Nếu người dùng lỡ tay bấm 5+ rồi bấm nhầm thành -, ta thay thế + thành -
        char lastChar = expression.charAt(expression.length() - 1);
        if (isOperator(String.valueOf(lastChar))) {
            // Xóa ký tự cuối đi (xóa dấu cũ)
            expression = expression.substring(0, expression.length() - 1);
        }

        expression += op; // Thêm dấu mới vào
        view.showResult(expression);
    }

    // --- 3. XỬ LÝ KHI BẤM DẤU BẰNG (=) ---
    @Override
    public void onEqualClicked() {
        if (expression.isEmpty()) return;

        try {
            // Gọi Model để tính toán (Model chứa thư viện exp4j hoặc logic tính toán)
            double result = model.evaluate(expression);

            // Format số đẹp (bỏ số 0 thừa ở đuôi .0)
            String finalResult = NumberFormatter.format(result);

            view.showResult(finalResult);       // Hiện kết quả to
            view.showEquation(expression + " ="); // Hiện phép tính nhỏ ở trên

            expression = finalResult; // Lưu kết quả lại để tính tiếp nếu cần
            isResultShown = true;     // Đánh dấu là đã xong 1 phép tính

        } catch (Exception e) {
            view.showError("Lỗi biểu thức");
            expression = ""; // Reset nếu lỗi
        }
    }

    // --- 4. XỬ LÝ KHI BẤM XÓA HẾT (AC) ---
    @Override
    public void onClearClicked() {
        expression = "";
        view.showResult("0");
        view.showEquation("");
        isResultShown = false;
    }

    // --- 5. XỬ LÝ KHI BẤM XÓA 1 KÝ TỰ (DEL) ---
    @Override
    public void onDeleteClicked() {
        // Nếu chuỗi rỗng hoặc vừa hiện kết quả xong thì reset về 0
        if (expression.isEmpty() || isResultShown) {
            expression = "";
            view.showResult("0");
            return;
        }

        // Cắt bỏ ký tự cuối cùng
        expression = expression.substring(0, expression.length() - 1);

        // Nếu xóa hết sạch thì hiện số 0, ngược lại hiện phần còn lại
        view.showResult(expression.isEmpty() ? "0" : expression);
    }

    // --- HÀM PHỤ TRỢ (Private) ---
    private boolean isOperator(String s) {
        return "+-x/".contains(s);
    }
}