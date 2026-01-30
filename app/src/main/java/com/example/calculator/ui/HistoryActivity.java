package com.example.calculator.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calculator.HistoryContract;
import com.example.calculator.R;
import com.example.calculator.model.HistoryItem;
import com.example.calculator.model.HistoryModel;
import com.example.calculator.presenter.HistoryPresenter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HistoryActivity extends AppCompatActivity implements HistoryContract.View {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private TextView tvEmptyState;
    private FloatingActionButton fabClearAll;
    private HistoryContract.Presenter presenter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupMVP();
        setupRecyclerView();
        setupToolbar();

        presenter.loadHistory();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view_history);
        tvEmptyState = findViewById(R.id.tv_empty_state);
        fabClearAll = findViewById(R.id.fab_clear_all);

        fabClearAll.setOnClickListener(v -> showClearAllDialog());
    }

    private void setupMVP() {
        HistoryContract.Model model = new HistoryModel(this);
        presenter = new HistoryPresenter(model);
        presenter.attachView(this);
    }

    private void setupRecyclerView() {
        adapter = new HistoryAdapter(new HistoryAdapter.OnHistoryItemClickListener() {
            @Override
            public void onItemClick(HistoryItem item) {
                presenter.onHistoryItemClicked(item);
            }

            @Override
            public void onDeleteClick(HistoryItem item) {
                showDeleteDialog(item);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Lịch sử tính toán");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void showClearAllDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xóa tất cả lịch sử")
                .setMessage("Bạn có chắc chắn muốn xóa tất cả lịch sử tính toán?")
                .setPositiveButton("Xóa", (dialog, which) -> presenter.onClearAllClicked())
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showDeleteDialog(HistoryItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa lịch sử")
                .setMessage("Bạn có chắc chắn muốn xóa mục này?")
                .setPositiveButton("Xóa", (dialog, which) -> presenter.onDeleteItemClicked(item))
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public void showHistory(List<HistoryItem> historyItems) {
        recyclerView.setVisibility(View.VISIBLE);
        tvEmptyState.setVisibility(View.GONE);
        fabClearAll.setVisibility(View.VISIBLE);
        adapter.setHistoryItems(historyItems);
    }

    @Override
    public void showEmptyState() {
        recyclerView.setVisibility(View.GONE);
        tvEmptyState.setVisibility(View.VISIBLE);
        fabClearAll.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        tvEmptyState.setText(message);
        tvEmptyState.setVisibility(View.VISIBLE);
    }

    @Override
    public void navigateToCalculator(String expression, String result) {
        Intent intent = new Intent();
        intent.putExtra("expression", expression);
        intent.putExtra("result", result);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}