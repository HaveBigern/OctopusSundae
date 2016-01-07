package com.CS413BluetoothController;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.*;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by GavinDonnelly on 15/11/2015.
 *
 * This deals with comunication between app and robot through bluetooth
 */
public class BTDeviceSelectActivity extends Activity implements Handler.Callback {

    private ArrayList<BTDevice> btdevAvailableList, btdevPairedList;
    private BTDeviceListBaseAdapter btdevAvailableListAdapter, btdevPairedListAdapter;
    private ListView btdevAvailableLV, btdevPairedV;
    private ProgressDialog connectionProgressDialog;
    private Button bFindDevices;
    private BluetoothAdapter bluetoothAdapter;
    private BlueToothControlApp appState;

    private static final String TAG = "DeviceSelect";

    private static final int ACTION_LIST = 0;
    private static final int BT_ENABLE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Request the spinner in upper right corner (this sometimes doesnt work but even if it does not it does not affect app)
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.btdevice_list);

        // Setup Bluetooth devices lists with custom rows
        btdevPairedV = (ListView) findViewById(R.id.lvPairedDevices);
        btdevPairedList = new ArrayList<BTDevice>();
        btdevPairedListAdapter = new BTDeviceListBaseAdapter(this, btdevPairedList);
        btdevPairedV.setAdapter(btdevPairedListAdapter);
        btdevPairedV.setOnItemClickListener(deviceClickListener);

        btdevAvailableLV = (ListView) findViewById(R.id.lvAvailableDevices);
        btdevAvailableList = new ArrayList<BTDevice>();
        btdevAvailableListAdapter = new BTDeviceListBaseAdapter(this, btdevAvailableList);
        btdevAvailableLV.setAdapter(btdevAvailableListAdapter);
        btdevAvailableLV.setOnItemClickListener(deviceClickListener);

        appState = (BlueToothControlApp) getApplicationContext();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Register a receiver to handle Bluetooth actions
        registerReceiver(Receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        registerReceiver(Receiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));

        bFindDevices = (Button) findViewById(R.id.bFindDevices);
        bFindDevices.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                startDiscovery();
            }
        });

        // Automatically start discovery the first time the application starts
        startDiscovery();
    }

    /**
     * When a Bluetooth item is clicked this method tries to connect to it. It
     * shows a dialog that informs the user it's trying to establish a
     * connection.
     */
    final AdapterView.OnItemClickListener deviceClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            // Cancel discovery because it's costly and we're about to connect
            bluetoothAdapter.cancelDiscovery();

            // Get the selected BTDevice
            BTDevice BTDevice = (BTDevice) parent.getItemAtPosition(position);

            // Show connection dialog and make so that the connection it can be canceled
            connectionProgressDialog = ProgressDialog.show(BTDeviceSelectActivity.this, "", "Establishing connection...", false, true);
            connectionProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
            {
                public void onCancel(DialogInterface di)
                {
                    // The user can back out at any moment
                    connectionProgressDialog.dismiss();
                    appState.disconnect();
                    if(BlueToothControlApp.D) Log.i(TAG, "Canceled connection progress");
                    return;
                }
            });

            // Try to connect to the selected BTDevice, once connected the handler will do the rest
            appState.connect(BluetoothAdapter.getDefaultAdapter().getRemoteDevice(BTDevice.getAddress()));
        }
    };

    /**
     * When a message comes from either the Bluetooth activity (when enabling
     * Bluetooth) or the child activity it's processed here.
     */
    public boolean handleMessage(Message msg)
    {
        // In case the connection dialog hasn't disappeared yet
        if(connectionProgressDialog != null)
        {
            connectionProgressDialog.dismiss();
        }

        switch(msg.what)
        {
            case BlueToothControlApp.MSG_OK:
                break;
            case BlueToothControlApp.MSG_CANCEL:
                // The child activity did not end gracefully
                if(msg.obj != null)
                {
                    // If a some text came with the message show in a toast
                    if(BlueToothControlApp.D) Log.i(TAG, "Message: " + msg.obj);
                    Toast.makeText(BTDeviceSelectActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                }
                break;
            case BlueToothControlApp.MSG_CONNECTED:
                if(BlueToothControlApp.D) Log.i(TAG, "Connection successful to " + msg.obj);
                startActivityForResult(new Intent(getApplicationContext(), ActionListActivity.class), ACTION_LIST);
                break;
        }
        return false;
    }

   // Looking for BT devices
    public void startDiscovery()
    {
        // Prevent phones without Bluetooth from using this application
        if(!checkBlueToothState())
        {
            finish();
            return;
        }

        // Show search progress spinner
        setProgressBarIndeterminateVisibility(true);
        // Disable button
        bFindDevices.setText(R.string.searching);
        bFindDevices.setEnabled(false);


        findViewById(R.id.tvAvailableDevices).setVisibility(View.GONE);

        btdevPairedList.clear();
        btdevPairedListAdapter.notifyDataSetChanged();


        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            findViewById(R.id.tvPairedDevices).setVisibility(View.VISIBLE);
            for(BluetoothDevice device : pairedDevices)
            {
                // Signal strength isn't available for paired devices
                btdevPairedList.add(new BTDevice(device.getName(), device.getAddress(), (short) 0));
            }
            // Tell the list adapter that its data has changed so it would update itself
            btdevPairedListAdapter.notifyDataSetChanged();
        }

        btdevAvailableList.clear();
        btdevAvailableListAdapter.notifyDataSetChanged();

        bluetoothAdapter.startDiscovery();
    }

    // Add found device to the devices list
    private final BroadcastReceiver Receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action))
            {
                // Found a device in range
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BTDevice foundBTDevice = new BTDevice(device.getName(), device.getAddress(), intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE));
                // If it's not a paired device add it to the list
                if(device.getBondState() != BluetoothDevice.BOND_BONDED)
                {
                    btdevAvailableList.add(foundBTDevice);

                    btdevAvailableListAdapter.notifyDataSetChanged();

                    findViewById(R.id.tvAvailableDevices).setVisibility(View.VISIBLE);
                }
            }
            else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
            {
                // When finished (timeout) remove the progress indicator and re-enable button
                setProgressBarIndeterminateVisibility(false);
                bFindDevices.setText(R.string.search);
                bFindDevices.setEnabled(true);

            }
        }
    };

    // To tell if bluetooth is enabled or available
    private boolean checkBlueToothState()
    {
        // Inform user that the phone does not have Bluetooth
        if(bluetoothAdapter == null)
        {
            Toast.makeText(getApplicationContext(), "Bluetooth not available.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!bluetoothAdapter.isEnabled())
        {
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), BT_ENABLE);
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case ACTION_LIST:

                Message.obtain(new Handler(this), resultCode);
                break;
            case BT_ENABLE:

                if(!bluetoothAdapter.isEnabled())
                {
                    Toast.makeText(getApplicationContext(), "Bluetooth must be enabled", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    startDiscovery();
                }
                break;
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if(bluetoothAdapter != null)
        {
            bluetoothAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        this.unregisterReceiver(Receiver);
    }

    @Override
    protected void onResume()
    {
        if(BlueToothControlApp.D) Log.i(TAG, "Set handler");
        appState.setActivityHandler(new Handler(this));
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        bluetoothAdapter.cancelDiscovery();
        super.onPause();
    }
}
