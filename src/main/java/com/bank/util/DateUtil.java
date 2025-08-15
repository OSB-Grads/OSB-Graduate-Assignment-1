package com.bank.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class DateUtil {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy, hh:mm a");

    public static String formatStringDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return "N/A";
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.parse(dateStr, parser);
        return ldt.format(FORMATTER);
    }

    public static String formatTimestamp(Timestamp ts) {
        if (ts == null) return "N/A";
        return ts.toLocalDateTime().format(FORMATTER);
    }
}
