package com.example.calculator;

import com.example.calculator.model.HistoryItem;

import java.util.List;

public interface HistoryContract {

    interface View {
        void showHistory(List<HistoryItem> historyItems);
        void showEmptyState();
        void showError(String message);
        void navigateToCalculator(String expression, String result);
    }

    interface Presenter {
        void loadHistory();
        void onHistoryItemClicked(HistoryItem item);
        void onClearAllClicked();
        void onDeleteItemClicked(HistoryItem item);
        void attachView(View view);
        void detachView();
    }

    interface Model {
        List<HistoryItem> getHistory();
        void addHistory(HistoryItem item);
        void clearHistory();
        void deleteHistoryItem(HistoryItem item);
        void saveToFile();
        void loadFromFile();
    }
}