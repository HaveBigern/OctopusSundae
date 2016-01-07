package com.CS413BluetoothController;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by GavinDonnelly on 15/11/2015.
 *
 * Using the Base adapter common pattern
 *
 * Base adapter for the action list can have rows of the different bluetooth devices that
 * start when selected by user
 */
public class BTDeviceListBaseAdapter extends BaseAdapter {

    private ArrayList<BTDevice> deviceArrayList;

    private LayoutInflater mInflater;

    public BTDeviceListBaseAdapter(Context context, ArrayList<BTDevice> results)
    {
        deviceArrayList = results;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount()
    {
        return deviceArrayList.size();
    }

    public Object getItem(int position)
    {
        return deviceArrayList.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;

        if(convertView == null)
        {
            convertView = mInflater.inflate(R.layout.btdevice_row_view, null);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
            holder.tvSignal = (TextView) convertView.findViewById(R.id.tvSignal);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(deviceArrayList.get(position).getName());
        holder.tvAddress.setText(deviceArrayList.get(position).getAddress());
        if(!deviceArrayList.get(position).getSignal().equals("0"))
        {
            holder.tvSignal.setText(deviceArrayList.get(position).getSignal() + "dBm");
        }

        return convertView;
    }

    static class ViewHolder
    {
        TextView tvName;
        TextView tvAddress;
        TextView tvSignal;
    }
}
