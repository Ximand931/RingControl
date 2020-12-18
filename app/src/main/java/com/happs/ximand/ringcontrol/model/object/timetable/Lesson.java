package com.happs.ximand.ringcontrol.model.object.timetable;

public class Lesson {

    private int number;
    private LessonScope lessonScope;

    public Lesson(int number) {
        this.number = number;
        this.lessonScope = new LessonScope(Time.getMidnightTime(), Time.getMidnightTime());
    }

    public Lesson(int number, Time startTime, Time endTime) {
        this.number = number;
        this.lessonScope = new LessonScope(startTime, endTime);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Time getStartTime() {
        return lessonScope.getStartTime();
    }

    public void setStartTime(Time startTime) {
        this.lessonScope.setStartTime(startTime);
    }

    public Time getEndTime() {
        return lessonScope.getEndTime();
    }

    public void setEndTime(Time endTime) {
        this.lessonScope.setEndTime(endTime);
    }

}
