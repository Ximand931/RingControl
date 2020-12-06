package com.happs.ximand.ringcontrol.viewmodel.item;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.happs.ximand.ringcontrol.BR;
import com.happs.ximand.ringcontrol.OnEventListener;
import com.happs.ximand.ringcontrol.model.object.Lesson;
import com.happs.ximand.ringcontrol.model.object.Timetable;
import com.happs.ximand.ringcontrol.viewmodel.TimeHelper;

import java.util.List;

@Deprecated
public class TimetableItemViewModel extends BaseObservable {

    private Timetable timetable;
    private boolean currentTimetable;
    private boolean connected;

    private OnEventListener<Timetable> onApplyClickEvent;
    private OnEventListener<Timetable> onDetailsClickEvent;

    private TimetableItemViewModel() {

    }

    @Deprecated
    public TimetableItemViewModel(Timetable timetable, boolean currentTimetable) {
        this.timetable = timetable;
        this.currentTimetable = currentTimetable;
        this.connected = false;
    }

    public void setOnApplyClickEvent(OnEventListener<Timetable> onApplyClickEvent) {
        this.onApplyClickEvent = onApplyClickEvent;
    }

    public void setOnDetailsClickEvent(OnEventListener<Timetable> onDetailsClickEvent) {
        this.onDetailsClickEvent = onDetailsClickEvent;
    }

    @Bindable
    public boolean isCurrentTimetable() {
        return currentTimetable;
    }

    @Bindable
    public String getTitle() {
        return timetable.getTitle();
    }

    @Bindable
    public String getTimePreview() {
        List<Lesson> lessons = timetable.getLessons();
        StringBuilder previewBuilder = new StringBuilder();
        for (Lesson lesson : lessons) {
            if (previewBuilder.length() < 86) {
                previewBuilder
                        .append(TimeHelper.getPreviewTime(lesson.getStartTime()))
                        .append(" - ")
                        .append(TimeHelper.getPreviewTime(lesson.getEndTime()))
                        .append(", ");
            }
        }
        return prunePreviewString(previewBuilder.toString());
    }

    private String prunePreviewString(String s) {
        if (s.length() > 86) {
            return s.substring(
                    0, s.substring(0, 86).lastIndexOf(',')
            ) + "...";
        } else {
            return s.substring(0, s.length() - 2);
        }
    }

    @Bindable
    public int getNumOfLessons() {
        return timetable.getLessons().size();
    }

    @Bindable
    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
        notifyPropertyChanged(BR.connected);
    }

    public void onApplyClick() {
        onApplyClickEvent.onEvent(timetable);
    }

    public void onDetailsClick() {
        onDetailsClickEvent.onEvent(timetable);
    }

}
