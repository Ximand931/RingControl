package com.happs.ximand.ringcontrol.viewmodel.fragment;

import androidx.lifecycle.MutableLiveData;

import com.happs.ximand.ringcontrol.SingleLiveEvent;
import com.happs.ximand.ringcontrol.model.object.exception.BluetoothException;

public class ExceptionViewModel extends BaseViewModel {

    private final SingleLiveEvent<Void> restartApplicationLiveEvent;
    private MutableLiveData<BluetoothException> exceptionLiveData = new MutableLiveData<>();

    public ExceptionViewModel() {
        this.restartApplicationLiveEvent = new SingleLiveEvent<>();
    }

    public SingleLiveEvent<Void> getRestartApplicationLiveEvent() {
        return restartApplicationLiveEvent;
    }

    public MutableLiveData<BluetoothException> getExceptionLiveData() {
        return exceptionLiveData;
    }

    public String getDescription() {
        return "Stub"; //TODO: use BindingConverter?
    }

    public void setException(BluetoothException exception) {
        exceptionLiveData.setValue(exception);
    }

    public void onRetryClick() {
        restartApplicationLiveEvent.call();
    }
}
