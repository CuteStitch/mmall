package com.mmall.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * @描述：时间转换工具
 * @作者：Stitch
 * @时间：2019/2/22 10:30
 */
public class DateTimeUtil {

    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 字符串--->时间
     *
     * @param dateTimeStr 时间字符串
     * @param formatStr   格式化格式
     * @return
     */
    public static Date strToDate(String dateTimeStr, String formatStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    /**
     * joda-time--->字符串
     *
     * @param date      时间
     * @param formatStr 字符串格式
     * @return
     */
    public static String dateToStr(Date date, String formatStr) {
        if (date == null) {
            return null;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formatStr);
    }


    /*------------重载方法------------*/

    /**
     * 字符串--->时间
     *
     * @param dateTimeStr 时间字符串
     * @return
     */
    public static Date strToDate(String dateTimeStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    /**
     * joda-time--->字符串
     *
     * @param date 时间
     * @return
     */
    public static String dateToStr(Date date) {
        if (date == null) {
            return null;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }
}
