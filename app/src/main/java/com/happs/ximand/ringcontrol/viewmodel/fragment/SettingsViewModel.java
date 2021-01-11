package com.happs.ximand.ringcontrol.viewmodel.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.snackbar.Snackbar;
import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.SingleLiveEvent;
import com.happs.ximand.ringcontrol.model.dao.BluetoothDao;
import com.happs.ximand.ringcontrol.model.dao.BluetoothEventListener;
import com.happs.ximand.ringcontrol.model.dao.SharedPreferencesDao;
import com.happs.ximand.ringcontrol.model.object.command.BluetoothCommand;
import com.happs.ximand.ringcontrol.model.object.command.ChangeRingDurationCommand;
import com.happs.ximand.ringcontrol.model.object.command.simple.ChangeManualModeCommand;
import com.happs.ximand.ringcontrol.model.object.command.simple.ChangeWeekendModeCommand;
import com.happs.ximand.ringcontrol.model.object.command.simple.WeekendMode;
import com.happs.ximand.ringcontrol.model.object.info.BluetoothEvent;
import com.happs.ximand.ringcontrol.model.object.response.Response;
import com.happs.ximand.ringcontrol.viewmodel.dto.InputAlertDialogDto;
import com.happs.ximand.ringcontrol.viewmodel.dto.SelectAlertDialogDto;
import com.happs.ximand.ringcontrol.viewmodel.dto.SnackbarDto;

public class SettingsViewModel extends BaseViewModel {

    private final BluetoothDao bluetoothDao = BluetoothDao.getInstance();

    private final SingleLiveEvent<Integer> inputIncorrectEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> dismissDialogEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<InputAlertDialogDto> setRingDurationEvent =
            new SingleLiveEvent<>();
    private final SingleLiveEvent<SelectAlertDialogDto> setWeekendModeEvent =
            new SingleLiveEvent<>();

    private final BluetoothEventListener<BluetoothEvent> eventListener;
    private final BluetoothEventListener<Response> responseListener;

    private final MutableLiveData<BluetoothEvent> bluetoothEventLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> ringDurationLiveData = new MutableLiveData<>();
    private final MutableLiveData<WeekendMode> weekendModeLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> manualModeStateLiveData = new MutableLiveData<>();

    @SuppressWarnings("rawtypes")
    private BluetoothCommand sentBluetoothCommand;

    public SettingsViewModel() {
        this.eventListener = new BluetoothEventListener<>(this::onBluetoothEvent);
        this.responseListener = new BluetoothEventListener<>(this::onBluetoothResponse);
        this.ringDurationLiveData.setValue(SharedPreferencesDao.getInstance().getRingDuration());
        this.weekendModeLiveData.setValue(getCurrentWeekendMode());
        this.manualModeStateLiveData
                .setValue(SharedPreferencesDao.getInstance().getManualModeState());
        this.bluetoothDao.subscribeToResponses(responseListener);
        this.bluetoothDao.subscribeToEvents(eventListener);
    }

    private WeekendMode getCurrentWeekendMode() {
        int mode = SharedPreferencesDao.getInstance().getWeekendMode();
        return WeekendMode.getInstanceForModeId(mode);
    }

    public int getDescriptionForWeekendMode(WeekendMode weekendMode) {
        switch (weekendMode) {
            case MODE_NOT_WORK_ON_WEEKENDS:
                return R.string.mode_not_work_on_weekends;
            case MODE_WORK_ON_SATURDAY:
                return R.string.mode_work_on_saturday;
            case MODE_WORK_ON_WEEKENDS:
                return R.string.mode_work_on_weekends;
            default:
                throw new IllegalStateException();
        }
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

    public SingleLiveEvent<SelectAlertDialogDto> getSetWeekendModeEvent() {
        return setWeekendModeEvent;
    }

    public MutableLiveData<BluetoothEvent> getBluetoothEventLiveData() {
        return bluetoothEventLiveData;
    }

    public LiveData<Integer> getRingDurationLiveData() {
        return ringDurationLiveData;
    }

    public LiveData<WeekendMode> getWeekendModeLiveData() {
        return weekendModeLiveData;
    }

    public MutableLiveData<Boolean> getManualModeStateLiveData() {
        return manualModeStateLiveData;
    }

    private void onBluetoothEvent(BluetoothEvent event) {
        bluetoothEventLiveData.postValue(event);
    }

    private void onBluetoothResponse(Response response) {
        if (response.isSuccess()) {
            saveSentData(response);
        } else {
            getMakeSnackbarEvent().setValue(
                    new SnackbarDto(R.string.unsuccess_response, Snackbar.LENGTH_LONG)
            );
        }
    }

    private void saveSentData(Response response) {
        switch (response.getCommandCode()) {
            case ChangeRingDurationCommand.COMMAND_CODE:
                saveNewRingDuration();
                break;
            case ChangeWeekendModeCommand.COMMAND_CODE:
                saveNewWeekendMode();
                break;
            case ChangeManualModeCommand.COMMAND_CODE:
                saveManualModeState();
                break;
        }
    }

    public void updateRingDuration() {
        setRingDurationEvent.setValue(new InputAlertDialogDto()
                .setTitleResId(R.string.ring_duration)
                .setInputCompleteListener(this::onRingDurationFilled)
                .setCancelListener(this::onChangeCanceled)
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
                sendNewRingDurationToDevice(duration);
            }
        } catch (NumberFormatException e) {
            inputIncorrectEvent.setValue(R.string.wrong_number_format);
        }
    }

    private void sendNewRingDurationToDevice(int duration) {
        ChangeRingDurationCommand command = new ChangeRingDurationCommand(duration);
        bluetoothDao.sendMessage(command.getCommand());
        sentBluetoothCommand = command;
    }

    private void saveNewRingDuration() {
        int duration = ((ChangeRingDurationCommand) sentBluetoothCommand)
                .getMainContent();
        SharedPreferencesDao.getInstance().updateRingDuration(duration);
    }

    public void updateWeekendMode() {
        setWeekendModeEvent.setValue(new SelectAlertDialogDto()
                .setTitleResId(R.string.select_weekend_mode)
                .setItemsArrayResId(R.array.weekend_mods)
                .setSelectListener(this::onWeekendModeSelected)
                .setCancelListener(this::onChangeCanceled)
        );
    }

    private void onWeekendModeSelected(int modeId) {
        ChangeWeekendModeCommand command = new ChangeWeekendModeCommand((byte) modeId);
        bluetoothDao.sendMessage(command.getCommand());
        sentBluetoothCommand = command;
    }

    private void saveNewWeekendMode() {
        int modeId = ((ChangeWeekendModeCommand) sentBluetoothCommand)
                .getMainContent();
        SharedPreferencesDao.getInstance().updateWeekendMode(modeId);
    }

    public void updateManualModeState() {
        @SuppressWarnings("ConstantConditions")
        boolean newManualModeState = !manualModeStateLiveData.getValue();
        ChangeManualModeCommand command = new ChangeManualModeCommand(newManualModeState);
        bluetoothDao.sendMessage(command.getCommand());
        sentBluetoothCommand = command;
    }

    private void saveManualModeState() {
        boolean newState = convertByteToBoolean(((ChangeManualModeCommand) sentBluetoothCommand)
                .getMainContent());
        SharedPreferencesDao.getInstance().updateManualMode(newState);
    }

    private void onChangeCanceled(Void aVoid) {
        getMakeSnackbarEvent().setValue(
                new SnackbarDto(R.string.changes_did_not_complet, Snackbar.LENGTH_SHORT)
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        bluetoothDao.unsubscribeFromEvents(eventListener);
        bluetoothDao.unsubscribeFromResponses(responseListener);
    }

    private boolean convertByteToBoolean(byte b) {
        return b == (byte) 0xFF;
    }
}
