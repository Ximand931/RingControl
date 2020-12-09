package com.happs.ximand.ringcontrol.view.fragment;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.happs.ximand.ringcontrol.viewmodel.item.ItemViewModelFactory;

import java.util.List;

public class AllTimetablesFragment
        extends BaseFragment<AllTimetablesViewModel, FragmentAllTimetablesBinding> {

    public static final String FRAGMENT_TAG = "AllTimetables";

    public static final int EVENT_APPLIED_TIMETABLE_UPDATED = 1;
    public static final int EVENT_TIMETABLE_LIST_UPDATED = 2;

    private static final String SHARED_PREF_NAME = "SHARED_PREF_DATA";
    private static final String SHARED_PREF_FIELD_APPLIED_ID = "FIELD_APPLIED_TIMETABLE_ID";

    private SharedPreferences sharedPreferences;
    private SharedViewModel sharedViewModel;
    private RecyclerView allTimetablesRecyclerView;

    public AllTimetablesFragment() {
        super(R.layout.fragment_all_timetables, FRAGMENT_TAG);
    }

    public static AllTimetablesFragment newInstance() {
        return new AllTimetablesFragment();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar_main, menu);
    }

    @Override
    protected void onViewDataBindingCreated(@NonNull FragmentAllTimetablesBinding binding) {
        this.allTimetablesRecyclerView = binding.allTimetablesRecyclerView;
        this.sharedViewModel = getSharedViewModelFromProvider();
        if (getContext() != null) {
            this.sharedPreferences = getContext()
                    .getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        }
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
        subscribeToViewModelsFields();
    }

    private void subscribeToViewModelsFields() {
        getViewModel().getAllTimetablesLiveData()
                .observe(getViewLifecycleOwner(), this::onTimetableListUpdated);
    }

    private void onTimetableListUpdated(List<Timetable> timetables) {
        if (timetables == null) {
            return;
        }
        AllTimetablesRecyclerViewAdapter currentAdapter = getAllTimetablesRecyclerViewAdapter();
        if (currentAdapter == null) {
            initRecyclerViewAdapter(timetables);
        } else {
            currentAdapter.updateTimetables(
                    createTimetablesItemViewModels(timetables)
            );
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

    @SuppressWarnings("rawtypes")
    private void initRecyclerViewAdapter(List<Timetable> timetables) {
        RecyclerView.Adapter adapter = new AllTimetablesRecyclerViewAdapter(
                createTimetablesItemViewModels(timetables)
        );
        allTimetablesRecyclerView.setAdapter(adapter);
    }

    private List<NewTimetableItemViewModel> createTimetablesItemViewModels(
            List<Timetable> timetables) {
        TimetableItemViewModelFactory factory = new TimetableItemViewModelFactory(
                loadAppliedTimetableId(), sharedViewModel.isTimetableApplyingPossible()
        );
        return factory.createViewModelsByItemList(timetables);
    }

    private int loadAppliedTimetableId() {
        return sharedPreferences.getInt(SHARED_PREF_FIELD_APPLIED_ID, -2);
    }

    private static class TimetableItemViewModelFactory
            extends ItemViewModelFactory<NewTimetableItemViewModel, Timetable> {

        private final int appliedTimetableId;
        private final boolean applyingPossible;

        public TimetableItemViewModelFactory(int appliedTimetableId, boolean applyingPossible) {
            this.appliedTimetableId = appliedTimetableId;
            this.applyingPossible = applyingPossible;
        }

        @Override
        public NewTimetableItemViewModel createViewModelByItem(Timetable timetable) {
            NewTimetableItemViewModel viewModel =
                    NewTimetableItemViewModel.createFromTimetable(timetable);
            viewModel.setApplyingTimetable(isAppliedTimetable(timetable));
            viewModel.setApplyingPossible(applyingPossible);
            return viewModel;
        }

        private boolean isAppliedTimetable(Timetable timetable) {
            return appliedTimetableId == timetable.getId();
        }
    }

    /*
    public static final String FRAGMENT_TAG = "AllTimetables";
    public static final int EVENT_APPLIED_TIMETABLE_UPDATED = 1;
    public static final int EVENT_TIMETABLE_LIST_UPDATED = 2;

    private RecyclerView allTimetablesRecyclerView;
    private TimetableItemViewModelFactory itemViewModelFactory;

    public AllTimetablesFragment() {
        super(R.layout.fragment_all_timetables, FRAGMENT_TAG);
        this.itemViewModelFactory = new TimetableItemViewModelFactory();
    }

    public static AllTimetablesFragment newInstance() {
        return new AllTimetablesFragment();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar_main, menu);
    }

    private void onConnectStatusUpdated(ConnectStatus connectStatus) {
        if (connectStatus == ConnectStatus.CONNECTED) {
            notifyRecyclerViewDeviceConnected();
        }
    }

    private void notifyRecyclerViewDeviceConnected() {
        AllTimetablesRecyclerViewAdapter adapter = getCurrentAdapter();
        if (adapter != null) {
            List<TimetableItemViewModel> viewModels = adapter.getViewModels();
            for (TimetableItemViewModel viewModel : viewModels) {
                viewModel.setConnected(true);
            }
        }
    }

    @Override
    protected void onViewDataBindingCreated(@NonNull FragmentAllTimetablesBinding binding) {
        SharedViewModel sharedViewModel = getSharedViewModelUsingProvider();
        binding.setSharedViewModel(sharedViewModel);
        if (sharedViewModel.getConnectStatusLiveData().getValue() == ConnectStatus.CONNECTED) {
            binding.testApply.setEnabled(true);
            onConnectStatusUpdated(ConnectStatus.CONNECTED);
        }
        sharedViewModel.getConnectStatusLiveData().observe(
                getViewLifecycleOwner(), connectStatus -> {
                    if (connectStatus == ConnectStatus.CONNECTED) {
                        binding.testApply.setEnabled(true);
                    }
                    onConnectStatusUpdated(connectStatus);
                }
        );
        allTimetablesRecyclerView = binding.allTimetablesRecyclerView;
        allTimetablesRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext())
        );
    }

    protected SharedViewModel getSharedViewModelUsingProvider() {
        if (getActivity() != null) {
            Application application = getActivity().getApplication();
            if (application != null) {
                return new ViewModelProvider(
                        getActivity(),
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
        getViewModel().getAllTimetablesLiveData().observe(
                getViewLifecycleOwner(), this::onTimetableListUpdated
        );
    }

    private void onTimetableListUpdated(List<Timetable> timetables) {
        if (timetables == null) {
            return;
        }

        AllTimetablesRecyclerViewAdapter currentAdapter = getCurrentAdapter();
        List<TimetableItemViewModel> viewModels = itemViewModelFactory
                .createViewModelsByItemList(timetables);
        if (currentAdapter == null) {
            initRecyclerViewAdapter(viewModels);
        } else {
            currentAdapter.updateViewModelList(viewModels);
        }
    }

    @Nullable
    private AllTimetablesRecyclerViewAdapter getCurrentAdapter() {
        return (AllTimetablesRecyclerViewAdapter)
                allTimetablesRecyclerView.getAdapter();
    }

    private void initRecyclerViewAdapter(List<TimetableItemViewModel> viewModels) {
        AllTimetablesRecyclerViewAdapter adapter =
                new AllTimetablesRecyclerViewAdapter(viewModels);
        allTimetablesRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onExternalEvent(int eventId) {
        switch (eventId) {
            case EVENT_APPLIED_TIMETABLE_UPDATED:
                showUpdatedAppliedTimetableSnackbar();
                break;
            case EVENT_TIMETABLE_LIST_UPDATED:
                getViewModel().updateTimetables();
                break;
        }
    }

    private void showUpdatedAppliedTimetableSnackbar() {
        if (getView() != null) {
            Snackbar snackbar = Snackbar
                    .make(getView(), R.string.applied_timetable_updated, Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.apply,
                    v -> getViewModel().applyUpdatedCurrentTimetable());
            snackbar.show();
        }
    }

    private class TimetableItemViewModelFactory
            extends ItemViewModelFactory<TimetableItemViewModel, Timetable> {

        @Override
        public TimetableItemViewModel createViewModelByItem(Timetable item) {
            boolean isLastApplied = getViewModel().isLastAppliedTimetable(item);
            TimetableItemViewModel viewModel =
                    new TimetableItemViewModel(item, isLastApplied);
            viewModel.setOnDetailsClickEvent(getViewModel()::showTimetableDetails);
            viewModel.setOnApplyClickEvent(getViewModel()::applyTimetable);
            return viewModel;
        }
    }

     */
}
