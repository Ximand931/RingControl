package com.happs.ximand.ringcontrol.model.object;

public class LessonScope {

    private Time startTime;
    private Time endTime;

    public LessonScope(Time startTime, Time endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

}
