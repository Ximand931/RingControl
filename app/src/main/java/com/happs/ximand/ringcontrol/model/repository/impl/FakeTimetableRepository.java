package com.happs.ximand.ringcontrol.model.repository.impl;

import android.util.Log;

import com.happs.ximand.ringcontrol.model.object.Lesson;
import com.happs.ximand.ringcontrol.model.object.Timetable;
import com.happs.ximand.ringcontrol.model.repository.Repository;
import com.happs.ximand.ringcontrol.model.specification.SqlSpecification;

import java.util.ArrayList;
import java.util.List;

public final class FakeTimetableRepository implements Repository<Timetable> {

    private static FakeTimetableRepository instance;

    private List<Timetable> timetables;

    private FakeTimetableRepository() {
        this.timetables = new ArrayList<Timetable>() {{
            add(new Timetable(0, "Стандартное расписание", new ArrayList<Lesson>() {{
                add(new Lesson(1, "08:30:00", "09:15:00"));
                add(new Lesson(2, "09:25:00", "10:10:00"));
            }}));
            /*
            add(new Timetable(1, "Сокращенное расписание", new ArrayList<Lesson>() {{
                add(new Lesson(1, "08:30:00", "09:15:00"));
                add(new Lesson(2, "09:25:00", "10:10:00"));
                add(new Lesson(3, "10:20:00", "11:05:00"));
            }}));
            add(new Timetable(2, "Полное расписание", new ArrayList<Lesson>() {{
                add(new Lesson(1, "08:30:00", "09:15:00"));
                add(new Lesson(2, "09:25:00", "10:10:00"));
                add(new Lesson(3, "10:20:00", "11:05:00"));
                add(new Lesson(4, "11:25:00", "12:10:00"));
                add(new Lesson(5, "12:20:00", "13:05:00"));
                add(new Lesson(6, "13:25:00", "14:10:00"));
                add(new Lesson(7, "14:20:00", "15:05:00"));
            }}));
            add(new Timetable(3, "Стандартное расписание 2", new ArrayList<Lesson>() {{
                add(new Lesson(1, "08:30:01", "09:15:00"));
                add(new Lesson(2, "09:25:00", "10:10:00"));
                add(new Lesson(3, "10:20:30", "11:05:00"));
                add(new Lesson(4, "11:25:00", "12:10:00"));
            }}));
            add(new Timetable(4, "Стандартное расписание 3", new ArrayList<Lesson>() {{
                add(new Lesson(1, "08:30:00", "09:15:00"));
                add(new Lesson(2, "09:25:00", "10:10:00"));
                add(new Lesson(3, "10:20:00", "11:05:00"));
            }}));
            add(new Timetable(5, "Стандартное расписание 4", new ArrayList<Lesson>() {{
                add(new Lesson(1, "08:30:30", "09:15:30"));
                add(new Lesson(2, "09:25:30", "10:10:30"));
                add(new Lesson(3, "10:20:30", "11:05:30"));
                add(new Lesson(4, "11:25:30", "12:10:30"));
            }}));

             */
        }};
    }

    public static FakeTimetableRepository getInstance() {
        if (instance == null) {
            throw new NullPointerException("Instance of timetable repository " +
                    "was not initialized");
        }
        return instance;
    }

    public synchronized static void initialize() {
        if (instance == null) {
            instance = new FakeTimetableRepository();
        } else {
            throw new RuntimeException("Instance already was initialized");
        }
    }

    @Override
    public void add(Timetable item) {
        timetables.add(item);
    }

    @Override
    public void add(Iterable<Timetable> items) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Timetable item) {
        timetables.set((int) item.getId(), item);
        Log.d(getClass().getSimpleName(),
                "Repository updated, new timetable list: \n" + listToString());
    }

    @Override
    public void remove(Timetable item) {
        timetables.remove(item);
    }

    @Override
    public void remove(SqlSpecification sqlSpecification) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Timetable> query(SqlSpecification sqlSpecification) {
        return timetables;
    }

    private String listToString() {
        StringBuilder builder = new StringBuilder();
        for (Timetable timetable : timetables) {
            builder.append(timetable).append("\n");
        }
        return builder.toString();
    }
}
