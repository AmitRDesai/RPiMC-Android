package com.rpimc.hari.rpimc;

import android.util.Log;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by Amit on 23-Feb-16.
 */
public class SSHConnection {
    public static Session session;
    public static ChannelExec channel;

    static public boolean connect() {
        Object obj = new JSch();
        try {
            String ip=Logo.getPrefs.getString("ip","192.168.43.156");
            session = ((JSch) (obj)).getSession("pi", ip, 22);
            session.setPassword("raspberry");
            obj = new Properties();
            ((Properties) (obj)).put("StrictHostKeyChecking", "no");
            session.setConfig(((Properties) (obj)));
            session.connect();
            Log.i(Logo.TAG, "connected ssh");
        } catch (JSchException jschexception) {
            Log.i("e", jschexception.getMessage());
            return false;
        }
        return true;
    }

    static public boolean disconnect() {
        session.disconnect();
        channel.disconnect();
        return true;
    }

    static public void send(String cmd) {
        try {
            channel = (ChannelExec) session.openChannel("exec");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    channel.getInputStream()));
            channel.setCommand(cmd);
            //channel.setCommand("pwd;");
            channel.connect();
            Log.i(Logo.TAG, "done ssh");
//            String msg = null;
//            while ((msg = in.readLine()) != null) {
//                System.out.println(msg);
//            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }
}
