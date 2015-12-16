package com.CS413BluetoothController.activities;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import com.CS413BluetoothController.BluetoothActivity;
import com.CS413BluetoothController.R;

/**
 * Created by GavinDonnelly on 16/11/2015.
 *
 *
 * Made again through developers.google.com documentation and lots of stack overflow articles,
 * it works pretty well but can skip frames sometimes when it get cloged up, thats ok tho just lags a bit
 * to fix this should do thread management better, but it rarely happens and only for a few seconds.
 *
 */
public class AccelControl extends BluetoothActivity implements SensorEventListener {

    private AccelView svAccelerometer;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private boolean enabled = false;
    private TextView tvX, tvY;
    private float lastX = 0, lastY = 0, accJitterMargin = 0.1f;
    private RadioButton rbElbow, rbWrist;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.accelerometer_control);

        // Set up accelerometer output fields
        tvX = (TextView) findViewById(R.id.tvX);
        tvY = (TextView) findViewById(R.id.tvY);

        rbElbow = (RadioButton) findViewById(R.id.rbElbow);
        rbWrist = (RadioButton) findViewById(R.id.rbWrist);

        final Button btStart = (Button) findViewById(R.id.btStart);
        btStart.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                enabled = !enabled;
                if(enabled)
                {
                    btStart.setText(R.string.stop);
                }
                else
                {
                    btStart.setText(R.string.start);
                    write("q");
                }
            }
        });

        svAccelerometer = (AccelView) findViewById(R.id.svLine);
        // Needed to make the SurfaceView background transparent
        svAccelerometer.setZOrderOnTop(true);
        svAccelerometer.getHolder().setFormat(PixelFormat.TRANSPARENT);
    }

    @Override
    protected void onResume()
    {
        enabled = false;
        lastX = 0;
        lastY = 0;
        accJitterMargin = 0.1f;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        svAccelerometer.onResumeMySurfaceView();
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        svAccelerometer.onPauseMySurfaceView();
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    public void onAccuracyChanged(Sensor arg0, int arg1)
    {

    }

    public void onSensorChanged(SensorEvent event)
    {
        float x = event.values[0];
        float y = event.values[1];

        // Some accelerometer sensors can change very often for no reason
        boolean change = false;

        if(Math.abs(lastX - x) > accJitterMargin)
        {
            lastX = x;
            change = true;
        }

        if(Math.abs(lastY - y) > accJitterMargin)
        {
            lastY = y;
            change = true;
        }

        // No significant enough change
        if(!change)
        {
            return;
        }

        // Show accelerator sensor values
        tvX.setText("X: " + String.format("%.02f", lastX));
        tvY.setText("Y: " + String.format("%.02f", lastY));

        svAccelerometer.setVector(lastX, lastY);
        if (enabled) {

            if (lastX < -1.5) {
                String msg;
                if (rbElbow.isChecked()) {

                    msg = "ERX";

                } else {
                    msg = "WRX";

                }
                write(msg);
                //Log.d("Test touch commands", msg);
            } else if (lastX > 1.5) {
                String msg;
                if (rbElbow.isChecked()) {

                    msg = "ELX";

                } else {
                    msg = "WLX";

                }
                write(msg);
                //Log.d("Test touch commands", msg);
            } else if (lastY < -1.5) {
                String msg;
                if (rbElbow.isChecked()) {

                    msg = "EUX";

                } else {
                    msg = "WUX";

                }
                write(msg);
                //Log.d("Test touch commands", msg);
            } else if (lastY > 1.5) {
                String msg;
                if (rbElbow.isChecked()) {

                    msg = "EDX";

                } else {
                    msg = "WDX";

                }
                write(msg);
                //Log.d("Test touch commands", msg);
            } else {
                return;
            }
        }
        else{
            return;
        }

    }
}
