package com.happs.ximand.ringcontrol.viewmodel.fragment;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.AndroidViewModel;

import com.happs.ximand.ringcontrol.SingleLiveEvent;
import com.happs.ximand.ringcontrol.model.object.Timetable;
import com.happs.ximand.ringcontrol.model.repository.Repository;
import com.happs.ximand.ringcontrol.model.repository.impl.FakeTimetableRepository;
import com.happs.ximand.ringcontrol.view.BaseFragment;

public abstract class BaseFragmentViewModel extends AndroidViewModel implements Observable {

    private static final String PREFERENCES = "PREFERENCES";
    static final String PREF_APPLIED_TIMETABLE = "APPLIED_TIMETABLE_ID";

    @SuppressWarnings("rawtypes")
    @Deprecated
    private final SingleLiveEvent<BaseFragment> replaceFragmentLiveEvent;
    @Deprecated
    private final SingleLiveEvent<Void> pressBackEvent;

    private final SharedPreferences dataSharedPreferences;

    private transient PropertyChangeRegistry callbacks;

    BaseFragmentViewModel(@NonNull Application application) {
        super(application);
        this.replaceFragmentLiveEvent = new SingleLiveEvent<>();
        this.pressBackEvent = new SingleLiveEvent<>();
        this.dataSharedPreferences = application
                .getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }

    /**
     * @deprecated use FragmentNavigation.getInstance().navigateToFragment(BaseFragment f)
     * to replace fragment
     */
    @SuppressWarnings("rawtypes")
    @Deprecated
    public SingleLiveEvent<BaseFragment> getReplaceFragmentLiveEvent() {
        return replaceFragmentLiveEvent;
    }

    @Deprecated
    public SingleLiveEvent<Void> getPressBackEvent() {
        return pressBackEvent;
    }

    public boolean onOptionsItemSelected(int itemId) {
        return false;
    }

    int loadAppliedTimetableIdFromProperties() {
        return dataSharedPreferences.getInt(PREF_APPLIED_TIMETABLE, 1);
    }

    void updateAppliedTimetableId(int id) {
        dataSharedPreferences.edit()
                .putInt(PREF_APPLIED_TIMETABLE, id)
                .apply();
    }

    private SharedPreferences getSharedPreferences() {
        return getApplication().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }

    /**
     * @deprecated use FragmentNavigation.getInstance().navigateToFragment(BaseFragment f)
     * to replace fragment
     */
    @SuppressWarnings("rawtypes")
    @Deprecated
    void replaceFragment(BaseFragment newFragment) {
        replaceFragmentLiveEvent.setValue(newFragment);
    }

    Repository<Timetable> getTimetableRepository() {
        return FakeTimetableRepository.getInstance();
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
