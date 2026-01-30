package com.example.calculator.presenter;

import com.example.calculator.HistoryContract;
import com.example.calculator.model.HistoryItem;

import java.util.List;

public class HistoryPresenter implements HistoryContract.Presenter {
    private HistoryContract.View view;
    private final HistoryContract.Model model;

    public HistoryPresenter(HistoryContract.Model model) {
        this.model = model;
    }

    @Override
    public void attachView(HistoryContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void loadHistory() {
        if (view == null) return;

        List<HistoryItem> historyItems = model.getHistory();
        if (historyItems.isEmpty()) {
            view.showEmptyState();
        } else {
            view.showHistory(historyItems);
        }
    }

    @Override
    public void onHistoryItemClicked(HistoryItem item) {
        if (view != null) {
            view.navigateToCalculator(item.getExpression(), item.getResult());
        }
    }

    @Override
    public void onClearAllClicked() {
        model.clearHistory();
        if (view != null) {
            view.showEmptyState();
        }
    }

    @Override
    public void onDeleteItemClicked(HistoryItem item) {
        model.deleteHistoryItem(item);
        loadHistory();
    }
}