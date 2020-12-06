package com.happs.ximand.ringcontrol.model.bl;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.lang.reflect.Constructor;

public class FakeBluetoothContainer {

    private static final String FAKE_ADDRESS = "02:00:00:00:00:00";

    public static BluetoothDevice getFakeDevice() {
        try {
            Constructor<BluetoothDevice> bluetoothDeviceConstructor =
                    BluetoothDevice.class.getDeclaredConstructor(String.class);
            bluetoothDeviceConstructor.setAccessible(true);
            return bluetoothDeviceConstructor.newInstance(FAKE_ADDRESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
