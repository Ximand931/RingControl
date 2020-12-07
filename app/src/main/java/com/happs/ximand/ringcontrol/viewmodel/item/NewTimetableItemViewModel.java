package com.happs.ximand.ringcontrol.viewmodel.item;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.happs.ximand.ringcontrol.OnEventListener;
import com.happs.ximand.ringcontrol.model.object.Lesson;
import com.happs.ximand.ringcontrol.model.object.Timetable;
import com.happs.ximand.ringcontrol.viewmodel.util.TimeHelper;

@Deprecated
public class NewTimetableItemViewModel extends BaseItemViewModel {

    private Timetable timetable;

    private MutableLiveData<String> titleLiveData;
    private MutableLiveData<Integer> numOfLessonsLiveData;
    private MutableLiveData<String> timePreviewLiveData;

    private MutableLiveData<Boolean> currentTimetableLiveData;
    private MutableLiveData<Boolean> applyingPossibleLiveData;

    private OnEventListener<Timetable> applyClickListener;
    private OnEventListener<Timetable> detailsClickListener;

    private NewTimetableItemViewModel() {
        this.titleLiveData = new MutableLiveData<>();
        this.numOfLessonsLiveData = new MutableLiveData<>();
        this.timePreviewLiveData = new MutableLiveData<>();
        this.currentTimetableLiveData = new MutableLiveData<>();
        this.applyingPossibleLiveData = new MutableLiveData<>();
    }
    
    public static NewTimetableItemViewModel createFromTimetable(Timetable timetable) {
        NewTimetableItemViewModel viewModel = new NewTimetableItemViewModel();
        viewModel.setTimetable(timetable);
        viewModel.titleLiveData.setValue(timetable.getTitle());
        viewModel.numOfLessonsLiveData.setValue(timetable.getId());
        viewModel.timePreviewLiveData.setValue(viewModel.makeTimePreview());
        viewModel.currentTimetableLiveData.setValue(false);
        viewModel.applyingPossibleLiveData.setValue(false);
        return viewModel;
    }

    private void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }

    public LiveData<String> getTitleLiveData() {
        return titleLiveData;
    }

    public LiveData<Integer> getNumOfLessonsLiveData() {
        return numOfLessonsLiveData;
    }

    public LiveData<String> getTimePreviewLiveData() {
        return timePreviewLiveData;
    }

    public LiveData<Boolean> getCurrentTimetableLiveData() {
        return currentTimetableLiveData;
    }

    public void setApplyingTimetable(boolean current) {
        currentTimetableLiveData.setValue(current);
    }

    public LiveData<Boolean> getApplyingPossibleLiveData() {
        return applyingPossibleLiveData;
    }

    public void setApplyingPossible(boolean possible) {
        applyingPossibleLiveData.setValue(possible);
    }

    public void setApplyClickListener(OnEventListener<Timetable> applyClickListener) {
        this.applyClickListener = applyClickListener;
    }

    public void setDetailsClickListener(OnEventListener<Timetable> detailsClickListener) {
        this.detailsClickListener = detailsClickListener;
    }

    public void onApplyButtonClick() {
        if (applyClickListener != null) {
            applyClickListener.onEvent(timetable);
        } else {
            throw new RuntimeException("Apply click listener not attached to view model");
        }
    }

    public void onDetailsButtonClick() {
        if (detailsClickListener != null) {
            detailsClickListener.onEvent(timetable);
        } else {
            throw new RuntimeException("Details click listener not attached to view model");
        }
    }

    private String makeTimePreview() {
        StringBuilder previewBuilder = new StringBuilder();
        for (Lesson lesson : timetable.getLessons()) {
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

    private String prunePreviewString(String s) {
        if (s.length() > 86) {
            return s.substring(
                    0, s.substring(0, 86).lastIndexOf(',')
            ) + "...";
        } else {
            return s.substring(0, s.length() - 2);
        }
    }
}
