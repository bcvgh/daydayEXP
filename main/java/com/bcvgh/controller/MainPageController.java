package com.bcvgh.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class MainPageController {
    @FXML
    private Tab VulManager;

//    @FXML
//    private MenuItem reloadPOC;

    @FXML
    private MenuItem setProxy;


    @FXML
    void  SetProxy(ActionEvent event){
        Stage newTargetStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/com/bcvgh/SetProxy.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        newTargetStage.setTitle("-代理设置-");
        Scene scene = new Scene(root);
        newTargetStage.setScene(scene);
        newTargetStage.setResizable(false);
        newTargetStage.show();
        /**new**/
        Window window = newTargetStage.getScene().getWindow();
        window.setOnCloseRequest((e) -> {
            window.hide();
        });
    }

    public void initialize(){

    }






}
