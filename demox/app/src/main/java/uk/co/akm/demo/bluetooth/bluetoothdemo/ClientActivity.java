package uk.co.akm.demo.bluetooth.bluetoothdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collection;

import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.RemoteClient;
import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.RemoteResponse;
import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.ResponseCallback;
import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.client.BluetoothClientFactory;
import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.util.BluetoothConfig;

public class ClientActivity extends AppCompatActivity implements ResponseCallback, AdapterView.OnItemSelectedListener {
    private Spinner serverList;
    private EditText requestTxt;
    private TextView responseTxt;
    private TextView responseErrorTxt;

    private RemoteClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        resolveViewReferences();
        addListeners();
    }

    private void resolveViewReferences() {
        serverList = (Spinner) findViewById(R.id.client_server_list);
        requestTxt = (EditText) findViewById(R.id.client_request);
        responseTxt = (TextView) findViewById(R.id.client_response);
        responseErrorTxt = (TextView) findViewById(R.id.client_response_error);
    }

    private void addListeners() {
        serverList.setOnItemSelectedListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        client = BluetoothClientFactory.instance();
        showPairedDevices();
    }

    private void showPairedDevices() {
        final Collection<CharSequence> deviceNames = client.listServers();
        if (deviceNames != null && !deviceNames.isEmpty()) {
            final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(deviceNames));
            serverList.setAdapter(adapter);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int index, long id) {
        final Object server = adapterView.getItemAtPosition(index);
        if (server != null) {
            connect(server.toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    private void connect(String serverDeviceName) {
        if (client.connect(serverDeviceName)) {
            setText(R.id.client_server_uuid, ("Server UUID: " + BluetoothConfig.SERVER_UUID.toString()));
        } else {
            client = null;
            Toast.makeText(this, "Could not connect to Bluetooth device: " + serverDeviceName, Toast.LENGTH_SHORT).show();
        }
    }

    private void setText(int resId, String text) {
        ((TextView) findViewById(resId)).setText(text);
    }

    public void onSend(View view) {
        final String request = readUserInput();
        if (client != null && request != null) {
            client.send(request, this);
        }
    }

    private String readUserInput() {
        final CharSequence request = requestTxt.getText();
        if (request != null && request.length() > 0) {
            return request.toString();
        } else {
            return null;
        }
    }

    @Override
    public void responseArrived(final RemoteResponse response) { // This method is going to be called on a separate (non-UI) thread.
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if (response.success()) {
                    responseTxt.setText(response.getContent());
                    responseErrorTxt.setText("");
                } else {
                    responseTxt.setText("");
                    responseErrorTxt.setText(response.getErrorMessage());
                }
            }
        };

        runOnUiThread(update);
    }
}
