package com.happs.ximand.ringcontrol.model.object;

public class Lesson {

    private int number;
    @Deprecated
    private String startTimeDep;
    @Deprecated
    private String endTimeDep;

    private Time startTime;
    private Time endTime;

    public Lesson(int number, Time startTime, Time endTime) {
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Lesson(int number, String startTime, String endTime) {
        this.number = number;
        this.startTimeDep = startTime;
        this.endTimeDep = endTime;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    @Deprecated
    public String getStartTimeDep() {
        return startTimeDep;
    }

    @Deprecated
    public void setStartTimeDep(String startTimeDep) {
        this.startTimeDep = startTimeDep;
    }

    @Deprecated
    public String getEndTimeDep() {
        return endTimeDep;
    }

    @Deprecated
    public void setEndTimeDep(String endTimeDep) {
        this.endTimeDep = endTimeDep;
    }
}
