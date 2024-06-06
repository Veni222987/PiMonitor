package org.pi.server.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author hu1hu
 */
public class TimeUtils {
    /**
     * 获取当前时间
     * @return 当前时间
     */
    public static String getDateTimeNow() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    /**
     * @description 格式化时间
     * @param time 时间
     * @return java.lang.String
     */
    public static String format(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return time.format(formatter);
    }
}
