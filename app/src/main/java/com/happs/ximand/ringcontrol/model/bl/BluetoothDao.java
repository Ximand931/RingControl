package com.happs.ximand.ringcontrol.model.bl;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import androidx.lifecycle.MutableLiveData;

import com.happs.ximand.ringcontrol.OnEventListener;
import com.happs.ximand.ringcontrol.viewmodel.ConnectStatus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class BluetoothDao {

    private static BluetoothDao instance;

    private final List<OnEventListener<String>> messageReceiveListeners = new ArrayList<>();
    private MutableLiveData<ConnectStatus> connectStatus;
    private BluetoothSocket bluetoothSocket;
    private BluetoothThread bluetoothThread;

    private BluetoothDao() {
    }

    public static BluetoothDao getInstance() {
        if (instance == null) {
            instance = new BluetoothDao();
        }
        return instance;
    }

    public void setConnectStatus(MutableLiveData<ConnectStatus> connectStatus) {
        this.connectStatus = connectStatus;
    }

    public boolean isBluetoothEnable() {
        return BluetoothAdapter.getDefaultAdapter().enable();
    }

    public void startConnectToDeviceTask(BluetoothDevice target) {
        connectStatus.setValue(ConnectStatus.CONNECTING);
        new ConnectTaskThread(target, this::afterConnect)
                .start();
    }

    public void startConnectToDeviceTask(String deviceAddress) {
        Set<BluetoothDevice> devices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        for (BluetoothDevice device : devices) {
            if (device != null && device.getAddress().equals(deviceAddress)) {
                startConnectToDeviceTask(device);
                return;
            }
        }
    }

    public void subscribeToBluetoothMessages(OnEventListener<String> messageReceiveListener) {
        messageReceiveListeners.add(messageReceiveListener);
    }

    private void afterConnect(BluetoothSocket socket) {
        if (socket == null || !socket.isConnected()) {
            connectStatus.postValue(ConnectStatus.ERROR);
            return;
        }
        this.bluetoothSocket = socket;
        this.bluetoothThread = createBluetoothThreadBySocket();
        if (bluetoothThread != null) {
            bluetoothThread.start();
            this.connectStatus.postValue(ConnectStatus.CONNECTED);
        } else {
            this.connectStatus.postValue(ConnectStatus.ERROR);
        }
    }

    private BluetoothThread createBluetoothThreadBySocket() {
        try {
            bluetoothThread = new BluetoothThread(
                    new BufferedReader(new InputStreamReader(bluetoothSocket.getInputStream())),
                    new BufferedWriter(new OutputStreamWriter(bluetoothSocket.getOutputStream())),
                    messageReceiveListeners
            );
            return bluetoothThread;
        } catch (IOException e) {
            connectStatus.postValue(ConnectStatus.ERROR);
        }
        return null;
    }

    public void clear() {
        bluetoothThread.interrupt();
    }

    private static class ConnectTaskThread extends Thread {

        private static final UUID OBJ_UUID = UUID
                .fromString("00000000-0000-1000-8000-00805F9B34FB");

        private final BluetoothDevice target;
        private final OnEventListener<BluetoothSocket> connectEvent;

        public ConnectTaskThread(BluetoothDevice target,
                                 OnEventListener<BluetoothSocket> connectEvent) {
            this.target = target;
            this.connectEvent = connectEvent;
        }

        @Override
        public void run() {
            try {
                BluetoothSocket socket = target.createInsecureRfcommSocketToServiceRecord(OBJ_UUID);
                socket.connect();
                connectEvent.onEvent(socket);
            } catch (IOException e) {
                connectEvent.onEvent(null);
            }
        }
    }

    private static class BluetoothThread extends Thread {

        private final BufferedReader reader;
        private final BufferedWriter writer;
        private final List<OnEventListener<String>> messageReceiveListener;

        public BluetoothThread(BufferedReader reader, BufferedWriter writer,
                               List<OnEventListener<String>> messageReceiveListener) {
            this.writer = writer;
            this.reader = reader;
            this.messageReceiveListener = messageReceiveListener;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    if (reader.ready()) {
                        notifySubscribersAboutMessage(reader.readLine());
                    } else {
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        private void notifySubscribersAboutMessage(String message) {
            for (OnEventListener<String> subscriber : messageReceiveListener) {
                subscriber.onEvent(message);
            }
        }

        public void writeString(String string) {
            try {
                writer.write(string);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void interrupt() {
            super.interrupt();
            try {
                reader.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
