package com.happs.ximand.ringcontrol.model.object.command;

public class SendDateCommand extends BluetoothCommand<String> {

    private static final byte SEND_TIME_COMMAND_CODE = 30;

    private final byte yearLastTwoDigits;
    private final byte month;
    private final byte dateOfMonth;
    private final byte hours;
    private final byte minutes;
    private final byte seconds;
    private final byte dateOfWeek;

    public SendDateCommand(byte yearLastTwoDigits, byte month, byte dateOfMonth,
                           byte hours, byte minutes, byte seconds, byte dateOfWeek) {
        super(SEND_TIME_COMMAND_CODE);
        this.yearLastTwoDigits = yearLastTwoDigits;
        this.month = month;
        this.dateOfMonth = dateOfMonth;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.dateOfWeek = dateOfWeek;
    }

    @Override
    public byte[] getCommand() {
        return new byte[]{
                getCode(), yearLastTwoDigits, month, dateOfMonth, hours, minutes, seconds, dateOfWeek
        };
    }

    @Override
    public String getMainContent() {
        return hours + ":" + minutes + ":" + seconds;
    }
}
