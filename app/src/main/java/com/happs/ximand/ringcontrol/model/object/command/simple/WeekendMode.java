package com.happs.ximand.ringcontrol.model.object.command.simple;

public enum WeekendMode {
    MODE_NOT_WORK_ON_WEEKENDS((byte) 0),
    MODE_WORK_ON_SATURDAY((byte) 1),
    MODE_WORK_ON_WEEKENDS((byte) 2);

    private byte modeId;

    WeekendMode(byte mode) {
        this.modeId = mode;
    }

    public static WeekendMode getInstanceForModeId(int modeId) {
        for (WeekendMode weekendMode : WeekendMode.values()) {
            if (weekendMode.getModeId() == modeId) {
                return weekendMode;
            }
        }
        throw new IllegalArgumentException();
    }

    public byte getModeId() {
        return modeId;
    }
}
