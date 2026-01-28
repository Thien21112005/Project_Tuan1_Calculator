package com.example.calculator.presenter;

import com.example.calculator.CalculatorContract;
import com.example.calculator.model.CalculatorModel;
import com.example.calculator.utils.NumberFormatter;

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

    // --- 1. XỬ LÝ KHI BẤM SỐ (ĐÃ SỬA LOGIC CHẶN DẤU CHẤM) ---
    @Override
    public void onNumberClicked(String number) {
        // Nếu trước đó vừa ra kết quả (VD: bấm = ra 10), giờ bấm số mới thì reset lại từ đầu
        if (isResultShown) {
            expression = "";
            isResultShown = false;
            view.showEquation("");
        }

        // --- BẮT ĐẦU SỬA: Kiểm tra kỹ trước khi thêm ---

        if (number.equals(".")) {
            // Nếu người dùng bấm dấu chấm:

            // Trường hợp 1: Chưa nhập gì hoặc ký tự cuối là phép tính (VD: "5+")
            // -> Tự động thêm số 0 đằng trước thành "0." cho hợp lý
            if (expression.isEmpty() || isLastCharOperator()) {
                expression += "0.";
            }
            // Trường hợp 2: Đang nhập dở số (VD: "12"), kiểm tra xem số này ĐÃ CÓ dấu chấm chưa?
            // Nếu CHƯA có (!hasDot...) thì mới cho thêm.
            else if (!hasDotInCurrentNumber()) {
                expression += ".";
            }
            // Trường hợp 3: Nếu đã có rồi (VD: "12.5") -> Thì lờ đi, không làm gì cả.

        } else {
            // Nếu là số bình thường (0-9) thì thêm như cũ
            expression += number;
        }

        // --- KẾT THÚC SỬA ---

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
            // Gọi Model để tính toán
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

    // --- CÁC HÀM PHỤ TRỢ (Private) ---

    // 1. Kiểm tra ký tự có phải phép tính không
    private boolean isOperator(String s) {
        return "+-x/".contains(s);
    }

    // 2. Kiểm tra ký tự cuối cùng của chuỗi hiện tại có phải là phép tính không
    // (Dùng để bổ trợ cho logic thêm dấu chấm)
    private boolean isLastCharOperator() {
        if (expression.isEmpty()) return false;
        char lastChar = expression.charAt(expression.length() - 1);
        return isOperator(String.valueOf(lastChar));
    }

    // 3. Kiểm tra xem con số HIỆN TẠI (số cuối cùng đang nhập) đã có dấu chấm chưa
    private boolean hasDotInCurrentNumber() {
        // Duyệt ngược từ cuối chuỗi về đầu
        for (int i = expression.length() - 1; i >= 0; i--) {
            char c = expression.charAt(i);

            // Nếu gặp dấu chấm -> Báo là CÓ RỒI (true)
            if (c == '.') {
                return true;
            }

            // Nếu gặp phép tính (+ - * /) -> Nghĩa là đã hết con số hiện tại -> Báo là CHƯA CÓ (false)
            // Ví dụ: biểu thức là "5.5 + 2" -> đang kiểm tra số 2, gặp dấu + là dừng kiểm tra.
            if (isOperator(String.valueOf(c))) {
                return false;
            }
        }
        // Duyệt hết mà không thấy gì -> Chưa có
        return false;
    }
}