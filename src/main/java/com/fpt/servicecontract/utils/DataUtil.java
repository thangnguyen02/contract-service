package com.fpt.servicecontract.utils;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class DataUtil {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("MMddhhmmss");
    private static final Gson gson = new Gson();

    public static boolean isNullObject(Object obj1) {
        if (obj1 == null) {
            return true;
        } else if (obj1 instanceof String) {
            return isNullOrEmpty(obj1.toString());
        } else if (obj1 instanceof List) {
            return isListNullOrEmpty(obj1);
        } else if (obj1 instanceof Map) {
            return isMapNullOrEmpty(obj1);
        } else {
            return obj1 instanceof Object[] && isArrayNullOrEmpty(obj1);
        }
    }

    public static boolean isNullOrEmpty(String obj1) {
        return obj1 == null || obj1.toString().trim().equals("");
    }

    public static boolean isListNullOrEmpty(Object obj1) {
        return obj1 == null || ((List)obj1).isEmpty();
    }

    public static boolean isMapNullOrEmpty(final Object map) {
        return map == null || ((Map)map).isEmpty();
    }

    public static boolean isArrayNullOrEmpty(Object obj1) {
        return obj1 == null || ((Object[])obj1).length == 0;
    }

    public static String safeToString(Object obj) {
        if (DataUtil.isNullObject(obj)) {
            return "";
        }
        return obj.toString();
    }
}
