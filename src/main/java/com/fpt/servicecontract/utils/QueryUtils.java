package com.fpt.servicecontract.utils;

import com.google.gson.Gson;

import java.util.List;
import java.util.stream.Collectors;

public class QueryUtils {
    public static String format(String input) {
        if (DataUtil.isNullObject(input)) {
            return null;
        }
        return input.trim();
    }

    public static String appendPercent(String input) {
        if (DataUtil.isNullObject(input)) {
            return null;
        }
        return "%" + input.trim().toLowerCase() + "%";
    }

    public static List<String> formatList(List<String> input) {
        if (DataUtil.isNullObject(input)) {
            return null;
        }
        return input.stream().map(String::trim).collect(Collectors.toList());
    }

    public static String getGson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static String formatKeywordLike(String input){
        if (DataUtil.isNullObject(input) || input.isEmpty()) return null;
        return "%"+input.toLowerCase()+"%";
    }

    public static String formatToLowerCase(String input){
        if (DataUtil.isNullObject(input) || input.isEmpty()) return null;
        return input.toLowerCase().trim();
    }
}
