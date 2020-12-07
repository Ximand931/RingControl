package com.happs.ximand.ringcontrol.viewmodel.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class TimeHelper {

    private static final String DATE_TIME_PATTERN = "H:m:s";

    public static String getPreviewTime(String time) {
        String simplifiedTime = time;
        if (time.length() == 8) {
            if (time.substring(6, 8).equals("00")) {
                simplifiedTime = simplifiedTime.substring(0, 5);
            }
            if (time.charAt(0) == '0') {
                simplifiedTime = simplifiedTime.substring(1);
            }
        }
        return simplifiedTime;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getCurrentTimeWithFewMinutes(int minutes) {
        final SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_PATTERN);

        long currentDateInMillis = new Date().getTime();
        Date dateAfterFewMinutes = new Date(currentDateInMillis + minutes * 60 * 1000);

        return format.format(dateAfterFewMinutes);
    }

}
