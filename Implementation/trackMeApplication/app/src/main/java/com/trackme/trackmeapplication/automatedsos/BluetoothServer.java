package com.trackme.trackmeapplication.automatedsos;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class BluetoothServer extends Thread {

    private static final String RF_COMM_NAME = "BluetoothCommunication";
    private static final UUID SERVER_UUID = UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");

    private final BluetoothServerSocket mServerSocket;
    private final Handler mHandler;

    /**
     * Constructor.
     * Create a new bluetooth server by listening using RFcomm based on a UUID
     *
     * @param bluetoothAdapter the adapter of the bluetooth
     * @param handler the handler of messages when a message is received
     */
    public BluetoothServer(BluetoothAdapter bluetoothAdapter, Handler handler) {
        BluetoothServerSocket tmp = null;
        try {
            tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(RF_COMM_NAME, SERVER_UUID);
        } catch (IOException e) {
            Log.e(TAG, "Socket's listen() method failed", e);
        }
        mServerSocket = tmp;
        mHandler = handler;
    }

    /**
     * Thread.
     * Keep listening until exception occurs or a socket is returned. When A connection is accepted,
     * performs work associated with the connection in a separate thread.
     */
    @Override
    public void run() {
        BluetoothSocket socket;
        while (true) {
            try {
                socket = mServerSocket.accept();
            } catch (IOException e) {
                Log.e(TAG, "Socket's accept() method failed", e);
                break;
            }

            if (socket != null) {
                SocketHandler socketHandler = new SocketHandler(socket, mHandler);
                socketHandler.start();
                this.cancel();
                break;
            }
        }
    }

    /**
     * Closes the connect socket and causes the thread to finish.
     */
    public void cancel() {
        try {
            mServerSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }
}
