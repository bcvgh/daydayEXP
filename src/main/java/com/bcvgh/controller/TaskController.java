package com.bcvgh.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;

public class TaskController {

    @FXML
    private MenuBar VulName;

    @FXML
    private Menu VulChoice;

    @FXML
    private TextArea VulOut;

    @FXML
    private Button VulExploit;

    public void  initialize() {
        this.VulChoice.setText(VulManagerController.VulName);
    }


}
