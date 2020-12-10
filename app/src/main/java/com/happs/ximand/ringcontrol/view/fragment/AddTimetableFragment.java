package com.happs.ximand.ringcontrol.view.fragment;

import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.databinding.FragmentAddTimetableBinding;
import com.happs.ximand.ringcontrol.view.BaseFragment;
import com.happs.ximand.ringcontrol.viewmodel.fragment.AddTimetableViewModel;

public class AddTimetableFragment
        extends BaseFragment<AddTimetableViewModel, FragmentAddTimetableBinding> {

    private RecyclerView addTimetableRecyclerView;

    public AddTimetableFragment() {
        super(R.layout.fragment_add_timetable);
    }

    public static AddTimetableFragment newInstance() {
        return new AddTimetableFragment();
    }

    @Override
    protected void onPreViewModelAttaching(@NonNull AddTimetableViewModel viewModel) {
        viewModel.initEditTimetableRecyclerViewAdapter();
        viewModel.getUpdateDataInAllTimetablesFragmentLiveEvent().observe(
                getViewLifecycleOwner(), aVoid -> {
                }
        );
    }

    @Override
    protected void onViewDataBindingCreated(@NonNull FragmentAddTimetableBinding binding) {
        initRecyclerViewLayoutManager(binding.lessonsRecyclerView);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar_add, menu);
    }
}
