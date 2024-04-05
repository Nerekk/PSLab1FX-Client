package com.example.pslab1fxclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input;
    private ClientController c;

    public Client(ClientController c) { this.c = c; }

    public void connect(String ip, Integer port) throws IOException {
        socket = new Socket(ip, port);
        output = new DataOutputStream(socket.getOutputStream());
        input = new DataInputStream(socket.getInputStream());
    }

    public void echo(String message) throws IOException {
        output.writeUTF(message);
        String echo = input.readUTF();

        c.sendAlert("Message: " + message + " sent");
        c.sendAlert("Server echo: " + echo);
    }

    public void disconnect() throws IOException {
        if (socket != null) socket.close();
    }
}
