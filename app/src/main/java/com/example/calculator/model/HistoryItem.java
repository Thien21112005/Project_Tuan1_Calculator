package com.example.calculator.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HistoryItem implements Serializable {
    private String expression;
    private String result;
    private long timestamp;
    private String formattedTime;

    public HistoryItem(String expression, String result) {
        this.expression = expression;
        this.result = result;
        this.timestamp = System.currentTimeMillis();
        this.formattedTime = formatTimestamp(timestamp);
    }

    // Constructor for JSON deserialization
    public HistoryItem(String expression, String result, long timestamp) {
        this.expression = expression;
        this.result = result;
        this.timestamp = timestamp;
        this.formattedTime = formatTimestamp(timestamp);
    }

    private String formatTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        this.formattedTime = formatTimestamp(timestamp);
    }

    public String getFormattedTime() {
        return formattedTime;
    }

    @Override
    public String toString() {
        return expression + " = " + result;
    }
}