package com.happs.ximand.ringcontrol.model.object.command;

import com.happs.ximand.ringcontrol.model.object.timetable.Lesson;
import com.happs.ximand.ringcontrol.model.object.timetable.Time;
import com.happs.ximand.ringcontrol.model.object.timetable.Timetable;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class ReplaceTimetableCommandTest {

    @Test
    void getCommandTest() {
        Timetable newTestTimetable = new Timetable("Title", new ArrayList<Lesson>() {{
            add(new Lesson(1, new Time(12, 0, 0),
                    new Time(13, 0, 0)));
            add(new Lesson(2, new Time(14, 0, 0),
                    new Time(15, 0, 0)));
            add(new Lesson(3, new Time(16, 0, 0),
                    new Time(17, 0, 0)));
        }});
        ReplaceTimetableCommand command = new ReplaceTimetableCommand(newTestTimetable);
        byte[] expected = new byte[]{10, 1, 6, 12, 0, 0, 13, 0, 0, 14, 0, 0,
                15, 0, 0, 16, 0, 0, 17, 0, 0, 1};
        byte[] actual = command.getCommand();
        Assert.assertArrayEquals(expected, actual);
    }
}