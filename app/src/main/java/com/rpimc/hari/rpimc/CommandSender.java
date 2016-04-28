package com.rpimc.hari.rpimc;

import android.content.Context;
import android.util.Log;

import java.io.ObjectOutputStream;

/**
 * Created by Hari on 10/30/2015.
 */
public class CommandSender {
    private Connection con;

    public CommandSender(Connection con) {
        this.con = con;
    }

    public void send(Command command) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
            oos.writeObject(command);
            oos.flush();
        } catch (Exception e) {
            Logo.connected = false;
//           Log.i("AndroidRuntime",e.getMessage());
        }
    }
}