package com.happs.ximand.ringcontrol.viewmodel.util;

import com.happs.ximand.ringcontrol.model.object.Lesson;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BindingConverterTest {

    @Test
    void getLessonsDetailsByList() {
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson(1, "12:00:00", "13:00:00"));

        String details = BindingConverter.convertListToLessonsDetails(lessons);
        String expected = "12:00 - 13:00";
        assertEquals(expected, details);
    }
}