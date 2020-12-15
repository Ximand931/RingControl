package com.happs.ximand.ringcontrol.viewmodel.fragment;

import com.google.android.material.snackbar.Snackbar;
import com.happs.ximand.ringcontrol.FragmentNavigation;
import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.model.dao.SharedPreferencesDao;
import com.happs.ximand.ringcontrol.model.object.Timetable;
import com.happs.ximand.ringcontrol.model.repository.Repository;
import com.happs.ximand.ringcontrol.model.repository.impl.FakeTimetableRepository;
import com.happs.ximand.ringcontrol.view.fragment.AllTimetablesFragment;
import com.happs.ximand.ringcontrol.viewmodel.SnackbarDto;

public class EditTimetableViewModel extends BaseEditTimetableViewModel {

    private Timetable editingTimetable;

    public EditTimetableViewModel() {

    }

    public void setEditingTimetable(Timetable editingTimetable) {
        this.editingTimetable = editingTimetable;
    }

    @Override
    protected void completeEditAction() {
        boolean correct = checkTitle() && checkLessons();
        if (correct) {
            updateEditingTimetable();
            updateRepository();
            if (isAppliedTimetable()) {
                notifyAppliedTimetableUpdated();
            }
            FragmentNavigation.getInstance().navigateToPreviousFragment();
        } else {
            getMakeSnackbarEvent().setValue(new SnackbarDto(
                    R.string.timetable_was_not_updated, Snackbar.LENGTH_SHORT
            ));
        }
    }

    private void updateEditingTimetable() {
        this.editingTimetable.setTitle(
                getTitleLiveData().getValue()
        );
        this.editingTimetable.setLessons(
                getLessonsLiveData().getValue()
        );
    }

    private void updateRepository() {
        Repository<Timetable> repository = FakeTimetableRepository.getInstance();
        repository.update(editingTimetable);
    }

    private boolean isAppliedTimetable() {
        return editingTimetable.getId() == SharedPreferencesDao.getInstance().getAppliedTimetableId();
    }

    private void notifyAppliedTimetableUpdated() {
        String tag = AllTimetablesFragment.class.getSimpleName();
        int id = AllTimetablesFragment.EVENT_APPLIED_TIMETABLE_UPDATED;
        FragmentNavigation.getInstance().notifyFragmentAboutEvent(tag, id);
    }

}
