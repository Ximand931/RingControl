package com.happs.ximand.ringcontrol.viewmodel.fragment;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.ViewModel;

import com.happs.ximand.ringcontrol.SingleLiveEvent;
import com.happs.ximand.ringcontrol.model.object.Timetable;
import com.happs.ximand.ringcontrol.model.repository.Repository;
import com.happs.ximand.ringcontrol.model.repository.impl.FakeTimetableRepository;
import com.happs.ximand.ringcontrol.view.BaseFragment;

public abstract class BaseFragmentViewModel extends ViewModel implements Observable {

    private static final String PREFERENCES = "PREFERENCES";
    static final String PREF_APPLIED_TIMETABLE = "APPLIED_TIMETABLE_ID";

    @Deprecated
    protected Application app;

    @SuppressWarnings("rawtypes")
    @Deprecated
    private SingleLiveEvent<BaseFragment> replaceFragmentLiveEvent;
    @Deprecated
    private SingleLiveEvent<Void> pressBackEvent;
    @Deprecated
    private SharedPreferences dataSharedPreferences;

    private transient PropertyChangeRegistry callbacks;

    @Deprecated
    BaseFragmentViewModel(@NonNull Application application) {
        this.app = application;
        this.replaceFragmentLiveEvent = new SingleLiveEvent<>();
        this.pressBackEvent = new SingleLiveEvent<>();
        this.dataSharedPreferences = application
                .getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }

    public BaseFragmentViewModel() {
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    protected <T extends Application> T getApplication() {
        return (T) app;
    }

    public boolean onOptionsItemSelected(int itemId) {
        return false;
    }

    @Deprecated
    void updateAppliedTimetableId(int id) {
        dataSharedPreferences.edit()
                .putInt(PREF_APPLIED_TIMETABLE, id)
                .apply();
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        synchronized (this) {
            if (callbacks == null) {
                callbacks = new PropertyChangeRegistry();
            }
        }
        callbacks.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        synchronized (this) {
            if (callbacks == null) {
                callbacks = new PropertyChangeRegistry();
            }
        }
        callbacks.remove(callback);
    }

    void notifyPropertyChanged(int fieldId) {
        synchronized (this) {
            if (callbacks == null) {
                return;
            }
        }
        callbacks.notifyCallbacks(this, fieldId, null);
    }
}
