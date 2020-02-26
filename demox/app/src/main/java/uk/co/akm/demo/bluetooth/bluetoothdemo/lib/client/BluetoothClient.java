package uk.co.akm.demo.bluetooth.bluetoothdemo.lib.client;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.Collection;

import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.RemoteClient;
import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.ResponseCallback;
import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.util.BluetoothUtils;

/**
 * Created by Thanos Mavroidis on 18/05/2017.
 */

final class BluetoothClient implements RemoteClient {
    private BluetoothDevice server;
    private BluetoothAdapter adapter;

    BluetoothClient() {}

    @Override
    public Collection<CharSequence> listServers() {
        if (init()) {
            return BluetoothUtils.getPairedDeviceNames(adapter);
        } else {
            return null;
        }
    }

    @Override
    public boolean connect(String bluetoothServerDeviceName) {
        if (init()) {
            server = BluetoothUtils.findPairedDeviceByName(adapter, bluetoothServerDeviceName);
            return (server != null);
        } else {
            return false;
        }
    }

    private boolean init() {
        if (adapter == null) {
            adapter = BluetoothUtils.getBluetoothAdapter();
        }

        return (adapter != null);
    }

    @Override
    public void send(String request, ResponseCallback responseCallback) {
        if (adapter != null && server != null) {
            final RequestThread requestThread = new RequestThread(adapter, server);
            requestThread.sendRequest(request, responseCallback);
        }
    }
}
