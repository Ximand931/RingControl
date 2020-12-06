package com.happs.ximand.ringcontrol.model.writer;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

@SuppressWarnings("WeakerAccess")
public class QueueWriter {

    private static QueueWriter instance;

    private static final String TAG = "QueueWriter";

    private final WriterThread writerThread;

    private transient final Queue<String> stringQueue;

    private QueueWriter(OutputStream out) {
        this.stringQueue = new ArrayDeque<>();
        this.writerThread = new WriterThread(out, stringQueue);
        this.writerThread.start();
    }

    public static QueueWriter getInstance() {
        if (instance == null) {
            throw new NullPointerException("Instance of queue writer " +
                    "was not initialized");
        }
        return instance;
    }

    public synchronized static void initialized(OutputStream out) {
        if (instance == null) {
            instance = new QueueWriter(out);
        }
    }

    public void write(String str) {
        write(Collections.singletonList(str));
    }

    public void write(List<String> strings) {
        stringQueue.addAll(strings);

        if (writerThread.getState() == Thread.State.WAITING) {
            synchronized (writerThread) {
                writerThread.notify();
            }
        }
    }

    public long calculateTimeInMillis() {
        return stringQueue.size() * WriterThread.DEFAULT_DELAY;
    }

    private static class WriterThread extends Thread {

        private static final int DEFAULT_DELAY = 300;

        private final Queue<String> stringQueue;
        private final OutputStream outputStream;

        WriterThread(OutputStream outputStream, Queue<String> stringQueue) {
            this.outputStream = outputStream;
            this.stringQueue = stringQueue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    waitIfQueueIsEmpty();
                    writeString(stringQueue.poll());
                    Thread.sleep(DEFAULT_DELAY);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }

        private void waitIfQueueIsEmpty() throws InterruptedException {
            if (stringQueue.isEmpty()) {
                synchronized (this) {
                    this.wait();
                }
            }
        }

        private void writeString(String str) {
            try {
                if (str != null) {
                    outputStream.write(str.getBytes());
                }
                outputStream.flush();
            } catch (IOException e) {
                Log.e(TAG, "Exception while writing line: " + str, e);
            }
        }
    }
}
