package com.app.utils;

public final class PathVariableReader {

    private static final int BEGIN_INDEX = 1;
    private static final int END_INDEX = 4;


    public static String extractBaseCurrency(String pathInfo) {
        return pathInfo.substring(BEGIN_INDEX, END_INDEX);
    }

    public static String extractTargetCurrency(String pathInfo) {
        return pathInfo.substring(END_INDEX);
    }
}
