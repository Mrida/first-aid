package yam.root.utilities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import static yam.root.ApplicationContext.showShortMessage;

public class BluetoothManager {
    public static final UUID CONNECTION_UUID = UUID.fromString("45743cf6-c787-11e5-9956-625662870761");
    public static final String APPLICATION_NAME = "Rescue";

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
        new ConnectionThread(device).start();
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

    private class ConnectionThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;
        private BluetoothDevice otherDevice;

        public ConnectionThread(BluetoothDevice device) {
            otherDevice = device;
            BluetoothServerSocket tmp = null;
            try {
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(APPLICATION_NAME, CONNECTION_UUID);
            } catch (IOException ignored) {
            }
            mmServerSocket = tmp;
        }

        public void run() {

            try {
                connectAsClient();
            } catch (IOException ex) {
                BluetoothSocket socket;
                while (true) {
                    try {
                        socket = mmServerSocket.accept();
                    } catch (IOException e) {
                        break;
                    }
                    if (socket != null) {
                        try {
                            mmServerSocket.close();
                        } catch (IOException ignored) {
                        }
                        manageClientSocket(socket);
                        break;
                    }
                }
            }

        }

        private void connectAsClient() throws IOException {
            BluetoothSocket otherServerSocket = otherDevice.createRfcommSocketToServiceRecord(CONNECTION_UUID);
            try {
                otherServerSocket.connect();
            } catch (IOException connectException) {
                otherServerSocket.close();
                throw new IOException(connectException);
            }

            manageOtherServerSocket(otherServerSocket);
        }


        private void manageClientSocket(BluetoothSocket client) {
            showShortMessage("I am the server and here I contact my client");
        }

        private void manageOtherServerSocket(BluetoothSocket server) {

            showShortMessage("I am the client and here I contact my server");
        }
    }
}


