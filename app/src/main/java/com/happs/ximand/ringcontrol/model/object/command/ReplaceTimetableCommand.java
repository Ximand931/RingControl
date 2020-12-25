package com.happs.ximand.ringcontrol.model.object.command;

import com.happs.ximand.ringcontrol.model.object.timetable.Lesson;
import com.happs.ximand.ringcontrol.model.object.timetable.Time;
import com.happs.ximand.ringcontrol.model.object.timetable.Timetable;

public class ReplaceTimetableCommand extends BluetoothCommand {

    private static final byte REPLACE_TIMETABLE_COMMAND_CODE = 10;
    private static final byte TIMETABLE_ID_STUB = 1;

    private final byte[] command;
    private final Timetable timetable;

    public ReplaceTimetableCommand(Timetable newTimetable) {
        super(REPLACE_TIMETABLE_COMMAND_CODE);
        int timeSize = newTimetable.getLessons().size() * 2 * 3;
        this.command = new byte[3 + timeSize + 1];
        this.timetable = newTimetable;
    }

    @Override
    public byte[] getCommand() {
        appendMeta();
        for (int i = 0; i < timetable.getLessons().size(); i++) {
            Lesson lesson = timetable.getLessons().get(i);
            appendTime(i * 6 + 3, lesson.getStartTime());
            appendTime(i * 6 + 6, lesson.getEndTime());
        }
        appendEndOfLine();
        return command;
    }

    private void appendMeta() {
        command[0] = getCode();
        command[1] = TIMETABLE_ID_STUB;
        command[2] = (byte) (timetable.getLessons().size() * 2);
    }

    private void appendTime(int startPos, Time time) {
        command[startPos] = (byte) time.getHours();
        command[startPos + 1] = (byte) time.getMinutes();
        command[startPos + 2] = (byte) time.getSeconds();
    }

    private void appendEndOfLine() {
        command[command.length - 1] = 1;
    }
}
