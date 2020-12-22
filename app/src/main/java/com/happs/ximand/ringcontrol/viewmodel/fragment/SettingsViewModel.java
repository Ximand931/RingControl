package com.happs.ximand.ringcontrol.viewmodel.fragment;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.SingleLiveEvent;
import com.happs.ximand.ringcontrol.model.dao.SharedPreferencesDao;
import com.happs.ximand.ringcontrol.viewmodel.InputAlertDialogDto;

public class SettingsViewModel extends BaseViewModel {

    private final SingleLiveEvent<InputAlertDialogDto> setRingDurationEvent =
            new SingleLiveEvent<>();

    private final MutableLiveData<Integer> ringDurationLiveData = new MutableLiveData<>();

    public SettingsViewModel() {
        this.ringDurationLiveData.setValue(SharedPreferencesDao.getInstance().getRingDuration());
    }

    public SingleLiveEvent<InputAlertDialogDto> getSetRingDurationEvent() {
        return setRingDurationEvent;
    }

    public LiveData<Integer> getRingDurationLiveData() {
        return ringDurationLiveData;
    }

    public void setRingDuration() {
        setRingDurationEvent.setValue(new InputAlertDialogDto()
                .setTitleResId(R.string.ring_duration)
                .setPositiveButtonResId(R.string.apply)
                .setNegativeButtonResId(R.string.cancel)
                .setOnCompleteInput(s -> Log.d("....", "MESSAGE: " + s))
        );
    }

}
