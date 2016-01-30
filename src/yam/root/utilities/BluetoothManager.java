package yam.root.utilities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import static yam.root.ApplicationContext.showShortMessage;

public class BluetoothManager {
    private Context context;
    private BluetoothAdapter bluetoothAdapter;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleDevices(intent);
        }
    };

    public BluetoothManager(Context context) {
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.context = context;
        context.registerReceiver(broadcastReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }

    private void handleDevices(Intent intent) {
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        showShortMessage(device.getAddress());
    }

    public void enableBluetoothAndConnect() {
        Log.d("Enabling bluetooth", "Enabling Bluetooth");
        if (bluetoothEnabled()) {
            Log.d("Connectivity", "Connected to bluetooth");
            connectDevice();
            Log.d("Connectivity", "Connected to device");
        }
    }

    private boolean bluetoothEnabled() {
        if (bluetoothAdapter == null) {
            showShortMessage("Bluetooth not supported");
            return false;
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivity(enableBtIntent);
        }
        return true;
    }

    private void connectDevice() {
        bluetoothAdapter.startDiscovery();
    }

    public void onDestroy() {
        context.unregisterReceiver(broadcastReceiver);
    }
}
