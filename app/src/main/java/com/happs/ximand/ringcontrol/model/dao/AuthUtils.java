package com.happs.ximand.ringcontrol.model.dao;

import java.util.UUID;

import static android.os.Build.BOARD;
import static android.os.Build.BRAND;
import static android.os.Build.DEVICE;
import static android.os.Build.DISPLAY;
import static android.os.Build.HOST;
import static android.os.Build.ID;
import static android.os.Build.MANUFACTURER;
import static android.os.Build.MODEL;
import static android.os.Build.PRODUCT;
import static android.os.Build.TAGS;
import static android.os.Build.TYPE;
import static android.os.Build.USER;
import static android.os.Build.getRadioVersion;

public final class AuthUtils {

    public static final String UUID = getUUID();

    private static String getUUID() {
        return new UUID(getUniqueDeviceId().hashCode(), getRadioVersion().hashCode()).toString();
    }

    private static String getUniqueDeviceId() {
        return "35" +
                BOARD.length() % 10 +
                BRAND.length() % 10 +
                DEVICE.length() % 10 +
                DISPLAY.length() % 10 +
                HOST.length() % 10 +
                ID.length() % 10 +
                MANUFACTURER.length() % 10 +
                MODEL.length() % 10 +
                PRODUCT.length() % 10 +
                TAGS.length() % 10 +
                TYPE.length() % 10 +
                USER.length() % 10;
    }
}
