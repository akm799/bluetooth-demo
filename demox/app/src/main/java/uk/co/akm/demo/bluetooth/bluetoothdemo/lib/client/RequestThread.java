package uk.co.akm.demo.bluetooth.bluetoothdemo.lib.client;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.RemoteResponse;
import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.ResponseCallback;
import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.util.BluetoothConfig;
import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.util.SocketUtils;

/**
 * Created by Thanos Mavroidis on 18/05/2017.
 */
final class RequestThread extends Thread {
    private static final String TAG = RequestThread.class.getSimpleName();

    private final BluetoothAdapter adapter;
    private final BluetoothDevice server;

    private BluetoothSocket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    private String request;
    private ResponseCallback responseCallback;

    private final byte[] buffer = new byte[BluetoothConfig.BUFFER_SIZE];

    public RequestThread(BluetoothAdapter adapter, BluetoothDevice server) {
        this.adapter = adapter;
        this.server = server;
    }

    void sendRequest(String request, ResponseCallback responseCallback) {
        this.request = request;
        this.responseCallback = responseCallback;
        socket = buildSocket();

        start();
    }

    private BluetoothSocket buildSocket() {
        try {
            return server.createRfcommSocketToServiceRecord(BluetoothConfig.SERVER_UUID);
        } catch (IOException ioe) {
            return null;
        }
    }

    @Override
    public void run() {
        try {
            adapter.cancelDiscovery();
            sendRequestAndReadResponse();
        } finally {
            close();
        }
    }

    private void sendRequestAndReadResponse() {
        if (connect()) {
            try {
                Log.d(TAG, "Sending request " + request + " ...");
                SocketUtils.writeString(request, outputStream);
            } catch (IOException ioe) {
                logErrorAndRespond("I/O error when sending the Bluetooth request", ioe);
                return;
            }

            waitForResponse(BluetoothConfig.RESPONSE_READ_DELAY_MILLIS);

            try {
                final String response = SocketUtils.readString(inputStream, buffer);
                Log.d(TAG, "Server response: " + response);
                final RemoteResponse remoteResponse = parseResponse(response);
                if (responseCallback != null) {
                    responseCallback.responseArrived(remoteResponse);
                }
            } catch (IOException ioe) {
                logErrorAndRespond("I/O error when reading the Bluetooth response", ioe);
            }
        } else {
            errorResponseCallback("Bluetooth connection error.");
        }
    }

    private boolean connect() {
        if (socket == null) {
            Log.e(TAG, "Could not connect to the Bluetooth server because of socket initialization failure.");
            return false;
        }

        try {
            socket.connect();
            if (socket.isConnected()) {
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                return true;
            } else {
                Log.d(TAG, "Unable to connect to the Bluetooth server.");
                return false;
            }
        } catch (IOException ioe) {
            Log.e(TAG, "I/O error when trying to connect to the Bluetooth server.", ioe);
            return false;
        }
    }

    private void waitForResponse(long millis) {
        try {
            sleep(millis);
        } catch (InterruptedException e) {}
    }

    private RemoteResponse parseResponse(String response) {
        if (response == null) {
            return new RemoteResponse(false, "Null response error.");
        } else if (BluetoothConfig.NULL_RESPONSE.equals(response)) {
            return new RemoteResponse(true, null);
        } else if (response.contains(BluetoothConfig.ERROR_RESPONSE_START)) {
            return parseErrorResponse(response);
        } else {
            return new RemoteResponse(true, response);
        }
    }

    private RemoteResponse parseErrorResponse(String response) {
        final int len = BluetoothConfig.ERROR_RESPONSE_START.length();
        if (response.length() > len) {
            return new RemoteResponse(false, response.substring(len));
        } else {
            return new RemoteResponse(false, null);
        }
    }

    private void logErrorAndRespond(String errorMessage, Exception e) {
        Log.e(TAG, errorMessage, e);
        errorResponseCallback(errorMessage, e);
    }

    private void errorResponseCallback(String errorMessage) {
        errorResponseCallback(errorMessage, null);
    }

    private void errorResponseCallback(String errorMessage, Exception e) {
        if (responseCallback != null) {
            final String exceptionMessage = (e == null ? "" : (e.getClass() + ": " + e.getMessage()));
            final String fullErrorMessage = (errorMessage + ". " + exceptionMessage);
            final RemoteResponse errorResponse = new RemoteResponse(false, fullErrorMessage);
            responseCallback.responseArrived(errorResponse);
        }
    }

    private void close() {
        request = null;
        responseCallback = null;

        SocketUtils.closeSilently(inputStream);
        SocketUtils.closeSilently(outputStream);
        SocketUtils.closeSilently(socket);

        inputStream = null;
        outputStream = null;
        socket = null;
    }
}
