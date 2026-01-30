package com.example.calculator.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calculator.R;
import com.example.calculator.model.HistoryItem;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<HistoryItem> historyItems = new ArrayList<>();
    private OnHistoryItemClickListener listener;

    public interface OnHistoryItemClickListener {
        void onItemClick(HistoryItem item);
        void onDeleteClick(HistoryItem item);
    }

    public HistoryAdapter(OnHistoryItemClickListener listener) {
        this.listener = listener;
    }

    public void setHistoryItems(List<HistoryItem> items) {
        this.historyItems = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryItem item = historyItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return historyItems.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tvExpression;
        private TextView tvResult;
        private TextView tvTime;
        private ImageButton btnDelete;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExpression = itemView.findViewById(R.id.tv_history_expression);
            tvResult = itemView.findViewById(R.id.tv_history_result);
            tvTime = itemView.findViewById(R.id.tv_history_time);
            btnDelete = itemView.findViewById(R.id.btn_delete_history);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(historyItems.get(position));
                }
            });

            btnDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onDeleteClick(historyItems.get(position));
                }
            });
        }

        public void bind(HistoryItem item) {
            tvExpression.setText(item.getExpression());
            tvResult.setText("= " + item.getResult());
            tvTime.setText(item.getFormattedTime());
        }
    }
}