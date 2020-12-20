package com.happs.ximand.ringcontrol.view.fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
    protected void onSetActionBarTitle(@NonNull ActionBar actionBar) {
        actionBar.setTitle(R.string.add_lesson);
    }
}
