package com.happs.ximand.ringcontrol.model.object;

public class Lesson {

    private int number;
    private String startTime;
    private String endTime;

    public Lesson(int number, String startTime, String endTime) {
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
