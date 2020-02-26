package uk.co.akm.demo.bluetooth.bluetoothdemo.lib.server;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;

import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.RequestProcessor;
import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.util.BluetoothConfig;
import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.util.SocketUtils;

/**
 * Created by Thanos Mavroidis on 18/05/2017.
 */
final class ListeningThread extends Thread {
    private static final String TAG = ListeningThread.class.getSimpleName();

    private final RequestProcessor requestProcessor;

    private BluetoothServerSocket serverSocket;

    ListeningThread(RequestProcessor requestProcessor) {
        this.requestProcessor = requestProcessor;
    }

    void startListening(BluetoothServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        start();
    }

    @Override
    public void run() {
        while (serverSocket != null) {
            try {
                Log.d(TAG, "Bluetooth server - Name: " + BluetoothConfig.SERVER_NAME + "  UUID: " + BluetoothConfig.SERVER_UUID);
                Log.d(TAG, "Listening for incoming requests ...");
                final BluetoothSocket socket = serverSocket.accept();
                processRequest(socket);
            } catch (IOException ioe) {
                Log.e(TAG, "Error while listening for incoming requests.", ioe);
                stopListening();
            }
        }
        Log.d(TAG, "Stopped listening for incoming requests.");
    }

    private void processRequest(BluetoothSocket socket) {
        Log.d(TAG, "Processing received request ...");
        final Thread processingThread = new ProcessingThread(socket, requestProcessor);
        processingThread.start();
    }

    void stopListening() {
        final BluetoothServerSocket temp = serverSocket;
        serverSocket = null;

        SocketUtils.closeSilently(temp);
    }
}
