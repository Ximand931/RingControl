package com.happs.ximand.ringcontrol.view;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.happs.ximand.ringcontrol.BR;
import com.happs.ximand.ringcontrol.viewmodel.fragment.BaseFragmentViewModel;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;

public abstract class BaseFragment<VM extends BaseFragmentViewModel, B extends ViewDataBinding>
        extends Fragment {

    private final int layoutId;
    private final int menuResId;
    @Nullable
    private VM viewModel;

    public BaseFragment(int layoutId, int menuResId) {
        this.layoutId = layoutId;
        this.menuResId = menuResId;
    }

    @NonNull
    protected VM getViewModel() {
        if (viewModel == null) {
            onNotInitializedField();
        }
        return viewModel;
    }

    private void onNotInitializedField() {
        throw new RuntimeException(
                "field of class: " + getClass().getName()
                        + " is null, check that method is called after onCreate()"
        );
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(menuResId, menu);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setHasOptionsMenu(true);
        viewModel = createFragmentViewModel();
    }

    protected VM createFragmentViewModel() {
        if (getActivity() != null) {
            Application application = getActivity().getApplication();
            if (application != null) {
                return new ViewModelProvider(
                        getActivity(),
                        new ViewModelProvider.AndroidViewModelFactory(application)
                ).get(getGenericClass());
            }
        }
        throw new RuntimeException("Activity or application is null");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        B viewDataBinding = DataBindingUtil
                .inflate(inflater, layoutId, container, false);
        onViewDataBindingCreated(viewDataBinding);

        onPreViewModelAttaching(getViewModel());
        viewDataBinding.setVariable(BR.viewModel, viewModel);

        return viewDataBinding.getRoot();
    }

    protected void onViewDataBindingCreated(@NonNull B binding) {

    }

    protected void onPreViewModelAttaching(@NonNull VM viewModel) {

    }

    public void onExternalEvent(int eventId) {

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return getViewModel().notifyOptionsMenuItemClicked(item.getItemId());
    }

    public String getDefaultTag() {
        return this.getClass().getSimpleName();
    }

    protected void setActionBarTitle(int resId) {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(resId);
        }
    }

    protected void setActionBarTitle(String string) {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(string);
        }
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getCheckedActivity()).getSupportActionBar();
    }

    private Activity getCheckedActivity() {
        Activity activity = getActivity();
        if (activity != null) {
            return activity;
        } else {
            throw new RuntimeException("Unable to continue; activity was destroyed");
        }
    }

    @SuppressWarnings("unchecked")
    @NonNull
    private Class<VM> getGenericClass() {
        return (Class<VM>)
                ((ParameterizedType) Objects.requireNonNull(
                        getClass().getGenericSuperclass())
                ).getActualTypeArguments()[0];
    }
}
