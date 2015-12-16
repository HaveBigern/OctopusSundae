package com.CS413BluetoothController;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by GavinDonnelly on 13/11/2015.
 *
 * Using the Base adapter common pattern
 *
 * Base adapter for the action list can have rows of the different actitys that
 * start when selected by user
 */
public class ActionListBaseAdapter extends BaseAdapter {

    private static ArrayList<Action> actionArrayList;

    private LayoutInflater mInflater;

    public ActionListBaseAdapter(Context context, ArrayList<Action> results)
    {
        actionArrayList = results;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount()
    {
        return actionArrayList.size();
    }

    public Object getItem(int position)
    {
        return actionArrayList.get(position);
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
            convertView = mInflater.inflate(R.layout.action_row_view, null);
            holder = new ViewHolder();
            holder.tvAction = (TextView) convertView.findViewById(R.id.tvAction);
            holder.tvDescription = (TextView) convertView.findViewById(R.id.tvDesc);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvAction.setText(actionArrayList.get(position).getAction());
        holder.tvDescription.setText(actionArrayList.get(position).getDescripiton());

        return convertView;
    }

    static class ViewHolder
    {
        TextView tvAction;
        TextView tvDescription;
    }
}
