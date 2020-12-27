package com.happs.ximand.ringcontrol.viewmodel.fragment;

import com.happs.ximand.ringcontrol.FragmentNavigation;
import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.model.dao.SharedPreferencesDao;
import com.happs.ximand.ringcontrol.model.object.timetable.Timetable;
import com.happs.ximand.ringcontrol.model.repository.impl.TimetableRepository;
import com.happs.ximand.ringcontrol.view.fragment.AllTimetablesFragment;

public class EditTimetableViewModel extends BaseEditTimetableViewModel {

    private Timetable editingTimetable;

    public void setEditingTimetable(Timetable editingTimetable) {
        this.editingTimetable = editingTimetable;
        setLessonList(editingTimetable.getLessons());
        getTitleLiveData().setValue(editingTimetable.getTitle());
    }

    @Override
    protected void onCompleteEditAction() {
        updateEditingTimetableWithInputData();
        TimetableRepository.getInstance().update(editingTimetable);
    }

    @Override
    protected void onEditActionCompleted() {
        super.onEditActionCompleted();
        if (isAppliedTimetable()) {
            notifyMainFragmentAboutAppliedTimetableUpdated();
        }
    }

    @Override
    protected int getErrorMessageResId() {
        return R.string.timetable_was_not_updated;
    }

    private void updateEditingTimetableWithInputData() {
        this.editingTimetable.setTitle(
                getTitleLiveData().getValue()
        );
        this.editingTimetable.setLessons(
                getLessonsLiveData().getValue()
        );
    }

    private boolean isAppliedTimetable() {
        return editingTimetable.getId() ==
                SharedPreferencesDao.getInstance().getAppliedTimetableId();
    }

    private void notifyMainFragmentAboutAppliedTimetableUpdated() {
        FragmentNavigation.getInstance().notifyFragmentAboutEvent(
                AllTimetablesFragment.TAG, AllTimetablesFragment.EVENT_APPLIED_TIMETABLE_UPDATED
        );
    }

}
