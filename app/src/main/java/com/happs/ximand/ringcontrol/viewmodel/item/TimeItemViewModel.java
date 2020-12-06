package com.happs.ximand.ringcontrol.viewmodel.item;

import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.happs.ximand.ringcontrol.viewmodel.TimeHelper;

@Deprecated
public class TimeItemViewModel extends BaseItemViewModel {

    private int numOfLesson;
    private String previewTime;
    private String overline;

    public TimeItemViewModel(int num, String originalStartTime, String originalEndTime) {
        this.numOfLesson = num;
        this.previewTime = getPreviewTimeByTime(originalStartTime, originalEndTime);
        this.overline = getOverlineByTime(originalStartTime);
    }

    @Bindable
    @Deprecated
    public String getTitle() {
        return "DEPRECATED";
    }

    @Bindable
    public int getNumOfLesson() {
        return numOfLesson;
    }

    @Bindable
    public String getPreviewTime() {
        return previewTime;
    }

    @Bindable
    public String getOverline() {
        return overline;
    }

    private String getPreviewTimeByTime(String start, String end) {
        return TimeHelper.getPreviewTime(start) + " - " + TimeHelper.getPreviewTime(end);
    }

    @Nullable
    private String getOverlineByTime(String start) {
        return null;
    }
}
