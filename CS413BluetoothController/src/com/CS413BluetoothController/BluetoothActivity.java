package com.CS413BluetoothController;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;

/**
 * Created by GavinDonnelly on 15/11/2015.
 *
 * Deal with the basic bluetooth interactions with some error checking etc.
 */
public class BluetoothActivity extends Activity implements Handler.Callback {

    private static BlueToothControlApp appState;
    // When launching a new activity and this one stops it doesn't mean something bad (no connection loss)
    protected boolean preventCancel;
    private static String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Launched when the activity is created
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        appState = (BlueToothControlApp) getApplicationContext();
    }

    protected boolean write(String message)
    {
        // Send command to the Bluetooth device
        return appState.write(message);
    }

    protected void disconnect()
    {
        // Disconnect from the Bluetooth device
        if(BlueToothControlApp.D) Log.i(TAG, "Connection end request");
        appState.disconnect();
    }

    public boolean handleMessage(Message msg)
    {
        switch(msg.what)
        {
            case BlueToothControlApp.MSG_OK:
                // When a child activity returns safely
                if(BlueToothControlApp.D) Log.i(TAG, "Child activity OK");
                break;
            case BlueToothControlApp.MSG_CANCEL:
                // When a child activity returns after being canceled (eg. if the connection is lost) cancel this activity
                if(BlueToothControlApp.D) Log.e(TAG, "Got canceled");
                setResult(BlueToothControlApp.MSG_CANCEL, new Intent());
                finish();
                break;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Message.obtain(new Handler(this), resultCode).sendToTarget();
    }

    @Override
    protected void onResume()
    {

        TAG = getLocalClassName();
        if(BlueToothControlApp.D) Log.i(TAG, "Set handler");
        // Set the handler to receive messages from the main application class
        appState.setActivityHandler(new Handler(this));
        preventCancel = false;
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            // Behave as if the back button was clicked
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        // Pressing the back button quits the activity and informs the parent activity
        if(BlueToothControlApp.D) Log.i(TAG, "Back pressed");
        setResult(BlueToothControlApp.MSG_OK, new Intent());
        finish();
    }

    @Override
    public void finish()
    {
        // Remove the handler from the main application class
        appState.setActivityHandler(null);
        super.finish();
    }

    @Override
    protected void onPause()
    {
        // Pausing an activity not allowed, unless it has been prevented
        if(!preventCancel)
        {
            Message.obtain(new Handler(this), BlueToothControlApp.MSG_CANCEL).sendToTarget();
        }
        super.onPause();
    }
}
