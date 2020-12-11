package com.happs.ximand.ringcontrol.view.fragment;

import android.app.Application;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.databinding.FragmentAllTimetablesBinding;
import com.happs.ximand.ringcontrol.model.object.Timetable;
import com.happs.ximand.ringcontrol.view.BaseFragment;
import com.happs.ximand.ringcontrol.view.adapter.AllTimetablesRecyclerViewAdapter;
import com.happs.ximand.ringcontrol.viewmodel.SharedViewModel;
import com.happs.ximand.ringcontrol.viewmodel.fragment.AllTimetablesViewModel;

import java.util.List;

public class AllTimetablesFragment
        extends BaseFragment<AllTimetablesViewModel, FragmentAllTimetablesBinding> {

    public static final String TAG = AllTimetablesFragment.class.getSimpleName();

    public static final int EVENT_APPLIED_TIMETABLE_UPDATED = 1;
    public static final int EVENT_TIMETABLE_LIST_UPDATED = 2;

    private SharedViewModel sharedViewModel;
    private RecyclerView allTimetablesRecyclerView;

    public AllTimetablesFragment() {
        super(R.layout.fragment_all_timetables, R.menu.menu_toolbar_main);
    }

    public static AllTimetablesFragment newInstance() {
        return new AllTimetablesFragment();
    }

    public SharedViewModel getSharedViewModel() {
        return sharedViewModel;
    }

    @Override
    protected void onViewDataBindingCreated(@NonNull FragmentAllTimetablesBinding binding) {
        this.allTimetablesRecyclerView = binding.allTimetablesRecyclerView;
        this.sharedViewModel = getSharedViewModelFromProvider();
    }

    private SharedViewModel getSharedViewModelFromProvider() {
        if (getActivity() != null) {
            Application application = getActivity().getApplication();
            if (application != null) {
                return new ViewModelProvider(getActivity(),
                        new ViewModelProvider.NewInstanceFactory()
                ).get(SharedViewModel.class);
            }
        }
        throw new RuntimeException("Activity or application is null");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setActionBarTitle(R.string.all_timetables);
        getViewModel().getAllTimetablesLiveData()
                .observe(getViewLifecycleOwner(), this::initAdapter);
    }

    private void initAdapter(List<Timetable> timetables) {
        if (timetables == null) {
            return;
        }
        AllTimetablesRecyclerViewAdapter currentAdapter = getAllTimetablesRecyclerViewAdapter();
        if (currentAdapter == null) {
            allTimetablesRecyclerView.setAdapter(
                    new AllTimetablesRecyclerViewAdapter(timetables)
            );
        } else {
            currentAdapter.notifyListUpdated(timetables);
        }
    }

    @SuppressWarnings("rawtypes")
    private AllTimetablesRecyclerViewAdapter getAllTimetablesRecyclerViewAdapter() {
        RecyclerView.Adapter adapter = allTimetablesRecyclerView.getAdapter();
        if (adapter instanceof AllTimetablesRecyclerViewAdapter) {
            return (AllTimetablesRecyclerViewAdapter) adapter;
        } else {
            return null;
        }
    }
}