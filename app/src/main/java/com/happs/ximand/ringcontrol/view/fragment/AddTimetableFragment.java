package com.happs.ximand.ringcontrol.view.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.databinding.FragmentAddTimetableBinding;
import com.happs.ximand.ringcontrol.viewmodel.fragment.AddTimetableViewModel;

public class AddTimetableFragment
        extends BaseEditTimetableFragment<AddTimetableViewModel, FragmentAddTimetableBinding> {

    public AddTimetableFragment() {
        super(R.layout.fragment_add_timetable);
    }

    public static AddTimetableFragment newInstance() {
        return new AddTimetableFragment();
    }

    @Override
    protected RecyclerView getRecyclerViewFromBinding(FragmentAddTimetableBinding binding) {
        return binding.lessonsRecyclerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setActionBarTitle(R.string.add_lesson);
    }
}
