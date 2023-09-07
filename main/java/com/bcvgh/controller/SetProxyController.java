package com.bcvgh.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

public class SetProxyController {

    @FXML
    private Button proxyCancel;

    @FXML
    private Button proxySave;

    @FXML
    private ComboBox<String> proxyType;

    @FXML
    private TextField proxyIp;

    @FXML
    private TextField proxyPort;

    @FXML
    private TextField proxyUser;

    @FXML
    private TextField proxyPass;

    @FXML
    private RadioButton upRadio;

    @FXML
    private RadioButton downRadio;

//    private Label proxyStatusLabel;

    public static Map proxySetting = new HashMap<>();

    public void initialize(){
        ToggleGroup toggleGroup = new ToggleGroup();
        this.downRadio.setToggleGroup(toggleGroup);
        this.upRadio.setToggleGroup(toggleGroup);
        this.proxyType.setItems(FXCollections.observableArrayList(new String[]{"HTTP", "SOCKS"}));
        this.proxyType.getSelectionModel().select(0);
        if (SetProxyController.proxySetting.get("proxy") != null) {
            Proxy currProxy = (Proxy)SetProxyController.proxySetting.get("proxy");
            String proxyInfo = currProxy.address().toString();
            String[] info = proxyInfo.split(":");
            String hisIpAddress = info[0].replace("/", "");
            String hisPort = info[1];
            this.proxyIp.setText(hisIpAddress);
            this.proxyPort.setText(hisPort);
            this.upRadio.setSelected(true);
            System.out.println(proxyInfo);
        } else {
            upRadio.setSelected(false);
        }
    }



    public void ProxySave(javafx.event.ActionEvent event) {
        String ipAddress = new String();
        String port = new String();
        String type = new String();
        if (this.downRadio.isSelected()){
            SetProxyController.proxySetting.put("proxy",(Object) null);
        }else {
            String user = new String();
            String pass = new String();
            if (!proxyUser.getText().trim().equals("")) {
                user = proxyUser.getText().trim();
                pass = proxyPass.getText().trim();
                String finalUser = user;
                String finalPass = pass;
                Authenticator.setDefault(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(finalUser, finalPass.toCharArray());
                    }
                });
            }else {
                Authenticator.setDefault((Authenticator)null);
            }
            proxySetting.put("username",proxyUser.getText());
            proxySetting.put("password",proxyPass.getText());
            ipAddress = proxyIp.getText();
            port = proxyPort.getText();
            InetSocketAddress proxyAddr = new InetSocketAddress(ipAddress, Integer.parseInt(port));
            type = ((String)this.proxyType.getValue()).toString();
            Proxy proxy;
            if (type.equals("HTTP")) {
                proxy = new Proxy(Proxy.Type.HTTP, proxyAddr);
                proxySetting.put("proxy", proxy);
            } else if (type.equals("SOCKS")) {
                proxy = new Proxy(Proxy.Type.SOCKS, proxyAddr);
                proxySetting.put("proxy", proxy);
            }
        }
        this.proxySave.getScene().getWindow().hide();
    }

    public void ProxyCancel(javafx.event.ActionEvent event) {
        this.proxyCancel.getScene().getWindow().hide();
    }
}
