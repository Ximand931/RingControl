package com.happs.ximand.ringcontrol.model.dao;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import androidx.annotation.Nullable;

import com.happs.ximand.ringcontrol.OnEventListener;
import com.happs.ximand.ringcontrol.model.object.exception.BluetoothExceptionDep;
import com.happs.ximand.ringcontrol.model.object.exception.FailedToConnectException;
import com.happs.ximand.ringcontrol.model.object.exception.WhileReadingException;
import com.happs.ximand.ringcontrol.model.object.exception.WhileSendingException;
import com.happs.ximand.ringcontrol.model.object.info.BluetoothEvent;
import com.happs.ximand.ringcontrol.model.object.response.Response;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Deprecated
public final class BluetoothDao {

    private static BluetoothDao instance;

    private final Set<BluetoothEventListener<Response>> responseListeners = new HashSet<>();
    private final Set<BluetoothEventListener<BluetoothEvent>> eventListeners = new HashSet<>();
    private final Set<BluetoothEventListener<BluetoothExceptionDep>> exceptionListeners =
            new HashSet<>();

    @Nullable
    private BluetoothSocket bluetoothSocket;
    @Nullable
    private BluetoothThread bluetoothThread;

    private BluetoothDao() {
    }

    public static BluetoothDao getInstance() {
        return null;
    }

    public void subscribeToResponses(BluetoothEventListener<Response> responseListener) {
        responseListeners.add(responseListener);
    }

    public void unsubscribeFromResponses(BluetoothEventListener<Response> responsesListener) {
        responseListeners.remove(responsesListener);
    }

    private void notifySubscribersAboutMessage(byte[] incomingMessage) {
        for (BluetoothEventListener<Response> subscriber : responseListeners) {
            subscriber.onEvent(Response.getResponseByMessage(incomingMessage));
        }
    }

    public void subscribeToEvents(BluetoothEventListener<BluetoothEvent> eventListener) {
        eventListeners.add(eventListener);
    }

    public void unsubscribeFromEvents(BluetoothEventListener<BluetoothEvent> eventListener) {
        eventListeners.remove(eventListener);
    }

    private void notifySubscribersAboutEvent(BluetoothEvent event) {
        for (BluetoothEventListener<BluetoothEvent> subscriber : eventListeners) {
            subscriber.onEvent(event);
        }
    }

    public void subscribeToExceptions(BluetoothEventListener<BluetoothExceptionDep> exceptionListener) {
        exceptionListeners.add(exceptionListener);
    }

    public void unsubscribeFromExceptionEvents(BluetoothEventListener<BluetoothExceptionDep>
                                                       exceptionEventListener) {
        exceptionListeners.remove(exceptionEventListener);
    }

    private void notifySubscribersAboutException(BluetoothExceptionDep exception) {
        for (BluetoothEventListener<BluetoothExceptionDep> subscriber : exceptionListeners) {
            subscriber.onEvent(exception);
        }
    }

    public boolean isBluetoothEnabled() {
        return BluetoothAdapter.getDefaultAdapter().isEnabled();
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
            //notifySubscribersAboutException(new WhileConnectingException(new IllegalStateException("3")));
            notifySubscribersAboutEvent(BluetoothEvent.ERROR_WHILE_CONNECTING);
        }
    }

    private BluetoothThread createBluetoothThreadBySocket() {
        if (bluetoothSocket == null) {
            throw new IllegalStateException();
        }
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

    public void sendMessage(byte[] messageByteArray) {
        if (bluetoothThread != null) {
            bluetoothThread.writeByteArray(messageByteArray);
        } else {
            notifySubscribersAboutException(new WhileSendingException());
        }
    }

    public void clear() {
        if (bluetoothThread != null) {
            bluetoothThread.interrupt();
        }
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
