package uk.co.akm.demo.bluetooth.bluetoothdemo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.RemoteServer;
import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.RequestProcessor;
import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.server.BluetoothServerFactory;
import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.util.BluetoothConfig;

public class ServerActivity extends AppCompatActivity implements RequestProcessor {
    private TextView requestTxt;
    private TextView responseTxt;
    private TextView requestCounterTxt;

    private int counter;
    private RemoteServer remoteServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        resolveViewReferences();
    }

    private void resolveViewReferences() {
        requestTxt = (TextView) findViewById(R.id.server_request);
        responseTxt = (TextView) findViewById(R.id.server_response);
        requestCounterTxt = (TextView) findViewById(R.id.server_request_counter);
    }

    @Override
    public void onResume() {
        super.onResume();

        remoteServer = BluetoothServerFactory.instance();
        remoteServer.init(this);

        setText(R.id.server_server_name, ("Name: " + BluetoothConfig.SERVER_NAME));
        setText(R.id.server_server_uuid, ("UUID: " + BluetoothConfig.SERVER_UUID.toString()));
    }

    public void onPause() {
        super.onPause();

        if (remoteServer != null) {
            remoteServer.close();
            remoteServer = null;
        }
    }

    private void setText(int resId, String text) {
        ((TextView) findViewById(resId)).setText(text);
    }

    @Override
    public String process(String request) { // This method will be called on a separate (non-UI) thread.
        final String response = Integer.toHexString(request.hashCode());
        updateUi(++counter, request, response);

        return response;
    }

    private void updateUi(final int counter, final String request, final String response) {
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                requestTxt.setText(request);
                responseTxt.setText(response);
                requestCounterTxt.setText(Integer.toString(counter));
            }
        };

        runOnUiThread(update);
    }
}
