//package com.example.calculator.utils;
//
//import com.example.calculator.model.CalculatorConstants;
//
//public class NumberFormatter {
//
//    public static String format(double number) {
//        if (Double.isNaN(number)) return "Error";
//        if (Double.isInfinite(number)) return "∞";
//        if (Math.abs(number) < 0.000001 && number != 0) {
//            return String.format("%.6e", number);
//        }
//        String result = CalculatorConstants.DECIMAL_FORMAT.format(number);
//        if (result.contains(".")) {
//            result = result.replaceAll("0+$", "").replaceAll("\\.$", "");
//        }
//        return result;
//    }
//}