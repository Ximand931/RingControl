package com.happs.ximand.ringcontrol.model.dao;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import me.aflak.bluetooth.Bluetooth;

public class BluetoothNDao {

    private static BluetoothNDao instance;
    private final Bluetooth bluetooth;
    @Nullable
    private BluetoothDevice pairedDevice;
    @Nullable
    private MessageListenerThread messageListenerThread;

    private BluetoothNDao(Activity activity) {
        this.bluetooth = new Bluetooth(activity);
    }

    public static void initialize(Activity activity) {
        instance = new BluetoothNDao(activity);
    }

    public static BluetoothNDao getInstance() {
        if (instance == null) {
            throw new IllegalStateException();
        }
        return instance;
    }

    public void enableBluetooth() {
        bluetooth.enableBluetooth();
    }

    public void startScanning(@NonNull Bluetooth.DiscoveryCallback discoveryCallback) {
        bluetooth.setDiscoveryCallback(discoveryCallback);
        bluetooth.scanDevices();
    }

    public void setPairedDevice(@Nullable BluetoothDevice device) {
        this.pairedDevice = device;
    }

    public void unpairDevice(BluetoothDevice device) {
        if (device.equals(pairedDevice)) {
            this.pairedDevice = null;
        }
    }

    public void startConnecting(BluetoothDevice device,
                                @Nullable Bluetooth.CommunicationCallback communicationCallback) {
        bluetooth.connectToDevice(device);
        if (communicationCallback != null) {
            bluetooth.setCommunicationCallback(communicationCallback);
        }
    }

    public void startConnecting(String address,
                                @Nullable Bluetooth.CommunicationCallback communicationCallback) {
        bluetooth.connectToAddress(address);
        if (communicationCallback != null) {
            bluetooth.setCommunicationCallback(communicationCallback);
        }
    }

    public void setCommunicationCallback(Bluetooth.CommunicationCallback callback) {
        bluetooth.setCommunicationCallback(callback);
    }

    public void sendMessage(byte[] message) {
        Log.d("...", "message: " + new String(message));
        try {
            OutputStream outputStream = bluetooth.getSocket().getOutputStream();
            for (byte b : message) {
                outputStream.write(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setIncomingMessagesListener(MessageListener listener) {
        try {
            InputStream inputStream = bluetooth.getSocket().getInputStream();
            if (messageListenerThread != null) {
                messageListenerThread.setMessageListener(listener);
            } else {
                messageListenerThread = new MessageListenerThread(inputStream, listener);
                messageListenerThread.start();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean isDeviceConnected() {
        return bluetooth.isConnected();
    }

    public interface MessageListener {
        void onMessage(byte[] bytes);
    }

    private static class MessageListenerThread extends Thread {

        private final InputStream inputStream;
        private MessageListener messageListener;

        public MessageListenerThread(InputStream inputStream, MessageListener messageListener) {
            this.inputStream = inputStream;
            this.messageListener = messageListener;
        }

        private void setMessageListener(MessageListener messageListener) {
            this.messageListener = messageListener;
        }

        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    if (inputStream.available() > 0) {
                        byte[] bytes = new byte[inputStream.available()];
                        for (int i = 0; i < bytes.length; i++) {
                            bytes[i] = (byte) inputStream.read();
                        }
                        messageListener.onMessage(bytes);
                    }
                    sleep(500);
                } catch (InterruptedException e) {
                    break;
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        }

        void onClear() {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
