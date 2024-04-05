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

    public void connect(String ip, Integer port) {
        try {
            socket = new Socket(ip, port);
            c.sendAlert("Connected");
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
        } catch (UnknownHostException e) {
            c.sendAlert("Unknown Host");
        } catch (IOException e) {
            c.sendAlert("IOException [connect]");
        }
    }

    public void echo(String message) {
        try {
            output.writeUTF(message);
            c.sendAlert("Message: " + message + " sent");

            String echo = input.readUTF();
            c.sendAlert("Server echo: " + echo);
        } catch (IOException e) {
            c.sendAlert("IOException [echo]");
        }
    }

    public void disconnect() {
        try {
            socket.close();
            c.sendAlert("Disconnected");
        } catch (IOException e) {
            c.sendAlert("IOException [disconnect]");
        }
    }
}
