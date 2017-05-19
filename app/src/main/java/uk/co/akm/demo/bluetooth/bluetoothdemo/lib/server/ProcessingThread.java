package uk.co.akm.demo.bluetooth.bluetoothdemo.lib.server;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;

import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.RequestProcessor;
import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.util.BluetoothConfig;
import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.util.SocketUtils;

/**
 * Created by Thanos Mavroidis on 18/05/2017.
 */
final class ProcessingThread extends Thread {
    private static final String TAG = ProcessingThread.class.getSimpleName();

    private final BluetoothSocket socket;
    private final RequestProcessor requestProcessor;

    private final byte[] buffer = new byte[BluetoothConfig.BUFFER_SIZE];

    ProcessingThread(BluetoothSocket socket, RequestProcessor requestProcessor) {
        this.socket = socket;
        this.requestProcessor = requestProcessor;
    }

    @Override
    public void run() {
        String request, response;

        try {
            request = SocketUtils.readString(socket.getInputStream(), buffer);
            response = getResponse(request);
        } catch (IOException ioe) {
            Log.e(TAG, "I/O error while reading request.", ioe);
            response = (BluetoothConfig.ERROR_RESPONSE_START + ioe.getClass() + ": " + ioe.getMessage());
        }

        sendResponse(response, socket);
    }

    private String getResponse(String request) {
        try {
            final String response = requestProcessor.process(request);

            return (response == null ? BluetoothConfig.NULL_RESPONSE : response);
        } catch (Exception e) {
            return (e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private void sendResponse(String response, BluetoothSocket socket) {
        try {
            SocketUtils.writeString(response, socket.getOutputStream());
        } catch (IOException ioe) {
            Log.e(TAG, "I/O error while sending response: " + response, ioe);
        }
    }
}
