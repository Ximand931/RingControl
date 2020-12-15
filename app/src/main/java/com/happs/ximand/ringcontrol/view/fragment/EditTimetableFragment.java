package com.happs.ximand.ringcontrol.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.databinding.FragmentEditTimetableBinding;
import com.happs.ximand.ringcontrol.model.object.Lesson;
import com.happs.ximand.ringcontrol.model.object.Timetable;
import com.happs.ximand.ringcontrol.view.BaseFragment;
import com.happs.ximand.ringcontrol.view.adapter.EditTimetableRecyclerViewAdapter;
import com.happs.ximand.ringcontrol.viewmodel.fragment.EditTimetableViewModel;

import java.util.List;

public class EditTimetableFragment
        extends BaseFragment<EditTimetableViewModel, FragmentEditTimetableBinding> {

    private static final String KEY_TIMETABLE = "TIMETABLE";
    private RecyclerView lessonsRecyclerView;
    @Nullable
    private Timetable timetable;

    public EditTimetableFragment() {
        super(R.layout.fragment_edit_timetable, R.menu.menu_toolbar_editing);
    }

    public static EditTimetableFragment newInstance(Timetable timetable) {
        EditTimetableFragment fragment = new EditTimetableFragment();
        fragment.timetable = timetable;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_TIMETABLE)) {
            this.timetable = (Timetable) savedInstanceState.getSerializable(KEY_TIMETABLE);
        }
    }

    @Override
    protected void onViewDataBindingCreated(@NonNull FragmentEditTimetableBinding binding) {
        lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        lessonsRecyclerView = binding.lessonsRecyclerView;
    }

    @Override
    protected void onPreViewModelAttaching(@NonNull EditTimetableViewModel viewModel) {
        viewModel.setEditingTimetable(timetable);
        viewModel.getLessonsLiveData().observe(getViewLifecycleOwner(), this::initAdapter);
    }

    private void initAdapter(List<Lesson> lessons) {
        lessonsRecyclerView.setAdapter(
                new EditTimetableRecyclerViewAdapter(lessons)
        );
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(KEY_TIMETABLE, timetable);
    }
}
