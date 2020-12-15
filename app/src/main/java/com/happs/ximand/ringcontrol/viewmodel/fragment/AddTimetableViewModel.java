package com.happs.ximand.ringcontrol.viewmodel.fragment;

import com.google.android.material.snackbar.Snackbar;
import com.happs.ximand.ringcontrol.FragmentNavigation;
import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.model.object.Timetable;
import com.happs.ximand.ringcontrol.model.repository.impl.TimetableRepository;
import com.happs.ximand.ringcontrol.viewmodel.SnackbarDto;

import java.util.ArrayList;

public class AddTimetableViewModel extends BaseEditTimetableViewModel {

    public AddTimetableViewModel() {
        this.detailEditing = false;
        this.numOfLessons.setValue(0);
        setLessons(new ArrayList<>());
    }

    @Override
    protected void completeEditAction() {
        boolean correct = checkTitle() && checkLessons();
        if (correct) {
            Timetable newTimetable = createNewTimetable();
            TimetableRepository.getInstance().add(newTimetable);
            FragmentNavigation.getInstance().navigateToPreviousFragment();
        } else {
            getMakeSnackbarEvent().setValue(new SnackbarDto(
                    R.string.timetable_was_not_added, Snackbar.LENGTH_SHORT
            ));
        }
    }

    private Timetable createNewTimetable() {
        return new Timetable(
                getTitleLiveData().getValue(), getLessonsLiveData().getValue()
        );
    }
}
