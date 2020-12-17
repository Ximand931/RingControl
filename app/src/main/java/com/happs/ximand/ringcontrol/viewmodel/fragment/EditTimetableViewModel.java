package com.happs.ximand.ringcontrol.viewmodel.fragment;

import com.happs.ximand.ringcontrol.FragmentNavigation;
import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.model.dao.SharedPreferencesDao;
import com.happs.ximand.ringcontrol.model.object.Timetable;
import com.happs.ximand.ringcontrol.model.repository.impl.TimetableRepository;
import com.happs.ximand.ringcontrol.view.fragment.AllTimetablesFragment;

public class EditTimetableViewModel extends BaseEditTimetableViewModel {

    private Timetable editingTimetable;

    public void setEditingTimetable(Timetable editingTimetable) {
        this.editingTimetable = editingTimetable;
        setLessonList(editingTimetable.getLessons());
    }

    @Override
    protected void onCompleteEditAction() {
        updateEditingTimetableWithInputData();
        TimetableRepository.getInstance().update(editingTimetable);
    }

    @Override
    protected void onEditActionCompleted() {
        if (isAppliedTimetable()) {
            notifyAppliedTimetableUpdated();
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

    private void notifyAppliedTimetableUpdated() {
        FragmentNavigation.getInstance().notifyFragmentAboutEvent(
                AllTimetablesFragment.TAG, AllTimetablesFragment.EVENT_TIMETABLE_LIST_UPDATED
        );
    }

}
