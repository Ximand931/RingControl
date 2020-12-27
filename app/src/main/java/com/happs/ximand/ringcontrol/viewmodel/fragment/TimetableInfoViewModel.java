package com.happs.ximand.ringcontrol.viewmodel.fragment;

import android.content.DialogInterface;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.happs.ximand.ringcontrol.FragmentNavigation;
import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.SingleLiveEvent;
import com.happs.ximand.ringcontrol.model.object.timetable.Timetable;
import com.happs.ximand.ringcontrol.model.repository.impl.TimetableRepository;
import com.happs.ximand.ringcontrol.view.fragment.AllTimetablesFragment;
import com.happs.ximand.ringcontrol.view.fragment.EditTimetableFragment;

public class TimetableInfoViewModel extends BaseViewModel {

    private final SingleLiveEvent<DialogInterface.OnClickListener> alertDialogLiveEvent;
    private MutableLiveData<Timetable> timetableLiveData = new MutableLiveData<>();

    public TimetableInfoViewModel() {
        this.alertDialogLiveEvent = new SingleLiveEvent<>();
    }

    public SingleLiveEvent<DialogInterface.OnClickListener> getAlertDialogLiveEvent() {
        return alertDialogLiveEvent;
    }

    public LiveData<Timetable> getTimetableLiveData() {
        return timetableLiveData;
    }

    public void setTimetable(Timetable timetable) {
        timetableLiveData.setValue(timetable);
    }

    @Override
    public boolean notifyOptionsMenuItemClicked(int itemId) {
        switch (itemId) {
            case R.id.toolbar_remove:
                removeTimetable();
                return true;
            case R.id.toolbar_edit:
                FragmentNavigation.getInstance()
                        .navigateTo(EditTimetableFragment.newInstance(
                                timetableLiveData.getValue())
                        );
                return true;
        }
        return false;
    }

    private void removeTimetable() {
        alertDialogLiveEvent.setValue((dialog, which) -> {
            //noinspection ConstantConditions
            TimetableRepository.getInstance().remove(timetableLiveData.getValue());
            notifyAllTimetablesFragmentThatTimetableListUpdated();
            FragmentNavigation.getInstance().navigateToPreviousFragment();
        });
    }

    private void notifyAllTimetablesFragmentThatTimetableListUpdated() {
        String tag = AllTimetablesFragment.TAG;
        int id = AllTimetablesFragment.EVENT_TIMETABLE_LIST_UPDATED;
        FragmentNavigation.getInstance().notifyFragmentAboutEvent(tag, id);
    }

}
