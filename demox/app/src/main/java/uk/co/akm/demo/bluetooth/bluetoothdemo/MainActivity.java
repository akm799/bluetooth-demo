package uk.co.akm.demo.bluetooth.bluetoothdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClient(View view) {
        startActivity(new Intent(this, ClientActivity.class));
    }

    public void onServer(View view) {
        startActivity(new Intent(this, ServerActivity.class));
    }
}
