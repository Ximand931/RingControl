package com.happs.ximand.ringcontrol.view.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.databinding.FragmentTimetableInfoBinding;
import com.happs.ximand.ringcontrol.model.object.Lesson;
import com.happs.ximand.ringcontrol.model.object.Timetable;
import com.happs.ximand.ringcontrol.view.adapter.TimeRecyclerViewAdapter;
import com.happs.ximand.ringcontrol.viewmodel.fragment.TimetableInfoViewModel;

import java.util.List;

public class TimetableInfoFragment extends BaseFragmentWithRecyclerView<TimetableInfoViewModel,
        FragmentTimetableInfoBinding, Lesson, TimeRecyclerViewAdapter> {

    private Timetable timetable;

    public TimetableInfoFragment() {
        super(R.layout.fragment_timetable_info, R.menu.menu_toolbar_info);
    }

    public static TimetableInfoFragment newInstance(Timetable timetable) {
        TimetableInfoFragment fragment = new TimetableInfoFragment();
        fragment.timetable = timetable;
        return fragment;
    }

    @Override
    protected RecyclerView getRecyclerViewFromBinding(FragmentTimetableInfoBinding binding) {
        return binding.timetableInfoRecyclerView;
    }

    @Override
    protected void onPreViewModelAttaching(@NonNull TimetableInfoViewModel viewModel) {
        if (timetable != null) {
            viewModel.setTimetable(timetable);
        }
        viewModel.getTimetableLiveData().observe(getViewLifecycleOwner(),
                timetable -> initAdapter(timetable.getLessons())
        );
        viewModel.getAlertDialogLiveEvent().observe(
                getViewLifecycleOwner(), this::onCreateAlertDialog);
    }

    @Override
    protected TimeRecyclerViewAdapter createNewAdapter(List<Lesson> items) {
        return new TimeRecyclerViewAdapter(items);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setActionBarTitle(timetable.getTitle());
    }

    private void onCreateAlertDialog(DialogInterface.OnClickListener onPositiveClick) {
        if (getContext() == null) {
            return;
        }
        new MaterialAlertDialogBuilder(getContext())
                .setTitle(R.string.removing)
                .setMessage(R.string.removing_description)
                .setPositiveButton(R.string.remove, onPositiveClick)
                .setNegativeButton(
                        R.string.cancel, (dialog, which) -> dialog.dismiss()
                )
                .create()
                .show();
    }
}
