package com.happs.ximand.ringcontrol.viewmodel.util;

import android.annotation.SuppressLint;

import com.happs.ximand.ringcontrol.model.object.Time;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class TimeUtils {

    public static final String SIMPLE_TIME_PATTERN =
            "(?:[01]\\d|2[0123]):(?:[012345]\\d) – (?:[01]\\d|2[0123]):(?:[012345]\\d)";
    public static final String DETAILED_TIME_PATTERN =
            "(?:[01]\\d|2[0123]):(?:[012345]\\d):(?:[012345]\\d) " +
                    "– (?:[01]\\d|2[0123]):(?:[012345]\\d):(?:[012345]\\d)";
    public static final String SIMPLE_TIME_MASK = "ss:ss – ss:ss";
    public static final String DETAILED_TIME_MASK = "ss:ss:ss – ss:ss:ss";
    private static final String DATE_TIME_PATTERN = "H:m:s";

    private TimeUtils() {
    }

    @SuppressLint("SimpleDateFormat")
    public static Time getCurrentTimeWithFewMinutes(int minutes) {
        final SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_PATTERN);

        long currentDateInMillis = new Date().getTime();
        Date dateAfterFewMinutes = new Date(currentDateInMillis + minutes * 60 * 1000);

        return new Time(format.format(dateAfterFewMinutes));
    }

}
