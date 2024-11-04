package com.bcvgh.controller;

import com.bcvgh.utils.Constant;
import com.bcvgh.utils.PromptUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class MainPageController {
    @FXML
    private Tab VulManager;


    @FXML
    private MenuItem setProxy;


    @FXML
    private MenuItem RenewPoc;

    private static final Logger LOGGER = LogManager.getLogger(MainPageController.class.getName());


    @FXML
    void RemoteUpdatePOC(ActionEvent event){
        Stage newTargetStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/com/bcvgh/RemoteUpdatePOC.fxml"));
        } catch (IOException e) {
            LOGGER.error(e.getMessage()+"(remote场景初始化失败!)");
        }
        newTargetStage.setTitle("-在线更新POC仓库-");
        Scene scene = new Scene(root);
        newTargetStage.setScene(scene);
        newTargetStage.setResizable(false);
        newTargetStage.show();
    }

    @FXML
    void  SetProxy(ActionEvent event){
        Stage newTargetStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/com/bcvgh/SetProxy.fxml"));
        } catch (IOException e) {
            LOGGER.error(e.getMessage()+"(proxy场景初始化失败!)");
        }
        newTargetStage.setTitle("-代理设置-");
        Scene scene = new Scene(root);
        newTargetStage.setScene(scene);
        newTargetStage.setResizable(false);
        newTargetStage.show();
        Window window = newTargetStage.getScene().getWindow();
        window.setOnCloseRequest((e) -> {
            window.hide();
        });
    }

    @FXML
    void AddPoc(ActionEvent event){
        PromptUtil.Alert("提示","输入Pattern（正则匹配）时，特殊字符（如:\\、,\"已经经过转义，无需再用\\\\、,\\\"转义）");
        Stage newTargetStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/com/bcvgh/AddPoc.fxml"));
        } catch (IOException e) {
            LOGGER.error(e.getMessage()+"(add场景初始化失败!)");
        }
        newTargetStage.setTitle("-新增POC-");
        Scene scene = new Scene(root);
        newTargetStage.setScene(scene);
        newTargetStage.setResizable(false);
        newTargetStage.show();
    }

    public void initialize(){

    }








}
