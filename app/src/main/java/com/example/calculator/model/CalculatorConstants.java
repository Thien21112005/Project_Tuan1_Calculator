package com.example.calculator.model;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CalculatorConstants {

    public static final String ADD = "+";
    public static final String SUBTRACT = "−";
    public static final String MULTIPLY = "×";
    public static final String DIVIDE = "÷";

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##########");

    public static final Map<String, BiFunction<Double, Double, Double>> OPERATION_MAP = new HashMap<>() {{
        put(ADD, Double::sum);
        put(SUBTRACT, (a, b) -> a - b);
        put(MULTIPLY, (a, b) -> a * b);
        put(DIVIDE, (a, b) -> b != 0 ? a / b : Double.NaN);
    }};

    public static final Map<String, Function<Double, Double>> SCIENTIFIC_MAP = new HashMap<>() {{
        put("sin", v -> Math.sin(Math.toRadians(v)));
        put("cos", v -> Math.cos(Math.toRadians(v)));
        put("tan", v -> Math.tan(Math.toRadians(v)));
        put("log", v -> v > 0 ? Math.log10(v) : Double.NaN);
        put("ln", v -> v > 0 ? Math.log(v) : Double.NaN);
        put("power", v -> v * v);
        put("sqrt", v -> v >= 0 ? Math.sqrt(v) : Double.NaN);
        put("factorial", v -> {
            if (v >= 0 && v <= 20 && v == Math.floor(v)) {
                return (double) factorial((int) Math.floor(v));  // cast an toàn
            }
            return Double.NaN;
        });
    }};

    private static long factorial(int n) {
        if (n < 0 || n > 20) return Long.MAX_VALUE;
        return (n <= 1) ? 1L : n * factorial(n - 1);
    }
}