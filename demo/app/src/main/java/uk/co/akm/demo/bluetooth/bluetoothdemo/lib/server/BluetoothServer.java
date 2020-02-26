package uk.co.akm.demo.bluetooth.bluetoothdemo.lib.server;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.util.Log;

import java.io.IOException;

import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.RequestProcessor;
import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.RemoteServer;
import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.util.BluetoothConfig;
import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.util.BluetoothUtils;

/**
 * Created by Thanos Mavroidis on 18/05/2017.
 */
final class BluetoothServer implements RemoteServer {
    private static final String TAG = BluetoothServer.class.getSimpleName();

    private ListeningThread listeningThread;

    BluetoothServer() {}

    @Override
    public boolean init(RequestProcessor requestProcessor) {
        final BluetoothServerSocket serverSocket = buildServerSocket();
        if (serverSocket == null) {
            return false;
        }

        listeningThread = new ListeningThread(requestProcessor);
        listeningThread.startListening(serverSocket);

        return true;
    }

    private BluetoothServerSocket buildServerSocket() {
        final BluetoothAdapter adapter = BluetoothUtils.getBluetoothAdapter();
        if (adapter == null) {
            return null;
        }

        try {
            return adapter.listenUsingRfcommWithServiceRecord(BluetoothConfig.SERVER_NAME, BluetoothConfig.SERVER_UUID);
        } catch (IOException ioe) {
            Log.e(TAG, "I/O error when building the bluetooth server socket.", ioe);
            return null;
        }
    }

    @Override
    public void close() {
        if (listeningThread != null) {
            listeningThread.stopListening();
            listeningThread = null;
        }
    }
}
