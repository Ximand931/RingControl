package com.happs.ximand.ringcontrol.model.dao;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.happs.ximand.ringcontrol.OnEventListener;
import com.happs.ximand.ringcontrol.model.object.exception.BluetoothException;
import com.happs.ximand.ringcontrol.model.object.exception.FailedToConnectException;
import com.happs.ximand.ringcontrol.model.object.exception.WhileConnectingException;
import com.happs.ximand.ringcontrol.model.object.info.ConnectedEvent;
import com.happs.ximand.ringcontrol.model.object.info.ConnectingEvent;
import com.happs.ximand.ringcontrol.model.object.info.InfoEvent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class BluetoothDao {

    private static BluetoothDao instance;

    private final BluetoothAdapter bluetoothAdapter;

    private final Set<BluetoothEventListener<String>> messageReceiveListeners =
            new HashSet<>();
    private final Set<BluetoothEventListener<InfoEvent>> infoEventListeners =
            new HashSet<>();
    private final Set<BluetoothEventListener<BluetoothException>> exceptionEventListeners =
            new HashSet<>();

    private BluetoothSocket bluetoothSocket;
    private BluetoothThread bluetoothThread;

    private BluetoothDao() {
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public static BluetoothDao getInstance() {
        if (instance == null) {
            instance = new BluetoothDao();
        }
        return instance;
    }

    public void subscribeToBluetoothMessages(BluetoothEventListener<String> messageReceiveListener) {
        messageReceiveListeners.add(messageReceiveListener);
    }

    public void unsubscribeFromBluetoothMessages(BluetoothEventListener<String>
                                                         messageReceiveListener) {
        messageReceiveListeners.remove(messageReceiveListener);
    }

    public void subscribeToInfoEvents(BluetoothEventListener<InfoEvent> infoEventListener) {
        infoEventListeners.add(infoEventListener);
    }

    public void unsubscribeFromInfoEvents(BluetoothEventListener<InfoEvent> infoEventListener) {
        infoEventListeners.remove(infoEventListener);
    }

    private void notifySubscribersAboutEvent(InfoEvent infoEvent) {
        for (BluetoothEventListener<InfoEvent> subscriber : infoEventListeners) {
            subscriber.onEvent(infoEvent);
        }
    }

    public void subscribeToExceptionEvents(BluetoothEventListener<BluetoothException>
                                                   exceptionEventListener) {
        exceptionEventListeners.add(exceptionEventListener);
    }

    public void unsubscribeFromExceptionEvents(BluetoothEventListener<BluetoothException>
                                                       exceptionEventListener) {
        exceptionEventListeners.remove(exceptionEventListener);
    }

    private void notifySubscribersAboutException(BluetoothException exception) {
        for (BluetoothEventListener<BluetoothException> subscriber : exceptionEventListeners) {
            subscriber.onEvent(exception);
        }
    }

    public boolean isBluetoothEnable() {
        return bluetoothAdapter.isEnabled();
    }

    public boolean enableBluetoothIfDisabled() {
        return bluetoothAdapter.enable();
    }

    public void startConnectToDeviceTask(BluetoothDevice target) {
        notifySubscribersAboutEvent(new ConnectingEvent());
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

    private void afterConnect(BluetoothSocket socket) {
        if (socket == null || !socket.isConnected()) {
            notifySubscribersAboutException(new FailedToConnectException());
            return;
        }
        this.bluetoothSocket = socket;
        this.bluetoothThread = createBluetoothThreadBySocket();
        if (bluetoothThread != null) {
            bluetoothThread.start();
            notifySubscribersAboutEvent(new ConnectedEvent());
        } else {
            notifySubscribersAboutException(new WhileConnectingException());
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
            notifySubscribersAboutException(new WhileConnectingException());
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
        private final Set<BluetoothEventListener<String>> messageReceiveListener;

        public BluetoothThread(BufferedReader reader, BufferedWriter writer,
                               Set<BluetoothEventListener<String>> messageReceiveListener) {
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
            for (BluetoothEventListener<String> subscriber : messageReceiveListener) {
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
