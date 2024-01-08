package org.greatgamesonly.core.aurorasaver.utility;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.TimeZone;

import static java.time.ZoneOffset.UTC;

public class DateUtils {

    public static final String STANDARD_DATE_FORMAT = "yyyy-MM-dd hh:mm";
    public static final String STANDARD_DATE_FORMAT_NO_HOUR_MIN = "yyyy-MM-dd";
    public static final String STANDARD_DATE_FORMAT_NO_HOUR_MIN_ALT = "yyyy-M-d";

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String convertDateToString(Date date) {
        return new SimpleDateFormat(STANDARD_DATE_FORMAT).format(date);
    }

    public static Date convertStringToDate(String date) throws ParseException {
        return (Date) new SimpleDateFormat(STANDARD_DATE_FORMAT).parse(date);
    }

    public static Date convertStringToDateNoHour(String date) throws ParseException {
        return (Date) new SimpleDateFormat(STANDARD_DATE_FORMAT_NO_HOUR_MIN).parse(date);
    }

    public static String dateToStringNoHourMin(ZonedDateTime date) {
        return DateTimeFormatter.ofPattern(STANDARD_DATE_FORMAT_NO_HOUR_MIN).format(date);
    }

    public static String convertDateToString(long dateInMillis) {
        return convertDateToString(new Date(dateInMillis));
    }

    public static Date getTodayStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(UTC));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return (Date) calendar.getTime();
    }

    public static String getTodayStartDateDbTimestamp() {
        return simpleDateFormat.format(getTodayStartDate());
    }

    public static String convertDateToDbTimestamp(Date date) {
        return simpleDateFormat.format(date);
    }

    public static Date getTodayDateMinusYears(int minusYears) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(UTC));
        calendar.set(Calendar.YEAR, minusYears*-1);
        return (Date) calendar.getTime();
    }

    public static Date getTodayDateMinusMonths(int minusMonths) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(UTC));
        calendar.set(Calendar.MONTH, minusMonths*-1);
        return (Date) calendar.getTime();
    }

    public static Date getTomorrowStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(UTC));
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return (Date) calendar.getTime();
    }

    public static Date getMonthStartDate() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(UTC));
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return (Date) calendar.getTime();
    }

    public static String getMonthStartDateDbTimestamp() throws ParseException {
        return simpleDateFormat.format(getMonthStartDate());
    }

    public static Calendar nowCal() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(UTC));
        return calendar;
    }

    public static Calendar nowCal(int minusMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(UTC));
        calendar.add(Calendar.MINUTE, minusMinutes);
        return calendar;
    }

    public static Date now() {
        return (Date) nowCal().getTime();
    }

    public static java.sql.Timestamp nowDbTimestamp() {
        return new java.sql.Timestamp(nowCal().getTimeInMillis());
    }

    public static java.sql.Timestamp nowDbTimestamp(int minusMinutes) {
        return new java.sql.Timestamp(nowCal(minusMinutes).getTimeInMillis());
    }
}