package com.example.pslab1fxclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import static com.example.pslab1fxclient.ClientController.ECHO;
import static com.example.pslab1fxclient.ClientController.INFO;

public class Client {
    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input;
    private final ClientController c;

    private static final int BUSY = 2;
    private static final int ALLOWED = 1;
    private static final int DENIED = 0;

    public Client(ClientController c) { this.c = c; }

    public void connect(String ip, Integer port) throws IOException {
        socket = new Socket(ip, port);
        output = new DataOutputStream(socket.getOutputStream());
        input = new DataInputStream(socket.getInputStream());

        int validator = input.readInt();
        if (validator == BUSY) {
            c.sendAlert(INFO, "Server is busy. Try again later.");
            socket.close();
            output.close();
            input.close();
            throw new IOException();
        } else if (validator == DENIED) {
            socket.close();
            output.close();
            input.close();
            throw new IOException();
        }
    }

    public void echo(String message) throws IOException {
        output.writeUTF(message);
        String echo = input.readUTF();

        c.sendAlert(ECHO, "Message sent [" + message.getBytes().length + " bytes]: " + message);
        c.sendAlert(ECHO, "Server echo [" + echo.getBytes().length + " bytes]: " + echo);
    }

    public void disconnect() throws IOException {
        if (socket != null) {
            socket.close();
            input.close();
            output.close();
        }
    }
}
