package com.bcvgh.controller;

import com.bcvgh.util.PocUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class TaskController {

    private String tag;

    @FXML
    private MenuBar VulName;

    @FXML
    private AnchorPane Pane;

    @FXML
    private Menu VulChoice;

    @FXML
    private TextArea VulOut;

    @FXML
    private Button VulExploit;

    public void  initialize() {
        this.VulOut.setWrapText(true);
        this.VulOut.setEditable(false);
//        ScrollPane scrollPane = new ScrollPane(this.VulExploit);
//        scrollPane.setFitToWidth(true); // 让 ScrollPane 适应宽度
//        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // 禁用水平滚动条
//        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // 禁用垂直滚动条
        this.VulChoice.setText(VulManagerController.VulName);
        for (Map.Entry<String, ArrayList<String>> entry : VulManagerController.vulName.entrySet()) {
            String tag = entry.getKey();
            ArrayList<String> names = entry.getValue();
            Iterator<String> iterator = names.iterator();
            while (iterator.hasNext()) {
                String item = iterator.next();
                if (item.equals(this.VulChoice.getText())){
                    this.tag = tag;
                }
            }
        }
//        if (this.type){
//            Text cmd = new Text("Command");
//            TextField Command = new TextField();
//            Button exec = new Button();
//            Pane.getChildren().add(cmd);
//            Pane.getChildren().add(Command);
//            Pane.getChildren().add(exec);
//        }

    }

    @FXML
    void Exploit(ActionEvent event) {
        this.VulOut.setText("");
        PocUtil poc = new PocUtil(VulManagerController.Url, this.tag, this.VulChoice.getText());
        if (!poc.EXP().equals("")){
            Platform.runLater(() -> this.VulOut.appendText(poc.EXP()+"\n"));
        }
        else {
            Platform.runLater(()->this.VulOut.appendText("漏洞利用失败，请检查poc可用性"));
        }
    }

}
