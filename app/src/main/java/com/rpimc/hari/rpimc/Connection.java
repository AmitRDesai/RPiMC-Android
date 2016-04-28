package com.rpimc.hari.rpimc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Hari on 10/16/2015.
 */
public class Connection {
    public int connectionID;
    private Socket socket;
    private int status;

    public Connection(Socket socket) {
        this.status = 0;
        this.socket = socket;
    }

    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    public OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }

    public int getStatus() {
        return status;
    }

    public void close() {
        try {
            status = 1;
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
