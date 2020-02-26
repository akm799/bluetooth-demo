package uk.co.akm.demo.bluetooth.bluetoothdemo.lib.server;

import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.RemoteServer;

/**
 * Created by Thanos Mavroidis on 18/05/2017.
 */
public class BluetoothServerFactory {

    public static RemoteServer instance() {
        return new BluetoothServer();
    }

    private BluetoothServerFactory() {}
}
