package com.trackme.trackmeapplication.automatedsos;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trackme.trackmeapplication.automatedsos.model.HealthData;
import com.trackme.trackmeapplication.automatedsos.model.MessageType;

import java.io.IOException;
import java.io.InputStream;

public class SocketHandler extends Thread {

    private static final String TAG = "SocketHandler";
    private static final Integer NUMBER_OF_BYTES_FOR_INTEGER = 4;

    private final BluetoothSocket mBluetoothSocket;
    private final InputStream mInputStream;
    private final Handler mHandler;

    /**
     * Constructor.
     * Create a socket handler which handle the connection socket with the client (e.g. Smartwatch)
     *
     * @param socket the socket bluetooth of the connection
     * @param handler the handler of messages which handle the message received
     */
    public SocketHandler(BluetoothSocket socket, Handler handler) {
        mBluetoothSocket = socket;
        InputStream tmpIn = null;

        // Get the input and output streams; using temp objects because
        // member streams are final.
        try {
            tmpIn = socket.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating input stream", e);
        }

        mInputStream = tmpIn;
        mHandler = handler;
    }

    /**
     * Keeps reading from the input stream: first the number of bytes of the string to be read and
     * then it reads the string which should be a json to be mapped in a HealthData object.
     * Consequently it will send the message to the handler.
     */
    @Override
    public void run() {
        byte[] mBuffer = new byte[1024];

        while (true) {
            try {
                mInputStream.read(mBuffer, 0, NUMBER_OF_BYTES_FOR_INTEGER);
                int sizeOfNextString = Integer.valueOf(new String(mBuffer));
                mInputStream.read(mBuffer, 0, sizeOfNextString);

                ObjectMapper objectMapper = new ObjectMapper();
                HealthData healthData = objectMapper.readValue(mBuffer, HealthData.class);

                Message readMsg = mHandler.obtainMessage(
                        MessageType.HEALTH_DATA, healthData);
                readMsg.sendToTarget();
            } catch (IOException e) {
                Log.d(TAG, "Input stream was disconnected", e);
                break;
            }finally {
                this.cancel();
            }
        }
    }

    /**
     * Closes the connect socket and input stream
     */
    public void cancel() {
        try {
            mInputStream.close();
            mBluetoothSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }

}
