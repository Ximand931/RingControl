package com.happs.ximand.ringcontrol.view.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.RecyclerView;

import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.databinding.FragmentEditTimetableBinding;
import com.happs.ximand.ringcontrol.model.object.timetable.Timetable;
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
    protected void onSetActionBarTitle(@NonNull ActionBar actionBar) {
        actionBar.setTitle(R.string.edit);
    }

    @Override
    protected void onPreViewModelAttaching(@NonNull EditTimetableViewModel viewModel) {
        if (timetable != null) {
            viewModel.setEditingTimetable(timetable);
        }
        super.onPreViewModelAttaching(viewModel);
    }

}
