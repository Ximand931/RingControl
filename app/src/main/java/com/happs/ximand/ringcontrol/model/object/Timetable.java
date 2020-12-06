package com.happs.ximand.ringcontrol.model.object;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Timetable implements Serializable {

    private int id;
    private String title;
    @Deprecated
    private List<String> timeList;
    private List<Lesson> lessons;

    public Timetable(String title, List<Lesson> lessons) {
        this.title = title;
        this.lessons = lessons;
    }

    public Timetable(int id, String title, List<Lesson> lessons) {
        this.id = id;
        this.title = title;
        this.lessons = lessons;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Deprecated
    public List<String> getTimeList() {
        return timeList;
    }

    @Deprecated
    public void setTimeList(List<String> timeList) {
        this.timeList = timeList;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Timetable timetable = (Timetable) o;
        return Objects.equals(title, timetable.title) &&
                Objects.equals(timeList, timetable.timeList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, timeList);
    }

    @NonNull
    @Override
    public String toString() {
        return "Timetable{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", timeList=" + timeList +
                '}';
    }
}
