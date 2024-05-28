package com.fpt.servicecontract.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DateUltil {
    private static Logger logger = LoggerFactory.getLogger(DateUltil.class);

    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss";
    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
    public static String DATE_FORMAT_EEE ="EEE MMM dd HH:mm:ss yyyy";
    public static final String DATE_FORMAT_MMM_dd_yyyy = "MMM dd, yyyy";
    public static final String DATE_FORMAT_MM_dd_yyyy = "MM dd, yyyy";
    public static final String DATE_FORMAT_dd_MM_yyyy = "dd/MM/yyyy";
    public static final String DATE_FORMAT_EXPORT_EXCEL = "yyyy-MM-dd_HH-mm-ss";
    public static Date stringToDate(String time, String format) {
        if (DataUtil.isNullObject(time) || DataUtil.isNullObject(format)) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            date = null;
        }
        return date;
    }

    public static Date longToDate(long time) {
        if (time < 1) {
            return null;
        }

        try {
            return new Date(time);
        } catch (Exception e) {

        }
        return null;
    }

    public static Date getStartDayOfMonth(){
        Calendar calendar = Calendar.getInstance(new Locale("en", "US"));
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    public static Date getEndDayOfMonth(){
        Calendar calendar = Calendar.getInstance(new Locale("en", "US"));
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.MILLISECOND, 59);
        return calendar.getTime();
    }

    public static Date stringToDateStartDay(Date date) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance(new Locale("en", "US"));
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        }
        return null;
    }

    public static Date addDateByDay(Date date, int day) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_WEEK, day);
            return calendar.getTime();
        }
        return null;
    }

    public static Date stringToDateEndDay(Date date) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance(new Locale("en", "US"));
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.MILLISECOND, 59);
            return calendar.getTime();
        }
        return null;
    }
}
