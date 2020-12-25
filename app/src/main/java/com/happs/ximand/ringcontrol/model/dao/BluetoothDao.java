package com.happs.ximand.ringcontrol.model.dao;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.happs.ximand.ringcontrol.OnEventListener;
import com.happs.ximand.ringcontrol.model.object.exception.bl.BluetoothException;
import com.happs.ximand.ringcontrol.model.object.exception.bl.FailedToConnectException;
import com.happs.ximand.ringcontrol.model.object.exception.bl.WhileConnectingException;
import com.happs.ximand.ringcontrol.model.object.exception.bl.WhileReadingException;
import com.happs.ximand.ringcontrol.model.object.exception.bl.WhileSendingException;
import com.happs.ximand.ringcontrol.model.object.info.BluetoothEvent;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class BluetoothDao {

    private static BluetoothDao instance;

    private final BluetoothAdapter bluetoothAdapter;

    private final Set<BluetoothEventListener<Byte[]>> messageReceiveListeners =
            new HashSet<>();
    private final Set<BluetoothEventListener<BluetoothEvent>> infoEventListeners =
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

    public void subscribeToBluetoothMessages(BluetoothEventListener<Byte[]> messageReceiveListener) {
        messageReceiveListeners.add(messageReceiveListener);
    }

    public void unsubscribeFromBluetoothMessages(BluetoothEventListener<Byte[]>
                                                         messageReceiveListener) {
        messageReceiveListeners.remove(messageReceiveListener);
    }

    private void notifySubscribersAboutMessage(byte[] messageByteArray) {
        for (BluetoothEventListener<Byte[]> subscriber : messageReceiveListeners) {
            subscriber.onEvent(toObjects(messageByteArray));
        }
    }

    public void subscribeToInfoEvents(BluetoothEventListener<BluetoothEvent> infoEventListener) {
        infoEventListeners.add(infoEventListener);
    }

    public void unsubscribeFromInfoEvents(BluetoothEventListener<BluetoothEvent> infoEventListener) {
        infoEventListeners.remove(infoEventListener);
    }

    private void notifySubscribersAboutEvent(BluetoothEvent infoEvent) {
        for (BluetoothEventListener<BluetoothEvent> subscriber : infoEventListeners) {
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

    public void startConnectToDeviceTask(BluetoothDevice target) {
        notifySubscribersAboutEvent(BluetoothEvent.CONNECTING);
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
            notifySubscribersAboutEvent(BluetoothEvent.ERROR_WHILE_CONNECTING);
            return;
        }
        this.bluetoothSocket = socket;
        this.bluetoothThread = createBluetoothThreadBySocket();
        if (bluetoothThread != null) {
            bluetoothThread.start();
            notifySubscribersAboutEvent(BluetoothEvent.READY);
        } else {
            notifySubscribersAboutException(new WhileConnectingException());
            notifySubscribersAboutEvent(BluetoothEvent.ERROR_WHILE_CONNECTING);
        }
    }

    private BluetoothThread createBluetoothThreadBySocket() {
        try {
            bluetoothThread = new BluetoothThread(
                    new BufferedInputStream((bluetoothSocket.getInputStream())),
                    new BufferedOutputStream(bluetoothSocket.getOutputStream())
            );
            return bluetoothThread;
        } catch (IOException e) {
            notifySubscribersAboutEvent(BluetoothEvent.ERROR_WHILE_CONNECTING);
        }
        return null;
    }

    private void startAuthentification() {

    }

    @Deprecated
    public void sendMessage(String message) {
        throw new UnsupportedOperationException();
    }

    public void sendMessage(byte[] messageByteArray) {
        bluetoothThread.writeByteArray(messageByteArray);
    }

    public void clear() {
        bluetoothThread.interrupt();
    }

    private Byte[] toObjects(byte[] bytesPrim) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return toObjectsForApiHigher24(bytesPrim);
        } else {
            return toObjectsForApiLower24(bytesPrim);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Byte[] toObjectsForApiHigher24(byte[] bytesPrim) {
        Byte[] bytes = new Byte[bytesPrim.length];
        Arrays.setAll(bytes, n -> bytesPrim[n]);
        return bytes;
    }

    private Byte[] toObjectsForApiLower24(byte[] bytesPrim) {
        Byte[] bytes = new Byte[bytesPrim.length];
        for (int i = 0; i < bytesPrim.length; i++) {
            bytes[i] = bytesPrim[i];
        }
        return bytes;
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

    private class BluetoothThread extends Thread {

        private final BufferedInputStream inputStream;
        private final BufferedOutputStream outputStream;

        public BluetoothThread(BufferedInputStream inputStream,
                               BufferedOutputStream outputStream) {
            this.inputStream = inputStream;
            this.outputStream = outputStream;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    if (inputStream.available() > 0) {
                        byte[] incomingMessage = new byte[inputStream.available()];
                        //noinspection ResultOfMethodCallIgnored
                        inputStream.read(incomingMessage);
                        notifySubscribersAboutMessage(incomingMessage);
                    } else {
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    break;
                } catch (IOException e) {
                    notifySubscribersAboutException(new WhileReadingException());
                    break;
                }
            }
        }

        public void writeByteArray(byte[] message) {
            try {
                notifySubscribersAboutEvent(BluetoothEvent.SENDING_DATA);
                outputStream.write(message);
                notifySubscribersAboutEvent(BluetoothEvent.READY);
            } catch (IOException e) {
                notifySubscribersAboutEvent(BluetoothEvent.ERROR_WHILE_SENDING);
                notifySubscribersAboutException(new WhileSendingException());
            }
        }

        @Override
        public void interrupt() {
            super.interrupt();
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
