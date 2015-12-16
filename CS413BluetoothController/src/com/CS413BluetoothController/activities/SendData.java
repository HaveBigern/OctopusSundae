package com.CS413BluetoothController.activities;

import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import com.CS413BluetoothController.BlueToothControlApp;
import com.CS413BluetoothController.BluetoothActivity;
import com.CS413BluetoothController.R;

/**
 * Created by GavinDonnelly on 16/11/2015.
 *
 * Send text data activity
 *
 */
public class SendData extends BluetoothActivity {

    private LogView tvData;
    private EditText etCommand;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.send_text_data);

        // Prepare the UI
        tvData = (LogView) findViewById(R.id.tvLogData);

        etCommand = (EditText) findViewById(R.id.etCommand);
        etCommand.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                // When the enter button of the keyboard is pressed the message is sent
                if(actionId == EditorInfo.IME_ACTION_SEND)
                {
                    // Sending a command clears the input
                    String msg = etCommand.getText().toString();
                    if(!msg.equals(""))
                    {
                        write(msg);
                        etCommand.setText("");
                    }
                }
                // Must return true or the keyboard will be hidden
                return true;
            }
        });
    }

    @Override
    public boolean handleMessage(Message msg)
    {
        super.handleMessage(msg);
        switch(msg.what)
        {
            case BlueToothControlApp.MSG_READ:
                tvData.append("R: " + msg.obj + "\n");
                break;
            case BlueToothControlApp.MSG_WRITE:
                tvData.append("S: " + msg.obj + "\n");
                break;
        }
        return false;
    }
}
