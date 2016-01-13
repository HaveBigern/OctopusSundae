package com.CS413BluetoothController;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by GavinDonnelly on 13/11/2015.
 * List of activitys the arm can do with the user selecting one
 */
public class ActionListActivity extends BluetoothActivity {

    private ArrayList<Action> activityList = new ArrayList<Action>();
    private ListView lvActionList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.action_list);

        lvActionList = (ListView) findViewById(R.id.lvActionList);

        activityList.add(new Action("Accelerometer Control", "Control your robot by tilting the phone", "AccelControl"));
        activityList.add(new Action("Touch Control", "Control robot's movements by touch", "TouchControl"));
        activityList.add(new Action("Send Data", "Send custom commands to robot", "SendData"));




        lvActionList.setAdapter(new ActionListBaseAdapter(this, activityList));
        lvActionList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id)
            {
                String activity = activityList.get(position).getClassName();

                try
                {
                    // Start the selected activity and prevent quitting
                    preventCancel = true;
                    Class<?> activityClass = Class.forName("com.CS413BluetoothController.activities." + activity);
                    Intent intent = new Intent(ActionListActivity.this, activityClass);
                    startActivityForResult(intent, 0);
                }
                catch(ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean handleMessage(Message msg)
    {
        super.handleMessage(msg);
        // When a child activity returns it passes ok or cancel message
        if(msg.what == BlueToothControlApp.MSG_OK)
        {
            // When quitting an activity automatically reset the robot
            write("C");
        }
        return false;
    }

    @Override
    public void onBackPressed()
    {
        // When quitting the activity select reset and disconnect from the device
        disconnect();
        super.onBackPressed();
    }
}
