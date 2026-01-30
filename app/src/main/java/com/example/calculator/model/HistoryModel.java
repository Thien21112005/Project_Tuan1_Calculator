package com.example.calculator.model;

import android.content.Context;

import com.example.calculator.HistoryContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryModel implements HistoryContract.Model {
    private static final String FILE_NAME = "calculator_history.json";
    private List<HistoryItem> historyItems;
    private Context context;

    public HistoryModel(Context context) {
        this.context = context;
        this.historyItems = new ArrayList<>();
        loadFromFile();
    }

    @Override
    public List<HistoryItem> getHistory() {
        // Trả về danh sách đảo ngược để hiển thị mới nhất trước
        List<HistoryItem> reversedList = new ArrayList<>(historyItems);
        Collections.reverse(reversedList);
        return reversedList;
    }

    @Override
    public void addHistory(HistoryItem item) {
        historyItems.add(item);
        saveToFile();
    }

    @Override
    public void clearHistory() {
        historyItems.clear();
        saveToFile();
    }

    @Override
    public void deleteHistoryItem(HistoryItem item) {
        historyItems.removeIf(historyItem ->
                historyItem.getTimestamp() == item.getTimestamp() &&
                        historyItem.getExpression().equals(item.getExpression()));
        saveToFile();
    }

    @Override
    public void saveToFile() {
        try {
            JSONArray jsonArray = new JSONArray();
            for (HistoryItem item : historyItems) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("expression", item.getExpression());
                jsonObject.put("result", item.getResult());
                jsonObject.put("timestamp", item.getTimestamp());
                jsonArray.put(jsonObject);
            }

            File file = new File(context.getFilesDir(), FILE_NAME);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(jsonArray.toString().getBytes());
            fos.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadFromFile() {
        historyItems.clear();
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            if (!file.exists()) {
                return;
            }

            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            fis.close();

            String jsonString = stringBuilder.toString();
            if (jsonString.isEmpty()) {
                return;
            }

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String expression = jsonObject.getString("expression");
                String result = jsonObject.getString("result");
                long timestamp = jsonObject.getLong("timestamp");
                historyItems.add(new HistoryItem(expression, result, timestamp));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}