package com.example.calculator.presenter;

import com.example.calculator.CalculatorContract;

public class CalculatorPresenter implements CalculatorContract.Presenter {

    private CalculatorContract.View view;
    private final CalculatorContract.Model model;

    public CalculatorPresenter(CalculatorContract.Model model) {
        this.model = model;
    }

    @Override
    public void attachView(CalculatorContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void onNumberClicked(String number) {
        model.inputNumber(number);
        updateDisplay();
    }

    @Override
    public void onOperatorClicked(String operator) {
        model.inputOperator(operator);
        updateDisplay();
    }

    @Override
    public void onEqualClicked() {
        try {
            model.calculate();
            updateDisplay();

            if (model.isResultShown() && view != null) {
                view.showEquation(model.getCurrentExpression());
            }
        } catch (Exception e) {
            if (view != null) {
                view.showError("Lỗi biểu thức");
            }
        }
    }

    @Override
    public void onClearClicked() {
        model.clear();
        if (view != null) {
            view.showResult("0");
            view.showEquation("");
        }
    }

    @Override
    public void onDeleteClicked() {
        model.delete();
        updateDisplay();
    }

    @Override
    public void onPercentClicked(){
        model.percent();
        updateDisplay();
    }

    @Override
    public void onHistoryClicked(){
        model.history();
    }

    private void updateDisplay() {
        if (view != null) {
            view.showResult(model.getCurrentResult());

            if (!model.isResultShown()) {
                view.showEquation(model.getCurrentExpression());
            }
        }
    }
}