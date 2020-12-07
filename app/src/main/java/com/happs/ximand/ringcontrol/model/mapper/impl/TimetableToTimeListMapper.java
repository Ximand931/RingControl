package com.happs.ximand.ringcontrol.model.mapper.impl;

import com.happs.ximand.ringcontrol.model.mapper.Mapper;
import com.happs.ximand.ringcontrol.model.object.Lesson;
import com.happs.ximand.ringcontrol.model.object.Timetable;

import java.util.ArrayList;
import java.util.List;

public class TimetableToTimeListMapper implements Mapper<Timetable, List<String>> {

    @Override
    public List<String> map(Timetable from) {
        List<String> timeList = new ArrayList<>();
        for (Lesson lesson : from.getLessons()) {
            timeList.add(lesson.getStartTimeDep());
            timeList.add(lesson.getEndTimeDep());
        }
        return timeList;
    }

}
