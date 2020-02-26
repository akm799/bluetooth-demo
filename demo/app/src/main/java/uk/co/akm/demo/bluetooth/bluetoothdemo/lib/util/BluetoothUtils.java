package uk.co.akm.demo.bluetooth.bluetoothdemo.lib.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Created by mavroidt on 18/05/2017.
 */
public class BluetoothUtils {
    private static final String TAG = BluetoothUtils.class.getSimpleName();

    public static BluetoothAdapter getBluetoothAdapter() {
        final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            Log.d(TAG, "Bluetooth not supported on this device.");
            return null;
        }

        if (adapter.isEnabled()) {
            return adapter;
        } else {
            Log.d(TAG, "Bluetooth not enabled. Please enable Bluetooth on the device before proceeding.");
            return null;
        }
    }

    public static BluetoothDevice findPairedDeviceByName(BluetoothAdapter adapter, String name) {
        if (adapter == null) {
            return null;
        }

        if (name == null) {
            Log.d(TAG, "Null Bluetooth devices name supplied. Will not search for any paired devices.");
            return null;
        }

        final Collection<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
        if (pairedDevices.isEmpty()) {
            Log.d(TAG, "No paired Bluetooth devices found.");
            return null;
        }

        for (BluetoothDevice device : pairedDevices) {
            final String pairedDeviceName = device.getName();
            Log.d(TAG, "Checking paired Bluetooth device: " + pairedDeviceName);
            if (name.equalsIgnoreCase(pairedDeviceName)) {
                return device;
            }
        }

        Log.d(TAG, "No paired Bluetooth device found matching: " + name);
        return null;
    }

    public static Collection<CharSequence> getPairedDeviceNames(BluetoothAdapter adapter) {
        if (adapter == null) {
            Log.d(TAG, "Bluetooth not supported on this device. Cannot search for paired devices.");
            return null;
        }

        final Collection<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
        final Collection<CharSequence> names = new LinkedHashSet<>(pairedDevices.size());
        for (BluetoothDevice device : pairedDevices) {
            Log.d(TAG, "Bluetooth paired device: " + device.getName());
            names.add(device.getName());
        }

        Log.d(TAG, "Number of Bluetooth paired devices: " + pairedDevices.size());
        return names;
    }

    private BluetoothUtils() {}
}
