package com.udacity.norbi930523.manutdapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import timber.log.Timber;

public class DateUtils {

    public static final long MILLIS_IN_HOUR = 1000 * 60 * 60;

    private static final SimpleDateFormat DATE_PARSER;
    private static final SimpleDateFormat DATE_FORMATTER;

    static {
        String pattern = "dd/MM/yyyy HH:mm";

        DATE_FORMATTER = new SimpleDateFormat(pattern);

        DATE_PARSER = new SimpleDateFormat(pattern);
        DATE_PARSER.setTimeZone(TimeZone.getTimeZone("Europe/London"));
    }

    private DateUtils(){}

    public static long parseDate(String dateStr){
        try {
            /* Parses the date in British time zone,
            *  returns (and stores) the date in the user's local time zone */
            return DATE_PARSER.parse(dateStr).getTime();
        } catch (ParseException e) {
            Timber.e(e);
            return 0L;
        }
    }

    public static String formatDate(long millis){
        return DATE_FORMATTER.format(new Date(millis));
    }

    public static Integer getMonth(long millis){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        return calendar.get(Calendar.MONTH);
    }

    public static long getStartOfMonth(long time){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    public static long getEndOfMonth(long time){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        calendar.add(Calendar.MONTH, 1);

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis() - 1;
    }

}
