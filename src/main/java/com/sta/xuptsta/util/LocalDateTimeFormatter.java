package com.sta.xuptsta.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeFormatter {
    public static String dateTimeToString(LocalDateTime dateTime){
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM月dd日HH:mm:ss");

        // 将 LocalDateTime 转换为 String
        String formattedDateTime = dateTime.format(formatter);

        return formattedDateTime;
    }
}
