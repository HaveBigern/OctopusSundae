package com.CS413BluetoothController;

/**
 * Created by GavinDonnelly on 13/11/2015.
 *
 * This class is more of a type stlye class, the idea of this class
 * is to hold an activity with its title (name), description and class name
 * shows this data of activityes in ActionListActity
 *
 * This class and BTDevice are inherently the same and follow the same
 * design pattern and ideas
 */
public class Action {

    private String action = "";
    private String descripiton = "";
    private String className = "";

    public Action(String name, String descripiton, String className)
    {
        this.action = name;
        this.descripiton = descripiton;
        this.className = className;
    }

    public String getAction()
    {
        return action;
    }

    public String getDescripiton()
    {
        return descripiton;
    }

    public String getClassName()
    {
        return className;
    }
}
