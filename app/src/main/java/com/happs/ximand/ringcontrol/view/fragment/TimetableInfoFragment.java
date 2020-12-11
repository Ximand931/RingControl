package com.happs.ximand.ringcontrol.view.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.databinding.FragmentTimetableInfoBinding;
import com.happs.ximand.ringcontrol.model.object.Timetable;
import com.happs.ximand.ringcontrol.view.BaseFragment;
import com.happs.ximand.ringcontrol.viewmodel.fragment.TimetableInfoViewModel;

public class TimetableInfoFragment
        extends BaseFragment<TimetableInfoViewModel, FragmentTimetableInfoBinding> {

    private static final String KEY_TIMETABLE = "TIMETABLE";
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (timetable == null && savedInstanceState != null) {
            this.timetable = (Timetable) savedInstanceState.getSerializable(KEY_TIMETABLE);
        }
    }

    @Override
    protected void onPreViewModelAttaching(@NonNull TimetableInfoViewModel viewModel) {
        viewModel.setTimetable(timetable);
        viewModel.getAlertDialogLiveEvent().observe(
                getViewLifecycleOwner(), this::onCreateAlertDialog);
    }

    @Override
    protected void onViewDataBindingCreated(@NonNull FragmentTimetableInfoBinding binding) {
        //init recycler view
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setActionBarTitle(timetable.getTitle());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(KEY_TIMETABLE, timetable);
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
