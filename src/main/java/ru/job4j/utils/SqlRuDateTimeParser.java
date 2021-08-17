package ru.job4j.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SqlRuDateTimeParser implements DateTimeParser {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yy, HH:mm");

    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    @Override
    public LocalDateTime parse(String parse) {
        String dateStr = parse;
        if (dateStr.startsWith("сегодня,")) {
            dateStr = convertTodayToDate(dateStr);
        } else if (dateStr.startsWith("вчера,")) {
            dateStr = convertYesterdayToDate(dateStr);
        } else if (dateStr.contains("фев")) {
            dateStr = convertFebToDate(dateStr);
        } else if (dateStr.contains("май")) {
            dateStr = convertMayToDate(dateStr);
        } else if (dateStr.contains("сен")) {
            dateStr = convertSepToDate(dateStr);
        } else if (dateStr.contains("ноя")) {
            dateStr = convertNovToDate(dateStr);
        } else {
            dateStr = addFirstSymbol(dateStr);
            dateStr = addPointInMonth(dateStr, 6);
        }
        LocalDateTime result = LocalDateTime.parse(dateStr, getFormatter());
        return result;
    }

    private String convertTodayToDate(String date) {
        String dateStr = date.substring(7);
        LocalDate localDate = LocalDate.now();
        return getDateToString(dateStr, localDate);
    }

    private String convertYesterdayToDate(String date) {
        String dateStr = date.substring(5);
        LocalDate localDate = LocalDate.now().minusDays(1);
        return getDateToString(dateStr, localDate);
    }

    private String convertFebToDate(String date) {
        StringBuffer sb = new StringBuffer(addFirstSymbol(date));
        return sb.insert(6, "р.").toString();
    }

    private String convertMayToDate(String date) {
        StringBuffer sb = new StringBuffer(addFirstSymbol(date));
        return sb.replace(5, 6, "я") .toString();
    }

    private String convertSepToDate(String date) {
        StringBuffer sb = new StringBuffer(addFirstSymbol(date));
        return sb.insert(6, "т.").toString();
    }

    private String convertNovToDate(String date) {
        StringBuffer sb = new StringBuffer(addFirstSymbol(date));
        return sb.insert(6, "б.").toString();
    }

    private String getDateToString(String result, LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yy");
        String todayFormat = localDate.format(formatter);
        StringBuffer sb = new StringBuffer(result);
        result = sb.insert(0, todayFormat).toString();
        return result;
    }

    private String addFirstSymbol(String date) {
        String day = date.substring(0, date.indexOf(" "));
        String result = date;
        if (day.length() != 2) {
            StringBuffer sb = new StringBuffer(date);
            result = sb.insert(0, "0").toString();
        }
        return result;
    }

    private String addPointInMonth(String date, int indexPoint) {
        StringBuffer sb = new StringBuffer(date);
        return sb.insert(indexPoint, ".").toString();
    }
}