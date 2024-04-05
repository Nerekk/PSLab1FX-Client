package com.example.pslab1fxclient;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientController implements Initializable {

    @FXML
    private TextField tServerPort;

    @FXML
    private TextField tServerIp;

    @FXML
    private TextField tMessage;

    @FXML
    private TextArea clientTextArea;

    @FXML
    private Button bSend;

    @FXML
    private Button bConnect;

    @FXML
    private Button bDisconnect;

    public final static int INFO = 0;
    public final static int ECHO = 1;
    public final static int ERROR = 2;

    private Client client;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client = new Client(this);
    }

    @FXML
    protected void connect() {
        String ip = getIp();
        if (ip == null) {
            sendAlert(ERROR, "Given ip is in wrong format!");
            return;
        }
        Integer port = getPort();
        if (port == -1) {
            sendAlert(ERROR, "Given port is not a number!");
            return;
        }

        try {
            sendAlert(INFO, "Trying to connect..");
            client.connect(ip, port);
        } catch (UnknownHostException e) {
            sendAlert(ERROR, "Unknown Host");
            return;
        } catch (IOException e) {
            sendAlert(ERROR, "Cannot connect to this server");
            return;
        }
        sendAlert(INFO, "Connected");
        switchButtonsLock();
    }

    @FXML
    protected void disconnect() {
        try {
            client.disconnect();
        } catch (IOException e) {
            sendAlert(ERROR, "IOException [disconnect]");
            return;
        }
        sendAlert(INFO, "Disconnected");
        switchButtonsLock();
    }

    @FXML
    protected void send() {
        try {
            client.echo(getMessage());
        } catch (IOException e) {
            sendAlert(ERROR, "Server connection lost");
            disconnect();
        }
    }

    public String getIp () {
        String ipPattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        String ip = tServerIp.getText();
        Pattern pattern = Pattern.compile(ipPattern);
        Matcher matcher = pattern.matcher(ip);

        if (matcher.matches() || ip.equals("localhost")) {
            return ip;
        } else {
            return null;
        }
    }

    public Integer getPort() {
        Integer port;
        try {
            port = Integer.parseInt(tServerPort.getText());
        } catch (NumberFormatException e) {
            return -1;
        }
        return port;
    }
    public String getMessage() {
        return tMessage.getText();
    }

    public void switchButtonsLock() {
        if (bConnect.isDisabled()) {
            bConnect.setDisable(false);
            bDisconnect.setDisable(true);
            bSend.setDisable(true);
        } else {
            bConnect.setDisable(true);
            bDisconnect.setDisable(false);
            bSend.setDisable(false);
        }
    }

    public void sendAlert(int type, String alert) {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentTimeString = "[" + currentTime.format(formatter) + "]";
        String sta = clientTextArea.getText();

        switch (type) {
            case INFO -> alert = "[INFO] " + alert;
            case ECHO -> alert = "[ECHO] " + alert;
            case ERROR -> alert = "[ERROR] " + alert;
        }

        String info;
        if (sta.isEmpty()) {
            info = currentTimeString + " " + alert;
        } else {
            info = "\n" + currentTimeString + " " + alert;
        }

        clientTextArea.appendText(info);
    }
}