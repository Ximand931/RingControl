package com.happs.ximand.ringcontrol.model.object;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Timetable implements Serializable {

    private int id;
    private String title;
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
        return id == timetable.id &&
                Objects.equals(title, timetable.title) &&
                Objects.equals(lessons, timetable.lessons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, lessons);
    }

    @NonNull
    @Override
    public String toString() {
        return "Timetable{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", lessons=" + lessons +
                '}';
    }
}
