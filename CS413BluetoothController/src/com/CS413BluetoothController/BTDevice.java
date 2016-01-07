package com.CS413BluetoothController;

/**
 * Created by GavinDonnelly on 15/11/2015.
 *
 * This is used to hold info about bt device name MAC address and signal strencth
 *
 * This class and Action are inherently the same and follow the same
 * design pattern and ideas
 */
public class BTDevice {
    private String name = "";
    private String address = "";
    private String signal = "";

    public BTDevice(String name, String address, Short signal)
    {
        this.name = name;
        this.address = address;
        this.signal = Short.toString(signal);
    }

    public String getName()
    {
        return name;
    }

    public String getAddress()
    {
        return address;
    }

    public String getSignal()
    {
        return signal;
    }
}
