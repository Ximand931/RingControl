package com.happs.ximand.ringcontrol.viewmodel.fragment;

import android.app.Application;
import android.content.DialogInterface;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;

import com.happs.ximand.ringcontrol.FragmentNavigation;
import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.SingleLiveEvent;
import com.happs.ximand.ringcontrol.model.object.Lesson;
import com.happs.ximand.ringcontrol.model.object.Timetable;
import com.happs.ximand.ringcontrol.view.adapter.TimeRecyclerViewAdapter;
import com.happs.ximand.ringcontrol.view.fragment.AllTimetablesFragment;
import com.happs.ximand.ringcontrol.view.fragment.EditTimetableFragment;
import com.happs.ximand.ringcontrol.viewmodel.item.ItemViewModelFactory;
import com.happs.ximand.ringcontrol.viewmodel.item.TimeItemViewModel;

public class TimetableInfoViewModel extends BaseFragmentViewModel {

    private final SingleLiveEvent<DialogInterface.OnClickListener> alertDialogLiveEvent;
    private TimeRecyclerViewAdapter adapter;
    private Timetable timetable;

    public TimetableInfoViewModel(@NonNull Application application) {
        super(application);
        this.alertDialogLiveEvent = new SingleLiveEvent<>();
    }

    public SingleLiveEvent<DialogInterface.OnClickListener> getAlertDialogLiveEvent() {
        return alertDialogLiveEvent;
    }

    @Bindable
    public TimeRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }

    public void initRecyclerViewAdapter() {
        TimeItemViewModelFactory factory = new TimeItemViewModelFactory();
        this.adapter = new TimeRecyclerViewAdapter(
                factory.createViewModelsByItemList(timetable.getLessons())
        );
    }

    @Override
    public boolean onOptionsItemSelected(int itemId) {
        switch (itemId) {
            case R.id.toolbar_remove:
                removeTimetable();
                return true;
            case R.id.toolbar_edit:
                replaceFragment(EditTimetableFragment.newInstance(timetable));
                return true;
        }
        return false;
    }

    private void removeTimetable() {
        alertDialogLiveEvent.setValue((dialog, which) -> {
            getTimetableRepository().remove(timetable);
            notifyAllTimetablesFragmentThatTimetableListUpdated();
            FragmentNavigation.getInstance().navigateToPreviousFragment();
        });
    }

    private void notifyAllTimetablesFragmentThatTimetableListUpdated() {
        String tag = AllTimetablesFragment.FRAGMENT_TAG;
        int id = AllTimetablesFragment.EVENT_TIMETABLE_LIST_UPDATED;
        FragmentNavigation.getInstance().notifyFragmentAboutEvent(tag, id);
    }

    private static class TimeItemViewModelFactory
            extends ItemViewModelFactory<TimeItemViewModel, Lesson> {

        @Override
        public TimeItemViewModel createViewModelByItem(Lesson lesson) {
            return new TimeItemViewModel(
                    lesson.getNumber(), lesson.getStartTime(), lesson.getEndTime()
            );
        }
    }
}
