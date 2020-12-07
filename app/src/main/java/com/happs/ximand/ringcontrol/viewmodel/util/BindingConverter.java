package com.happs.ximand.ringcontrol.viewmodel.util;

import com.happs.ximand.ringcontrol.model.object.Lesson;

import java.util.List;

public final class BindingConverter {

    private BindingConverter() {

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
