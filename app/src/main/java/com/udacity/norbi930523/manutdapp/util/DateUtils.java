package com.udacity.norbi930523.manutdapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import timber.log.Timber;

public class DateUtils {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private DateUtils(){}

    public static long parseDate(String dateStr){
        try {
            return DATE_FORMAT.parse(dateStr).getTime();
        } catch (ParseException e) {
            Timber.e(e);
            return 0L;
        }
    }

    public static String formatDate(long millis){
        return DATE_FORMAT.format(new Date(millis));
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
