package com.happs.ximand.ringcontrol.view.fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.databinding.FragmentEditTimetableBinding;
import com.happs.ximand.ringcontrol.model.object.Timetable;
import com.happs.ximand.ringcontrol.view.BaseFragment;
import com.happs.ximand.ringcontrol.viewmodel.fragment.EditTimetableViewModel;

public class EditTimetableFragment
        extends BaseFragment<EditTimetableViewModel, FragmentEditTimetableBinding> {

    private static final String FRAGMENT_TAG = "EditTimetable";
    private static final String KEY_ID = "TIMETABLE";
    private Timetable timetable;

    public EditTimetableFragment() {
        super(R.layout.fragment_edit_timetable, FRAGMENT_TAG);
    }

    public static EditTimetableFragment newInstance(Timetable timetable) {
        EditTimetableFragment fragment = new EditTimetableFragment();
        fragment.timetable = timetable;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_ID)) {
            this.timetable = (Timetable) savedInstanceState.getSerializable(KEY_ID);
        }
    }

    @Override
    protected void onPreViewModelAttaching(@NonNull EditTimetableViewModel viewModel) {
        viewModel.setEditingTimetable(timetable);
        viewModel.initEditTimetableRecyclerViewAdapter();
    }

    @Override
    protected void onViewDataBindingCreated(@NonNull FragmentEditTimetableBinding binding) {
        initRecyclerViewLayoutManager(binding.lessonsRecyclerView);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewModel().getEditStatus().observe(getViewLifecycleOwner(), editStatus -> {
            if (getView() != null) {
                Snackbar.make(getView(), editStatus, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(KEY_ID, timetable);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar_editing, menu);
    }
}
