package com.CS413BluetoothController.activities;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by GavinDonnelly on 16/11/2015.
 *
 * Displays log of commands sent and responses for text data send activity
 *
 */
public class LogView extends TextView implements TextWatcher {

    // Allow a maximum number of 30 lines
    private static final int maxLines = 30;
    private String data;

    public LogView(Context context)
    {
        super(context);
        init();
    }

    public LogView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LogView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    private void init()
    {

        addTextChangedListener(this);
        // If the content is too long: scroll
        setMovementMethod(new ScrollingMovementMethod());
        // Make the text appear at the bottom
        setGravity(Gravity.BOTTOM);
    }

    public void afterTextChanged(Editable s)
    {
        // Limit lines
        data = getText().toString();
        if(data.split("\n").length > maxLines)
        {
            data = data.substring(data.indexOf("\n") + 1);

            s.replace(0, length(), data);
        }
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
        // Not used
    }

    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        // Not used
    }
}
