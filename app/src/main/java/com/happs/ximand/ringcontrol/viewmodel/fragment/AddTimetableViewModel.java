package com.happs.ximand.ringcontrol.viewmodel.fragment;

import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.model.object.timetable.Timetable;
import com.happs.ximand.ringcontrol.model.repository.impl.TimetableRepository;

import java.util.ArrayList;

public class AddTimetableViewModel extends BaseEditTimetableViewModel {

    public AddTimetableViewModel() {
        setLessonList(new ArrayList<>());
        this.numOfLessons.setValue(0);
    }

    @Override
    protected void onCompleteEditAction() {
        Timetable timetable = createNewTimetable();
        TimetableRepository.getInstance().add(timetable);
    }

    @Override
    protected int getErrorMessageResId() {
        return R.string.timetable_was_not_added;
    }

    private Timetable createNewTimetable() {
        return new Timetable(
                getTitleLiveData().getValue(), getLessonsLiveData().getValue()
        );
    }
}
