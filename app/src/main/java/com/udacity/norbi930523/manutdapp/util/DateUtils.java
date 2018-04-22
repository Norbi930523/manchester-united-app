package com.udacity.norbi930523.manutdapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

}
