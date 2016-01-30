package yam.root.application;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import yam.root.rescue.R;
import yam.root.utilities.BluetoothManager;

public class Rescue extends Activity {

    private BluetoothManager bluetoothManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        bluetoothManager = new BluetoothManager(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bluetoothManager.enableBluetoothAndConnect();
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Stopped", "Application has stopped");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bluetoothManager.onDestroy();
    }
}
