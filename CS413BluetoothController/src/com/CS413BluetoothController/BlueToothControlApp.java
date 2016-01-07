package com.CS413BluetoothController;


import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by GavinDonnelly on 15/11/2015.
 *
 * Built from the developer.google.com sample code and other code on the
 * internet on a using bluetooth so this is just kinda hashed and mashed togther
 * works, there are bits I dont know why but they need to be there lots of comments
 *
 * Adding Comment for test of github intergration
 *
 */

public class BlueToothControlApp extends Application {

    private final static String TAG = "BluetoothController";
    // Debug flag
    public final static boolean D = true;

    // Time between sending the idle filler to confirm communication, must be smaller than the timeout constant.
    private final int minCommInterval = 900;
    // Time after which the communication is deemed dead
    private final int timeout = 5000;
    //was 3000
    private long lastComm;

    // Member fields
    private BluetoothThread bluetoothThread;
    private TimeoutThread timeoutThread;
    private Handler activityHandler;
    private int state;
    private boolean busy, stoppingConnection;

    // Constants to indicate message contents
    public static final int MSG_OK = 0;
    public static final int MSG_READ = 1;
    public static final int MSG_WRITE = 2;
    public static final int MSG_CANCEL = 3;
    public static final int MSG_CONNECTED = 4;

    // General purpose constants to be used inside activities as callback values
    public static final int MSG_1 = 10;
    public static final int MSG_2 = 11;
    public static final int MSG_3 = 12;

    // Constants that indicate the current connection state
    private static final int STATE_NONE = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    /**
     * Constructor. Prepares a new Bluetooth session.
     */
    public BlueToothControlApp()
    {
        state = STATE_NONE;
        activityHandler = null;
    }


    public void setActivityHandler(Handler handler)
    {
        activityHandler = handler;
    }


    private synchronized void sendMessage(int type, Object value)
    {

        if(activityHandler != null)
        {
            activityHandler.obtainMessage(type, value).sendToTarget();
        }
    }


    private synchronized void setState(int newState)
    {
        if(D)
            Log.i(TAG, "Connection status: " + state + " -> " + newState);
        state = newState;
    }


    private synchronized void updateLastComm()
    {
        lastComm = System.currentTimeMillis();
    }


    public synchronized void connect(BluetoothDevice device)
    {

        if(D)
            Log.i(TAG, "Connecting to " + device.getName());
        stoppingConnection = false;
        busy = false;

        // Cancel any thread currently running a connection
        if(bluetoothThread != null)
        {
            bluetoothThread.cancel();
            bluetoothThread = null;
        }

        setState(STATE_CONNECTING);

        // Start the thread to connect with the given device
        bluetoothThread = new BluetoothThread(device);
        bluetoothThread.start();

        // Start the timeout thread to check the connecting status
        timeoutThread = new TimeoutThread();
        timeoutThread.start();
    }

    /**
     * This thread runs during a connection with a remote device. It handles the
     * initial connection and all incoming and outgoing transmissions.
     */
    private class BluetoothThread extends Thread
    {
        private final BluetoothSocket socket;
        private InputStream inStream;
        private OutputStream outStream;

        public BluetoothThread(BluetoothDevice device)
        {
            BluetoothSocket tmp = null;
            try
            {
                // General purpose UUID
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            socket = tmp;
        }

        public void run()
        {
            // Connect to the socket
            try
            {
                // Blocking function, needs the timeout
                if(D)
                    Log.i(TAG, "Connecting to socket");
                socket.connect();
            }
            catch(IOException e)
            {
                // If the user didn't cancel the connection then it has failed (timeout)
                if(!stoppingConnection)
                {
                    if(D)
                        Log.e(TAG, "Cound not connect to socket");
                    e.printStackTrace();
                    try
                    {
                        socket.close();
                    }
                    catch(IOException e1)
                    {
                        if(D)
                            Log.e(TAG, "Cound not close the socket");
                        e1.printStackTrace();
                    }
                    Log.e(TAG,"disconnecting in run");
                    disconnect();
                }
                Log.e(TAG,"blast:" + e);
                return;
            }

            // Connected
            setState(STATE_CONNECTED);
            // Send message to activity to inform of success
            sendMessage(MSG_CONNECTED, null);
            Log.e(TAG,"just connected");
            // Get the BluetoothSocket input and output streams
            try
            {
                inStream = socket.getInputStream();
                outStream = socket.getOutputStream();
            }
            catch(IOException e)
            {
                // Failed to get the streams
                Log.e(TAG,"failed to get stream disconnecting");
                disconnect();
                e.printStackTrace();
                return;
            }

            byte[] buffer = new byte[1024];
            byte ch;
            int bytes;
            String input;
            Log.e(TAG,"listen outer");
            // Keep listening to the InputStream while connected
            while(true)
            {
                try
                {
                    Log.e(TAG,"listening loop");
                    // Make a packet, use \n (new line or NL) as packet end
                    // println() used in Arduino code adds \r\n to the end of the stream
                    bytes = 0;
                    while((ch = (byte) inStream.read()) != '\n')
                    {
                        buffer[bytes++] = ch;
                    }
                    // Prevent read errors (if you mess enough with it)
                    if(bytes > 0)
                    {
                        // The carriage return (\r) character has to be removed
                        input = new String(buffer, "UTF-8").substring(0, bytes - 1);

                        if(D)
                            Log.v(TAG, "Read: " + input);

                        // Empty character is considered as a filler to keep the connection alive, don't forward that to the activity
                        if(!input.equals("0"))
                        {
                            // Send the obtained bytes to the UI Activity if any
                            sendMessage(MSG_READ, input);
                        }
                    }
                    busy = false;
                    // Update last communication time to prevent timeout
                    Log.e(TAG,"updating comm");
                    updateLastComm();

                }
                catch(IOException e)
                {
                    // read() will inevitably throw an error, even when just disconnecting
                    if(!stoppingConnection)
                    {
                        if(D)
                            Log.e(TAG, "Failed to read");
                        e.printStackTrace();
                        Log.e(TAG,"fail to read, disconnecting");
                        disconnect();
                    }
                    Log.e(TAG,"blast2: " + e);
                    break;
                }
            }
        }

        public boolean write(String out)
        {
            if(outStream == null)
            {
                return false;
            }

            if(D)
                Log.v(TAG, "Write: " + out);
            try
            {
                if(out != null)
                {
                    // Show sent message to the active activity
                    sendMessage(MSG_WRITE, out);
                    outStream.write(out.getBytes());
                }
                else
                {
                    // This is a special case for the filler
                    outStream.write(0);
                }
                // End packet with a new line
                outStream.write('\n');
                return true;
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            return false;
        }

        public void cancel()
        {
            try
            {
                if(inStream != null)
                {
                    inStream.close();
                }
                if(outStream != null)
                {
                    outStream.close();
                }
                if(socket != null)
                {
                    socket.close();
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    private class TimeoutThread extends Thread
    {
        public TimeoutThread()
        {
            // Set the time before first check
            if(D)
                Log.i(TAG, "Started timeout thread");
            updateLastComm();
        }

        public void run()
        {
            Log.i(TAG, "State: " + state);
            while(state == STATE_CONNECTING || state == STATE_CONNECTED)
            {
                // I'm not sure that it's needed here, but it works
                synchronized(BlueToothControlApp.this)
                {
                    // Filler hash to confirm communication with device when idle
                    if(System.currentTimeMillis() - lastComm > minCommInterval && !busy && state == STATE_CONNECTED)
                    {
                        write(null);
                    }

                    Log.i(TAG, "State: " + state);
                    Log.i(TAG, "lastComm: " + lastComm);
                    // Communication timed out
                    if(System.currentTimeMillis() - lastComm > timeout)
                    {
                        if(D)
                            Log.e(TAG, "Timeout");
                        Log.e(TAG,"timeout disconnect");
                        disconnect();
                        break;
                    }
                }

                // This thread should not run all the time
                try
                {
                    Thread.sleep(50);
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }


    public boolean write(String out)
    {
        
        if(busy && !out.equals(out))
        {
            if(D)
                Log.v(TAG, "Busy");
            return false;
        }
        busy = true;

        // Create temporary object
        BluetoothThread r;
        // Synchronize a copy of the BluetoothThread
        synchronized(this)
        {
            // Make sure the connection is live
            if(state != STATE_CONNECTED)
            {
                return false;
            }
            r = bluetoothThread;
        }
        // Perform the write unsynchronized
        return r.write(out);
    }

    /**
     * Stop all threads
     */
    public synchronized void disconnect()
    {

        // Do not stop twice
        if(!stoppingConnection)
        {
            stoppingConnection = true;
            if(D)
                Log.i(TAG, "Stop");
            if(bluetoothThread != null)
            {
                bluetoothThread.cancel();
                bluetoothThread = null;
            }
            setState(STATE_NONE);
            sendMessage(MSG_CANCEL, "Connection ended");
        }
    }
}
