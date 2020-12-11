package com.happs.ximand.ringcontrol.viewmodel.fragment;

import androidx.lifecycle.MutableLiveData;

import com.happs.ximand.ringcontrol.FragmentNavigation;
import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.model.dao.SharedPreferencesDao;
import com.happs.ximand.ringcontrol.model.object.Timetable;
import com.happs.ximand.ringcontrol.model.object.exception.IncorrectInputException;
import com.happs.ximand.ringcontrol.model.repository.Repository;
import com.happs.ximand.ringcontrol.model.repository.impl.FakeTimetableRepository;
import com.happs.ximand.ringcontrol.view.fragment.AllTimetablesFragment;

public class EditTimetableViewModel extends BaseEditTimetableViewModel {

    //private final MutableLiveData<List<Lesson>> lesson
    private final MutableLiveData<String> editStatus;
    private Timetable editingTimetable;

    public EditTimetableViewModel() {
        this.editStatus = new MutableLiveData<>();
    }

    public MutableLiveData<String> getEditStatus() {
        return editStatus;
    }

    public void setEditingTimetable(Timetable editingTimetable) {
        this.editingTimetable = editingTimetable;
    }

    @Override
    public boolean notifyOptionsMenuItemClicked(int itemId) {
        switch (itemId) {
            case R.id.toolbar_save:
                saveChanges();
                return true;
            case R.id.toolbar_cancel:
                FragmentNavigation.getInstance().navigateToPreviousFragment();
                return true;
        }
        return false;
    }

    private void saveChanges() {
        try {
            updateEditingTimetable();
            updateRepository();
            if (isAppliedTimetable()) {
                notifyAppliedTimetableUpdated();
            }
            FragmentNavigation.getInstance().navigateToPreviousFragment();
        } catch (IncorrectInputException e) {
            updateEditStatus();
        }
    }

    private void updateEditingTimetable() throws IncorrectInputException {
        //TODO
    }

    private void updateRepository() {
        Repository<Timetable> repository = FakeTimetableRepository.getInstance();
        repository.update(editingTimetable);
    }

    private boolean isAppliedTimetable() {
        return editingTimetable.getId() == SharedPreferencesDao.getInstance().getAppliedTimetableId();
    }

    private void notifyAppliedTimetableUpdated() {
        String tag = AllTimetablesFragment.class.getSimpleName(); //TODO
        int id = AllTimetablesFragment.EVENT_APPLIED_TIMETABLE_UPDATED;
        FragmentNavigation.getInstance().notifyFragmentAboutEvent(tag, id);
    }


    //TODO: replace with enum
    private void updateEditStatus() {
        editStatus.setValue(
                getApplication().getString(R.string.timetable_was_not_updated)
        );
    }
}
