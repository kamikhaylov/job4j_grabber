package ru.job4j.utils;

import org.junit.Test;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SqlRuDateTimeParserTest {
    @Test
    public void whenDateSqlRuTodayThenDateFormat() {
        LocalDateTime dateTime = LocalDateTime.now();
        SqlRuDateTimeParser sqlRuDateTimeParser = new SqlRuDateTimeParser();
        DateTimeFormatter formatter = sqlRuDateTimeParser.getFormatter();
        String date = "сегодня, " + LocalTime.of(dateTime.getHour(), dateTime.getMinute());
        String result = sqlRuDateTimeParser.parse(date).format(formatter);
        String expected = dateTime.format(formatter);
        assertThat(result, is(expected));
    }

    @Test
    public void whenDateSqlRuYesterdayThenDateFormat() {
        LocalDateTime dateTime = LocalDateTime.now();
        SqlRuDateTimeParser sqlRuDateTimeParser = new SqlRuDateTimeParser();
        DateTimeFormatter formatter = sqlRuDateTimeParser.getFormatter();
        String date = "вчера, " + LocalTime.of(dateTime.getHour(), dateTime.getMinute());
        String result = sqlRuDateTimeParser.parse(date).format(formatter);
        String expected = dateTime.minusDays(1).format(formatter);
        assertThat(result, is(expected));
    }

    @Test
    public void whenDateNotFirstSymbolThenJavaDateFormat() {
        SqlRuDateTimeParser sqlRuDateTimeParser = new SqlRuDateTimeParser();
        DateTimeFormatter formatter = sqlRuDateTimeParser.getFormatter();
        String date = "2 дек 19, 22:30";
        String result = "02 дек. 19, 22:30";
        String expected = sqlRuDateTimeParser.parse(date).format(formatter);
        assertThat(result, is(expected));
    }

    @Test
    public void whenDateNotPointInMonthThenJavaDateFormat() {
        SqlRuDateTimeParser sqlRuDateTimeParser = new SqlRuDateTimeParser();
        DateTimeFormatter formatter = sqlRuDateTimeParser.getFormatter();
        String date = "02 фев 19, 22:30";
        String result = "02 февр. 19, 22:30";
        String expected = sqlRuDateTimeParser.parse(date).format(formatter);
        assertThat(result, is(expected));
    }

    @Test
    public void whenSqlRuMonthThenDateFormatString() {
        SqlRuDateTimeParser sqlRuDateTimeParser = new SqlRuDateTimeParser();
        DateTimeFormatter formatter = sqlRuDateTimeParser.getFormatter();
        List<String> expected = List.of(
                sqlRuDateTimeParser.parse("01 янв 19, 22:30").format(formatter),
                sqlRuDateTimeParser.parse("01 фев 19, 22:30").format(formatter),
                sqlRuDateTimeParser.parse("01 мар 19, 22:30").format(formatter),
                sqlRuDateTimeParser.parse("01 апр 19, 22:30").format(formatter),
                sqlRuDateTimeParser.parse("01 май 19, 22:30").format(formatter),
                sqlRuDateTimeParser.parse("01 июн 19, 22:30").format(formatter),
                sqlRuDateTimeParser.parse("01 июл 19, 22:30").format(formatter),
                sqlRuDateTimeParser.parse("01 авг 19, 22:30").format(formatter),
                sqlRuDateTimeParser.parse("01 сен 19, 22:30").format(formatter),
                sqlRuDateTimeParser.parse("01 окт 19, 22:30").format(formatter),
                sqlRuDateTimeParser.parse("01 ноя 19, 22:30").format(formatter),
                sqlRuDateTimeParser.parse("01 дек 19, 22:30").format(formatter)
        );
        List<String> result = List.of(
                "01 янв. 19, 22:30",
                "01 февр. 19, 22:30",
                "01 мар. 19, 22:30",
                "01 апр. 19, 22:30",
                "01 мая 19, 22:30",
                "01 июн. 19, 22:30",
                "01 июл. 19, 22:30",
                "01 авг. 19, 22:30",
                "01 сент. 19, 22:30",
                "01 окт. 19, 22:30",
                "01 нояб. 19, 22:30",
                "01 дек. 19, 22:30"
        );
        assertThat(result, is(expected));
    }

    @Test
    public void whenSqlRuMonthThenDateFormatLocalDateTime() {
        SqlRuDateTimeParser sqlRuDateTimeParser = new SqlRuDateTimeParser();
        List<LocalDateTime> expected = List.of(
                sqlRuDateTimeParser.parse("1 янв 19, 22:30"),
                sqlRuDateTimeParser.parse("2 фев 19, 22:30"),
                sqlRuDateTimeParser.parse("3 мар 19, 22:30"),
                sqlRuDateTimeParser.parse("4 апр 19, 22:30"),
                sqlRuDateTimeParser.parse("5 май 19, 22:30"),
                sqlRuDateTimeParser.parse("6 июн 19, 22:30"),
                sqlRuDateTimeParser.parse("7 июл 19, 22:30"),
                sqlRuDateTimeParser.parse("8 авг 19, 22:30"),
                sqlRuDateTimeParser.parse("9 сен 19, 22:30"),
                sqlRuDateTimeParser.parse("10 окт 19, 22:30"),
                sqlRuDateTimeParser.parse("11 ноя 19, 22:30"),
                sqlRuDateTimeParser.parse("12 дек 19, 22:30")
        );
        List<LocalDateTime> result = List.of(
                LocalDateTime.of(2019, 1, 1, 22, 30),
                LocalDateTime.of(2019, 2, 2, 22, 30),
                LocalDateTime.of(2019, 3, 3, 22, 30),
                LocalDateTime.of(2019, 4, 4, 22, 30),
                LocalDateTime.of(2019, 5, 5, 22, 30),
                LocalDateTime.of(2019, 6, 6, 22, 30),
                LocalDateTime.of(2019, 7, 7, 22, 30),
                LocalDateTime.of(2019, 8, 8, 22, 30),
                LocalDateTime.of(2019, 9, 9, 22, 30),
                LocalDateTime.of(2019, 10, 10, 22, 30),
                LocalDateTime.of(2019, 11, 11, 22, 30),
                LocalDateTime.of(2019, 12, 12, 22, 30)
        );
        assertThat(result, is(expected));
    }

    @Test
    public void whenDateSqlRuTodayAndYesterdayThenDateFormatLocalDateTime() {
        LocalDateTime dateTime = LocalDateTime.now();
        SqlRuDateTimeParser sqlRuDateTimeParser = new SqlRuDateTimeParser();
        List<LocalDateTime> expected = List.of(
                sqlRuDateTimeParser.parse("сегодня, " + LocalTime
                        .of(dateTime.getHour(), dateTime.getMinute())),
                sqlRuDateTimeParser.parse("вчера, " + LocalTime
                        .of(dateTime.getHour(), dateTime.getMinute()))
        );
        List<LocalDateTime> result = List.of(
                LocalDateTime.of(
                        dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(),
                        dateTime.getHour(), dateTime.getMinute()),
                LocalDateTime.of(
                        dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth() - 1,
                        dateTime.getHour(), dateTime.getMinute())
                );
        assertThat(result, is(expected));
    }
}