package com.happs.ximand.ringcontrol.view.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.databinding.FragmentEditTimetableBinding;
import com.happs.ximand.ringcontrol.model.object.Timetable;
import com.happs.ximand.ringcontrol.viewmodel.fragment.EditTimetableViewModel;

public class EditTimetableFragment
        extends BaseEditTimetableFragment<EditTimetableViewModel, FragmentEditTimetableBinding> {

    @Nullable
    private Timetable timetable;

    public EditTimetableFragment() {
        super(R.layout.fragment_edit_timetable);
    }

    public static EditTimetableFragment newInstance(Timetable timetable) {
        EditTimetableFragment fragment = new EditTimetableFragment();
        fragment.timetable = timetable;
        return fragment;
    }

    @Override
    protected RecyclerView getRecyclerViewFromBinding(FragmentEditTimetableBinding binding) {
        return binding.lessonsRecyclerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setActionBarTitle(R.string.edit);
    }

    @Override
    protected void onPreViewModelAttaching(@NonNull EditTimetableViewModel viewModel) {
        if (timetable != null) {
            viewModel.setEditingTimetable(timetable);
        }
        super.onPreViewModelAttaching(viewModel);
    }

}
