package uk.co.akm.demo.bluetooth.bluetoothdemo.lib.client;

import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.RemoteClient;

/**
 * Created by Thanos Mavroidis on 18/05/2017.
 */
public class BluetoothClientFactory {

    public static RemoteClient instance() {
        return new BluetoothClient();
    }

    private BluetoothClientFactory() {}
}
