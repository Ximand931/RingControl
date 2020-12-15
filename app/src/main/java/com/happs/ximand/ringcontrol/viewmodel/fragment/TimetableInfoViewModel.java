package com.happs.ximand.ringcontrol.viewmodel.fragment;

import android.content.DialogInterface;

import com.happs.ximand.ringcontrol.FragmentNavigation;
import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.SingleLiveEvent;
import com.happs.ximand.ringcontrol.model.object.Timetable;
import com.happs.ximand.ringcontrol.model.repository.impl.TimetableRepository;
import com.happs.ximand.ringcontrol.view.fragment.AllTimetablesFragment;
import com.happs.ximand.ringcontrol.view.fragment.EditTimetableFragment;

public class TimetableInfoViewModel extends BaseViewModel {

    //TODO: Use event instead liveEvent
    private final SingleLiveEvent<DialogInterface.OnClickListener> alertDialogLiveEvent;
    private Timetable timetable;

    public TimetableInfoViewModel() {
        this.alertDialogLiveEvent = new SingleLiveEvent<>();
    }

    public SingleLiveEvent<DialogInterface.OnClickListener> getAlertDialogLiveEvent() {
        return alertDialogLiveEvent;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }

    @Override
    public boolean notifyOptionsMenuItemClicked(int itemId) {
        switch (itemId) {
            case R.id.toolbar_remove:
                removeTimetable();
                return true;
            case R.id.toolbar_edit:
                FragmentNavigation.getInstance()
                        .navigateToFragment(EditTimetableFragment.newInstance(timetable));
                return true;
        }
        return false;
    }

    private void removeTimetable() {
        alertDialogLiveEvent.setValue((dialog, which) -> {
            TimetableRepository.getInstance().remove(timetable);
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
