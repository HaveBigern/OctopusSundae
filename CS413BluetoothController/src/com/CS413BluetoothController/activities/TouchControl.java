package com.CS413BluetoothController.activities;

import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.Button;

import android.widget.RadioButton;

import com.CS413BluetoothController.BluetoothActivity;
import com.CS413BluetoothController.R;

/**
 * Created by GavinDonnelly on 17/11/2015.
 *
 * For touch controls
 */
public class TouchControl extends BluetoothActivity {

    private Button btUp, btDown, btLeft, btRight, btGrabOpen, btGrabClose;
    private RadioButton rbElbow, rbWrist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.touch_control);

        rbElbow = (RadioButton) findViewById(R.id.rbElbow);
        rbWrist = (RadioButton) findViewById(R.id.rbWrist);

        btUp = (Button) findViewById(R.id.btUp);
        btUp.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String msg;
                        if(rbElbow.isChecked())
                        {

                            msg = "EUX";

                        }
                        else {
                            msg = "WUX";

                        }
                        sendCommand(msg);
                    }
                }));

        btDown = (Button) findViewById(R.id.btDown);
        btDown.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg;
                if(rbElbow.isChecked())
                {

                    msg = "EDX";

                }
                else {
                    msg = "WDX";

                }
                sendCommand(msg);
            }
        }));

        btLeft = (Button) findViewById(R.id.btLeft);
        btLeft.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg;
                if(rbElbow.isChecked())
                {

                    msg = "ELX";

                }
                else {
                    msg = "WLX";

                }
                sendCommand(msg);
            }
        }));

        btRight = (Button) findViewById(R.id.btRight);
        btRight.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg;
                if(rbElbow.isChecked())
                {

                    msg = "ERX";

                }
                else {
                    msg = "WRX";

                }
                sendCommand(msg);
            }
        }));

        btGrabOpen = (Button) findViewById(R.id.btGrabOpen);
        btGrabOpen.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg;

                    msg = "GOX";

                sendCommand(msg);
            }
        }));

        btGrabClose = (Button) findViewById(R.id.btGrabClose);
        btGrabClose.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg;

                msg = "GCX";

                sendCommand(msg);
            }
        }));
    }

    public void sendCommand(String liveMsg){
        {

                //Log.d("Test touch commands", liveMsg);
                write(liveMsg);

        }
    }





}
