package com.happs.ximand.ringcontrol.viewmodel.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.happs.ximand.ringcontrol.SingleLiveEvent;
import com.happs.ximand.ringcontrol.model.object.exception.bl.BluetoothException;

public class ExceptionViewModel extends BaseViewModel {

    private final SingleLiveEvent<Void> restartApplicationLiveEvent;
    private MutableLiveData<BluetoothException> exceptionLiveData = new MutableLiveData<>();

    public ExceptionViewModel() {
        this.restartApplicationLiveEvent = new SingleLiveEvent<>();
    }

    public SingleLiveEvent<Void> getRestartApplicationLiveEvent() {
        return restartApplicationLiveEvent;
    }

    public LiveData<BluetoothException> getExceptionLiveData() {
        return exceptionLiveData;
    }

    public void setException(BluetoothException exception) {
        exceptionLiveData.setValue(exception);
    }

    public void onRetryClick() {
        restartApplicationLiveEvent.call();
    }
}
