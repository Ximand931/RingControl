package com.happs.ximand.ringcontrol.viewmodel.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.snackbar.Snackbar;
import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.SingleLiveEvent;
import com.happs.ximand.ringcontrol.model.dao.BluetoothEventListener;
import com.happs.ximand.ringcontrol.model.dao.SharedPreferencesDao;
import com.happs.ximand.ringcontrol.model.object.info.BluetoothEvent;
import com.happs.ximand.ringcontrol.viewmodel.InputAlertDialogDto;
import com.happs.ximand.ringcontrol.viewmodel.SnackbarDto;

public class SettingsViewModel extends BaseViewModel {

    private final SingleLiveEvent<Integer> inputIncorrectEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> dismissDialogEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<InputAlertDialogDto> setRingDurationEvent =
            new SingleLiveEvent<>();

    private final BluetoothEventListener<BluetoothEvent> eventListener;
    private final BluetoothEventListener<String> messagesListener;

    private final MutableLiveData<BluetoothEvent> bluetoothEventLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> ringDurationLiveData = new MutableLiveData<>();

    public SettingsViewModel() {
        this.eventListener = new BluetoothEventListener<>(this::onBluetoothEvent);
        this.messagesListener = new BluetoothEventListener<>(this::onBluetoothMessage);
        this.ringDurationLiveData.setValue(SharedPreferencesDao.getInstance().getRingDuration());
    }

    public SingleLiveEvent<Integer> getInputIncorrectEvent() {
        return inputIncorrectEvent;
    }

    public SingleLiveEvent<Void> getDismissDialogEvent() {
        return dismissDialogEvent;
    }

    public SingleLiveEvent<InputAlertDialogDto> getSetRingDurationEvent() {
        return setRingDurationEvent;
    }

    public LiveData<Integer> getRingDurationLiveData() {
        return ringDurationLiveData;
    }

    private void onBluetoothEvent(BluetoothEvent event) {
        bluetoothEventLiveData.postValue(event);
    }

    private void onBluetoothMessage(String message) {

    }

    public void setRingDuration() {
        setRingDurationEvent.setValue(new InputAlertDialogDto()
                .setTitleResId(R.string.ring_duration)
                .setOnCompleteInput(this::onRingDurationFilled)
                .setOnCancel(this::onChangeCanceled)
        );
    }

    private void onRingDurationFilled(String inputDuration) {
        try {
            int duration = Integer.parseInt(inputDuration);
            if (duration < 500) {
                inputIncorrectEvent.setValue(R.string.too_small_value);
            } else if (duration > 30000) {
                inputIncorrectEvent.setValue(R.string.too_big_value);
            } else {
                dismissDialogEvent.call();
                sendNewRingDurationToDevice();
            }
        } catch (NumberFormatException e) {
            inputIncorrectEvent.setValue(R.string.wrong_number_format);
        }
    }

    private void sendNewRingDurationToDevice() {

    }

    private void onChangeCanceled(Void aVoid) {
        getMakeSnackbarEvent().setValue(
                new SnackbarDto(R.string.changes_did_not_complet, Snackbar.LENGTH_SHORT)
        );
    }

}
