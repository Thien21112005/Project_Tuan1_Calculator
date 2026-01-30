package com.example.calculator;

public interface CalculatorContract {

    interface View {
        void showResult(String result);
        void showEquation(String equation);
        void showError(String message);
    }

    interface Presenter {
        void onNumberClicked(String number);
        void onOperatorClicked(String operator);
        void onEqualClicked();
        void onClearClicked();
        void onDeleteClicked();
        void onPercentClicked();
        void onHistoryClicked();
        void attachView(View view);
        void detachView();
    }

    interface Model {
        void inputNumber(String number);
        void inputOperator(String operator);
        void percent();
        void calculate();
        void clear();
        void delete();
        void history();

        String getCurrentExpression();
        String getCurrentResult();
        boolean isResultShown();
    }
}