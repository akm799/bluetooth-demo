package uk.co.akm.demo.bluetooth.bluetoothdemo.lib.util;

import java.util.UUID;

/**
 * Created by Thanos Mavroidis on 18/05/2017.
 */
public class BluetoothConfig {
    public static final String SERVER_NAME = "constant.demo.bluetooh.server.name";
    public static final UUID SERVER_UUID = UUID.fromString("ca7d1817-efaf-444e-8f46-4261d5a30b3f");

    public static final String NULL_RESPONSE = "__bluetooth.demo.null.response.code.123418052017__";
    public static final String ERROR_RESPONSE_START = "__bluetooth.demo.error.response.start.145118052017__>";

    public static final int BUFFER_SIZE = 4*1024;
    public static final long RESPONSE_READ_DELAY_MILLIS = 250;

    private BluetoothConfig() {}
}
