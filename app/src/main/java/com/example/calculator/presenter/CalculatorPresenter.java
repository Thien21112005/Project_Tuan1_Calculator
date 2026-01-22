//package com.example.calculator.presenter;
//
//import android.view.View;
//
//import com.example.calculator.CalculatorContract;
//import com.example.calculator.R;
//import com.example.calculator.model.CalculatorModel;
//
//public class CalculatorPresenter implements CalculatorContract.Presenter {
//
//    private final CalculatorContract.View view;
//    private final CalculatorModel model;
//
//    public CalculatorPresenter(CalculatorContract.View view) {
//        this.view = view;
//        this.model = new CalculatorModel();
//    }
//
//    @Override
//    public void onButtonClick(View v, String buttonText) {
//        int id = v.getId();
//
//        if (isNumberButton(id)) {
//            model.handleNumber(buttonText);
//        } else if (id == R.id.btnDot) {
//            model.handleDecimal();
//        } else if (isOperatorButton(id)) {
//            model.handleOperator(buttonText);
//        } else if (id == R.id.btnEquals) {
//            model.handleEquals();
//        } else if (id == R.id.btnClear) {
//            model.handleClear();
//        } else if (id == R.id.btnDelete) {
//            model.handleDelete();
//        } else if (id == R.id.btnPercent) {
//            model.handlePercent();
//        } else if (id == R.id.btnPlusMinus) {
//            model.handlePlusMinus();
//        } else if (isScientificButton(id)) {
//            model.handleScientific(getScientificFunction(id));
//        } else if (id == R.id.btnPi) {
//            model.handleConstant(Math.PI, "π");
//        } else if (id == R.id.btnE) {
//            model.handleConstant(Math.E, "e");
//        } else if (id == R.id.btnOpenParen || id == R.id.btnCloseParen) {
//            model.handleParenthesis(buttonText);
//        }
//
//        // Update UI sau mọi hành động
//        view.updateResult(model.getResultDisplay());
//        view.updateExpression(model.getExpression());
//
//        // Handle error nếu có (ví dụ chia 0)
//        if (model.getResultDisplay().equals("Error")) {
//            view.showError("Error");
//        }
//    }
//
//    private boolean isNumberButton(int id) {
//        return id >= R.id.btn0 && id <= R.id.btn9;
//    }
//
//    private boolean isOperatorButton(int id) {
//        return id == R.id.btnAdd || id == R.id.btnSubtract ||
//                id == R.id.btnMultiply || id == R.id.btnDivide;
//    }
//
//    private boolean isScientificButton(int id) {
//        return id == R.id.btnSin || id == R.id.btnCos || id == R.id.btnTan ||
//                id == R.id.btnLog || id == R.id.btnLn || id == R.id.btnPower ||
//                id == R.id.btnSqrt || id == R.id.btnFactorial;
//    }
//
//    // Fix lỗi constant expression: Dùng switch statement thay expression (tương thích Android tốt hơn)
//    private String getScientificFunction(int id) {
//        if (id == R.id.btnSin) return "sin";
//        if (id == R.id.btnCos) return "cos";
//        if (id == R.id.btnTan) return "tan";
//        if (id == R.id.btnLog) return "log";
//        if (id == R.id.btnLn) return "ln";
//        if (id == R.id.btnPower) return "power";
//        if (id == R.id.btnSqrt) return "sqrt";
//        if (id == R.id.btnFactorial) return "factorial";
//        return "";
//    }
//}