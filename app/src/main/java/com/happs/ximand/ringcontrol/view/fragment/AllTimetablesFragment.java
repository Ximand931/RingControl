package com.happs.ximand.ringcontrol.view.fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.RecyclerView;

import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.databinding.FragmentAllTimetablesBinding;
import com.happs.ximand.ringcontrol.model.object.timetable.Timetable;
import com.happs.ximand.ringcontrol.view.adapter.AllTimetablesRecyclerViewAdapter;
import com.happs.ximand.ringcontrol.viewmodel.fragment.AllTimetablesViewModel;

import java.util.List;

public class AllTimetablesFragment extends BaseFragmentWithRecyclerView<AllTimetablesViewModel,
        FragmentAllTimetablesBinding, Timetable, AllTimetablesRecyclerViewAdapter> {

    public static final String TAG = AllTimetablesFragment.class.getSimpleName();

    public static final int EVENT_APPLIED_TIMETABLE_UPDATED = 1;
    public static final int EVENT_TIMETABLE_LIST_UPDATED = 2;

    public AllTimetablesFragment() {
        super(R.layout.fragment_all_timetables, R.menu.menu_toolbar_main);
    }

    public static AllTimetablesFragment newInstance() {
        return new AllTimetablesFragment();
    }

    @Override
    protected RecyclerView getRecyclerViewFromBinding(FragmentAllTimetablesBinding binding) {
        return binding.allTimetablesRecyclerView;
    }

    @Override
    protected void onPreViewModelAttaching(@NonNull AllTimetablesViewModel viewModel) {
        viewModel.getAllTimetablesLiveData().observe(
                getViewLifecycleOwner(), this::initAdapter
        );
    }

    @Override
    protected void onSetActionBarTitle(@NonNull ActionBar actionBar) {
        actionBar.setTitle(R.string.all_timetables);
    }

    @Override
    protected AllTimetablesRecyclerViewAdapter createNewAdapter(List<Timetable> items) {
        return new AllTimetablesRecyclerViewAdapter(items);
    }

    @Override
    protected void onPreAttachRecyclerViewAdapter(AllTimetablesRecyclerViewAdapter adapter) {
        adapter.setApplyTimetableClickListener(
                getViewModel()::applyTimetable
        );
        adapter.setDetailsTimetableClickListener(
                getViewModel()::showTimetableDetails
        );
    }

    @Override
    public void onExternalEvent(int eventId) {
        if (eventId == EVENT_TIMETABLE_LIST_UPDATED) {
            getViewModel().updateTimetables();
        }
    }
}