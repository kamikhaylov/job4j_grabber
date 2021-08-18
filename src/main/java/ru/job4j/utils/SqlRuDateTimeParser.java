package ru.job4j.utils;

import static java.util.Map.entry;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

public class SqlRuDateTimeParser implements DateTimeParser {
    private static final Map<String, String> MONTHS = Map.ofEntries(
            entry("янв", "янв."),
            entry("фев", "февр."),
            entry("мар", "мар."),
            entry("апр", "апр."),
            entry("май", "мая"),
            entry("июн", "июн."),
            entry("июл", "июл."),
            entry("авг", "авг."),
            entry("сен", "сент."),
            entry("окт", "окт."),
            entry("ноя", "нояб."),
            entry("дек", "дек.")
    );
    private Locale locale = new Locale("ru");
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yy, HH:mm", locale);

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
        } else {
            dateStr = convertMonth(dateStr);
        }
        return LocalDateTime.parse(dateStr, getFormatter());
    }

    private String convertTodayToDate(String date) {
        String dateStr = date.substring(7);
        return getDateToString(dateStr, LocalDate.now());
    }

    private String convertYesterdayToDate(String date) {
        String dateStr = date.substring(5);
        return getDateToString(dateStr, LocalDate.now().minusDays(1));
    }

    private String convertMonth(String date) {
        date = addFirstSymbol(date);
        StringBuilder sb = new StringBuilder(date);
        return sb.replace(3, 6, MONTHS.get(date.substring(3, 6))).toString();
    }

    private String getDateToString(String result, LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yy", locale);
        StringBuilder sb = new StringBuilder(result);
        return sb.insert(0, localDate.format(formatter)).toString();
    }

    private String addFirstSymbol(String date) {
        String day = date.substring(0, date.indexOf(" "));
        String result = date;
        if (day.length() != 2) {
            StringBuilder sb = new StringBuilder(date);
            result = sb.insert(0, "0").toString();
        }
        return result;
    }
}