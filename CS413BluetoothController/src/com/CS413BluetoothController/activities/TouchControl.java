package com.CS413BluetoothController.activities;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.*;
import android.view.View.OnTouchListener;

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
    private RadioButton rbElbow, rbShoulder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.touch_control);

        rbElbow = (RadioButton) findViewById(R.id.rbElbow);
        rbShoulder = (RadioButton) findViewById(R.id.rbShoulder);

        btUp = (Button) findViewById(R.id.btUp);
        btUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String msg;
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(rbElbow.isChecked()) {
                        msg = "EUX";
                    }
                    else {
                        msg = "SUX";
                    }
                    sendCommand(msg);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    msg = "C";
                    sendCommand(msg);
                    return true;
                }
                return false;
            }
        });

        btDown = (Button) findViewById(R.id.btDown);
        btDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String msg;
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(rbElbow.isChecked()) {
                        msg = "EDX";
                    }
                    else {
                        msg = "SDX";
                    }
                    sendCommand(msg);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    msg = "C";
                    sendCommand(msg);
                    return true;
                }
                return false;
            }
        });
        /*btDown.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
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
        }));*/

        btLeft = (Button) findViewById(R.id.btLeft);
        /*btLeft.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg;

                    msg = "L";


                sendCommand(msg);
            }
        }));*/
        btLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String msg;
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    msg = "LX";
                    sendCommand(msg);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    msg = "C";
                    sendCommand(msg);
                    return true;
                }
                return false;
            }
        });

        btRight = (Button) findViewById(R.id.btRight);
        btRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String msg;
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    msg = "RX";
                    sendCommand(msg);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    msg = "C";
                    sendCommand(msg);
                    return true;
                }
                return false;
            }
        });
        /*btRight.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
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
        }));*/

        btGrabOpen = (Button) findViewById(R.id.btGrabOpen);
        btGrabOpen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String msg;
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    msg = "GOX";
                    sendCommand(msg);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    msg = "C";
                    sendCommand(msg);
                    return true;
                }
                return false;
            }
        });

        btGrabClose = (Button) findViewById(R.id.btGrabClose);
        btGrabClose.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String msg;
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    msg = "GX";
                    sendCommand(msg);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    msg = "C";
                    sendCommand(msg);
                    return true;
                }
                return false;
            }
        });
    }

    public void sendCommand(String liveMsg){
        {

                Log.d("Test touch commands", liveMsg);
                write(liveMsg);

        }
    }





}
