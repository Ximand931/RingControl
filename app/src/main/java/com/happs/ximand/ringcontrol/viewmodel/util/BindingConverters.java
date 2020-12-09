package com.happs.ximand.ringcontrol.viewmodel.util;

import com.happs.ximand.ringcontrol.model.object.Lesson;
import com.happs.ximand.ringcontrol.model.object.Time;

import java.util.List;

public final class BindingConverters {

    private BindingConverters() {

    }

    public static String convertTimeToPreviewTimeString(Time time) {
        StringBuilder simplifiedTimeBuilder = new StringBuilder();
        simplifiedTimeBuilder.append(time.getHours());
        if (time.getMinutes() < 10) {
            simplifiedTimeBuilder.append("0");
        }
        simplifiedTimeBuilder.append(time.getMinutes());
        if (time.getSeconds() != 0) {
            if (time.getSeconds() < 10) {
                simplifiedTimeBuilder.append("0");
            }
            simplifiedTimeBuilder.append(time.getSeconds());
        }
        return simplifiedTimeBuilder.toString();
    }

    public static String convertListToLessonsDetails(List<Lesson> lessons) {
        StringBuilder previewBuilder = new StringBuilder();
        for (Lesson lesson : lessons) {
            if (previewBuilder.length() > 86) {
                break;
            }
            previewBuilder
                    .append(TimeHelper.getPreviewTime(lesson.getStartTimeDep()))
                    .append(" - ")
                    .append(TimeHelper.getPreviewTime(lesson.getEndTimeDep()))
                    .append(", ");
        }
        return prunePreviewString(previewBuilder.toString());
    }

    private static String prunePreviewString(String s) {
        if (s.length() > 86) {
            return s.substring(
                    0, s.substring(0, 86).lastIndexOf(',')
            ) + "...";
        } else {
            return s.substring(0, s.length() - 2);
        }
    }

}
