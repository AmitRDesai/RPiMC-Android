package com.rpimc.hari.rpimc;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Hari on 10/16/2015.
 */
public class ConnectionManager {
    public static void initialize(Context context) {
        if (Logo.getPrefs.getBoolean("start_host", true))
            Hotspot.start(context);
    }

    public static Connection connect() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(9090);
            return new Connection(serverSocket.accept());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void disconnect(Context context) {
        Hotspot.stop(context);
    }
}
