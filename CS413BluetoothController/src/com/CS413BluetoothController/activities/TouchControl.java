package com.CS413BluetoothController.activities;

import android.os.Bundle;

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
    private String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.touch_control);

        rbElbow = (RadioButton) findViewById(R.id.rbElbow);
        rbWrist = (RadioButton) findViewById(R.id.rbWrist);

        btUp = (Button) findViewById(R.id.btUp);
        btUp.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                moveUp();
            }
        });

        btDown = (Button) findViewById(R.id.btDown);
        btDown.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                moveDown();
            }
        });

        btLeft = (Button) findViewById(R.id.btLeft);
        btLeft.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                moveLeft();
            }
        });

        btRight = (Button) findViewById(R.id.btRight);
        btRight.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                moveRight();
            }
        });

        btGrabOpen = (Button) findViewById(R.id.btGrabOpen);
        btGrabOpen.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                openGrab();
            }
        });

        btGrabClose = (Button) findViewById(R.id.btGrabClose);
        btGrabClose.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                closeGrab();
            }
        });
    }

    public void moveLeft() {

        if(rbElbow.isChecked())
        {
            msg = "ELX";
            write(msg);

        }
        else{
            msg = "WLX";
            write(msg);
        }

    }

    public void moveRight() {

        if(rbElbow.isChecked())
        {
            msg = "ERX";
            write(msg);

        }
        else{
            msg = "WRX";
            write(msg);
        }

    }

    public void moveUp() {

        if(rbElbow.isChecked())
        {
            msg = "EUX";
            write(msg);

        }
        else{
            msg = "WUX";
            write(msg);
        }

    }

    public void moveDown() {

        if(rbElbow.isChecked())
        {
            msg = "EDX";
            write(msg);

        }
        else{
            msg = "WDX";
            write(msg);
        }

    }

    public void closeGrab() {

            msg = "GCX";
            write(msg);

    }

    public void openGrab() {

            msg = "GOX";
            write(msg);

    }

}
