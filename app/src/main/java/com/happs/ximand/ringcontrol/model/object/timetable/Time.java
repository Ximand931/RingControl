package com.happs.ximand.ringcontrol.model.object.timetable;

import androidx.annotation.NonNull;

public class Time {

    private int hours;
    private int minutes;
    private int seconds;
    private String detailedTime;

    public Time(int hours, int minutes, int seconds) {
        setTime(hours, minutes, seconds);
        updateDetailedTime();
    }

    public Time(String detailedTime) {
        int hours = Integer.parseInt(detailedTime.substring(0, 2));
        int minutes = Integer.parseInt(detailedTime.substring(3, 5));
        int seconds = Integer.parseInt(detailedTime.substring(6, 8));
        setTime(hours, minutes, seconds);
        updateDetailedTime();
    }

    public static Time getMidnightTime() {
        return new Time(0, 0, 0);
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setTime(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    @NonNull
    @Override
    public String toString() {
        return detailedTime;
    }

    private void updateDetailedTime() {
        StringBuilder timeBuilder = new StringBuilder();
        addOnePartOfTime(hours, timeBuilder);
        timeBuilder.append(":");
        addOnePartOfTime(minutes, timeBuilder);
        timeBuilder.append(":");
        addOnePartOfTime(seconds, timeBuilder);
        this.detailedTime = timeBuilder.toString();
    }

    private void addOnePartOfTime(int val, StringBuilder timeBuilder) {
        addZeroIfNecessary(val, timeBuilder);
        timeBuilder.append(val);
    }

    private void addZeroIfNecessary(int val, StringBuilder timeBuilder) {
        if (val < 10) {
            timeBuilder.append("0");
        }
    }

}
