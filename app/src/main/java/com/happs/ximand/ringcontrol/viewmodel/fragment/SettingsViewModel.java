package com.happs.ximand.ringcontrol.viewmodel.fragment;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.snackbar.Snackbar;
import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.SingleLiveEvent;
import com.happs.ximand.ringcontrol.model.dao.BluetoothNDao;
import com.happs.ximand.ringcontrol.model.dao.SharedPreferencesDao;
import com.happs.ximand.ringcontrol.model.object.command.BluetoothCommand;
import com.happs.ximand.ringcontrol.model.object.command.ChangeRingDurationCommand;
import com.happs.ximand.ringcontrol.model.object.command.SendDateCommand;
import com.happs.ximand.ringcontrol.model.object.command.simple.ChangeManualModeCommand;
import com.happs.ximand.ringcontrol.model.object.command.simple.ChangeWeekendModeCommand;
import com.happs.ximand.ringcontrol.model.object.command.simple.MakeRingCommand;
import com.happs.ximand.ringcontrol.model.object.command.simple.WeekendMode;
import com.happs.ximand.ringcontrol.model.object.response.Response;
import com.happs.ximand.ringcontrol.viewmodel.dto.InputAlertDialogDto;
import com.happs.ximand.ringcontrol.viewmodel.dto.SelectAlertDialogDto;
import com.happs.ximand.ringcontrol.viewmodel.dto.SnackbarDto;

import java.util.Calendar;

import me.aflak.bluetooth.Bluetooth;

public class SettingsViewModel extends BaseViewModel implements Bluetooth.CommunicationCallback {

    private final SingleLiveEvent<Integer> inputIncorrectEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> dismissDialogEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<InputAlertDialogDto> setRingDurationEvent =
            new SingleLiveEvent<>();
    private final SingleLiveEvent<SelectAlertDialogDto> setWeekendModeEvent =
            new SingleLiveEvent<>();
    private final SingleLiveEvent<InputAlertDialogDto> setManualRingDurationEvent =
            new SingleLiveEvent<>();

    private final MutableLiveData<Boolean> sendDataPossible = new MutableLiveData<>();
    private final MutableLiveData<Integer> ringDurationLiveData = new MutableLiveData<>();
    private final MutableLiveData<WeekendMode> weekendModeLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> manualModeStateLiveData = new MutableLiveData<>();

    @SuppressWarnings("rawtypes")
    private BluetoothCommand sentBluetoothCommand;

    public SettingsViewModel() {
        this.sendDataPossible.setValue(BluetoothNDao.getInstance().isDeviceConnected());
        this.ringDurationLiveData.setValue(SharedPreferencesDao.getInstance().getRingDuration());
        this.weekendModeLiveData.setValue(getCurrentWeekendMode());
        this.manualModeStateLiveData
                .setValue(SharedPreferencesDao.getInstance().getManualModeState());
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

    public MutableLiveData<Boolean> getSendDataPossible() {
        return sendDataPossible;
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

    public SingleLiveEvent<InputAlertDialogDto> getSetManualRingDurationEvent() {
        return setManualRingDurationEvent;
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
        BluetoothNDao.getInstance().sendMessage(command.getCommand());
        sentBluetoothCommand = command;
        saveNewRingDuration();
    }

    private void saveNewRingDuration() {
        int duration = ((ChangeRingDurationCommand) sentBluetoothCommand)
                .getMainContent();
        ringDurationLiveData.setValue(duration);
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
        BluetoothNDao.getInstance().sendMessage(command.getCommand());
        sentBluetoothCommand = command;
        saveNewWeekendMode();
    }

    private void saveNewWeekendMode() {
        int modeId = ((ChangeWeekendModeCommand) sentBluetoothCommand)
                .getMainContent();
        weekendModeLiveData.setValue(WeekendMode.getInstanceForModeId(modeId));
        SharedPreferencesDao.getInstance().updateWeekendMode(modeId);
    }

    public void updateManualModeState() {
        boolean newManualModeState = !manualModeStateLiveData.getValue();
        ChangeManualModeCommand command = new ChangeManualModeCommand(newManualModeState);
        BluetoothNDao.getInstance().sendMessage(command.getCommand());
        sentBluetoothCommand = command;
        saveManualModeState();
    }

    private void saveManualModeState() {
        boolean newState = convertByteToBoolean(((ChangeManualModeCommand) sentBluetoothCommand)
                .getMainContent());
        manualModeStateLiveData.setValue(newState);
        SharedPreferencesDao.getInstance().updateManualMode(newState);
    }

    public void makeManualRing() {
        setManualRingDurationEvent.setValue(
                new InputAlertDialogDto()
                        .setTitleResId(R.string.ring_duration)
                        .setInputCompleteListener(this::onManualRingDurationComplete)
                        .setCancelListener(this::onChangeCanceled)
        );
    }

    private void onManualRingDurationComplete(String input) {
        try {
            int duration = Integer.parseInt(input);
            if (duration < 3) {
                inputIncorrectEvent.setValue(R.string.too_small_value_n);
            } else if (duration > 59) {
                inputIncorrectEvent.setValue(R.string.too_big_value_n);
            } else {
                dismissDialogEvent.call();
                sendMakeRingCommand(duration);
            }
        } catch (NumberFormatException e) {
            inputIncorrectEvent.setValue(R.string.wrong_number_format);
        }
    }

    private void sendMakeRingCommand(int d) {
        BluetoothNDao.getInstance().sendMessage(new MakeRingCommand((byte) d).getCommand());
    }

    public void sendNewDateTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        byte month = (byte) calendar.get(Calendar.MONTH);
        byte dateOfMonth = (byte) calendar.get(Calendar.DAY_OF_MONTH);
        byte hours = (byte) calendar.get(Calendar.HOUR_OF_DAY);
        byte minutes = (byte) calendar.get(Calendar.MINUTE);
        byte seconds = (byte) calendar.get(Calendar.SECOND);
        byte dow = (byte) calendar.get(Calendar.DAY_OF_WEEK);
        BluetoothNDao.getInstance().sendMessage(new SendDateCommand(
                (byte) (year % 100), month, dateOfMonth, hours, minutes, seconds, dow
        ).getCommand());
    }

    private void onChangeCanceled(Void aVoid) {
        getMakeSnackbarEvent().setValue(
                new SnackbarDto(R.string.changes_did_not_complet, Snackbar.LENGTH_SHORT)
        );
    }

    private boolean convertByteToBoolean(byte b) {
        return b == (byte) 0xFF;
    }

    private String convertByteToHexString(byte b) {
        return Integer.toHexString(b);
    }

    @Override
    public void onConnect(BluetoothDevice device) {
        Log.d("...", "connected");
    }

    @Override
    public void onDisconnect(BluetoothDevice device, String message) {
        Log.d("...", "disconnect");
    }

    @Override
    public void onMessage(String message) {
        Response response = Response.getResponseByMessage(message.getBytes());
        if (response.isSuccess()) {
            saveSentData(response);
        } else {
            getMakeSnackbarEvent().setValue(
                    new SnackbarDto(R.string.unsuccess_response, Snackbar.LENGTH_LONG)
                            .setFormat(true)
                            .setSingleArg(convertByteToHexString(response.getResponseCode()))
            );
        }
    }

    @Override
    public void onError(String message) {
        Log.d("...", "error");
    }

    @Override
    public void onConnectError(BluetoothDevice device, String message) {
        Log.d("...", "connectError");
    }

}
