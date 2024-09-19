package com.sta.xuptsta.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeFormatter {
    public static String startDateTimeToString(LocalDateTime dateTime){
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M月d日 H:mm");

        // 将 LocalDateTime 转换为 String
        String formattedDateTime = dateTime.format(formatter);

        return formattedDateTime;
    }
    public static String endDateTimeToString(LocalDateTime dateTime){
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");

        // 将 LocalDateTime 转换为 String
        String formattedDateTime = dateTime.format(formatter);

        return formattedDateTime;
    }
}
