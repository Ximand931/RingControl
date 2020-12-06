package com.happs.ximand.ringcontrol.model.object.command;

import com.happs.ximand.ringcontrol.model.object.Lesson;
import com.happs.ximand.ringcontrol.model.object.Timetable;

import java.util.ArrayList;
import java.util.List;

public class ReplaceTimetableCommand extends BluetoothCommand {

    private static final String REPLACE_TIMETABLE_COMMAND_CODE = "01";

    private Timetable timetable;
    //01103|130000140000150000e Command-TimetableNum-NumOfRings-Time1-Time2...-e
    protected ReplaceTimetableCommand(Timetable newTimetable) {
        super(REPLACE_TIMETABLE_COMMAND_CODE);
        this.timetable = newTimetable;
    }

    @Override
    public String getCommand() {
        List<Lesson> lessons = new ArrayList<>();
        StringBuilder builder = new StringBuilder()
                .append(getCode())
                .append(1)
                .append(getTimetableSize());
        for (int i = 0; i < lessons.size(); i++) {
            Lesson lesson = lessons.get(i);
        }
        return null; //TODO
    }

    private String getTimetableSize() {
        int size = timetable.getLessons().size() * 2;
        if (size < 10) {
            return "0" + size;
        } else {
            return String.valueOf(size);
        }
    }
}
