package com.happs.ximand.ringcontrol.model.object.command;

import com.happs.ximand.ringcontrol.model.object.timetable.Lesson;
import com.happs.ximand.ringcontrol.model.object.timetable.Time;
import com.happs.ximand.ringcontrol.model.object.timetable.Timetable;

public class ReplaceTimetableCommand extends BluetoothCommand {

    private static final String REPLACE_TIMETABLE_COMMAND_CODE = "01";
    private static final String TIMETABLE_ID_STUB = "1";

    private final StringBuilder commandBuilder = new StringBuilder();
    private final Timetable timetable;

    //01103|130000140000150000e Command-TimetableNum-NumOfRings-Time1-Time2...-e
    protected ReplaceTimetableCommand(Timetable newTimetable) {
        super(REPLACE_TIMETABLE_COMMAND_CODE);
        this.timetable = newTimetable;
    }

    @Override
    public String getCommand() {
        appendMeta();
        for (Lesson lesson : timetable.getLessons()) {
            appendTime(lesson.getStartTime());
            appendTime(lesson.getEndTime());
        }
        appendEndOfLine();
        return commandBuilder.toString();
    }

    private void appendMeta() {
        commandBuilder.append(getCode())
                .append(TIMETABLE_ID_STUB)
                .append(getTimetableSize());
    }

    private void appendTime(Time time) {
        commandBuilder.append(time.getHours())
                .append(time.getMinutes())
                .append(time.getSeconds());
    }

    private void appendEndOfLine() {
        commandBuilder.append("e");
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
