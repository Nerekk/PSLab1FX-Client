package com.example.pslab1fxclient;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
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

    private Client client;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client = new Client(this);
    }

    public String getIp () {
        String ipPattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        String ip = tServerIp.getText();
        Pattern pattern = Pattern.compile(ipPattern);
        Matcher matcher = pattern.matcher(ip);

        if (matcher.matches() || ip.equals("localhost")) {
            return ip;
        } else {
            return "";
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

    public void sendAlert(String alert) {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentTimeString = "[" + currentTime.format(formatter) + "]";
        String sta = clientTextArea.getText();
        String info;
        if (sta.isEmpty()) {
            info = currentTimeString + " " + alert;
        } else {
            info = "\n" + currentTimeString + " " + alert;
        }
        clientTextArea.appendText(info);
    }
}