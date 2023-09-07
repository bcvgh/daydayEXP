package com.bcvgh.controller;

import com.bcvgh.core.poc.PocUsageImp;
import com.bcvgh.utils.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VulManagerController {

    @FXML
    private TextField Address;

    @FXML
    private Button VulScan;

    @FXML
    private MenuBar VulSelect;

    @FXML
    private Menu VulChoice;

    @FXML
    private TextArea VulOut;

    @FXML
    private Button VulExploit;

    public HashMap<String , ArrayList<HashMap<String,String>>> pocParse = new HashMap<>();

    public void initialize(){
        pocParse = PocUtil.PocParse(PocUtil.PocPath);
        PocUtil.GetTagName();
        try {
            GenList(pocParse);
        }
        catch (Exception e){
            System.out.println("加载poc失败，请检查Poc文件格式！");
        }
    }

    /**生成漏洞列表**/
    private void GenList(HashMap<String , ArrayList<HashMap<String,String>>> pocParse){
        for (Map.Entry<String, ArrayList<HashMap<String,String>>> entry : pocParse.entrySet()) {
            String tag = entry.getKey();
            ArrayList<HashMap<String,String>> names = entry.getValue();
            Menu VulTag = new Menu(PocUtil.GetTagCN(tag));
//            VulTag.getStyleClass().add("custom-menu-item");
            VulTag.setOnAction(event -> {
                VulChoice.setText(VulTag.getText());
                PocUtil.tag = tag;
                PocUtil.name = "";
                PocUtil.type = "";

                VulChoice.hide();
            });
            Iterator<HashMap<String,String>> iterator = names.iterator();
            while (iterator.hasNext()) {
//                String a = iterator.next();
                HashMap<String,String> b = iterator.next();
                MenuItem subVn = new MenuItem(b.get("name"));
//                subVn.getStyleClass().add("custom-menu-item");
                subVn.setOnAction(event -> {
                    VulChoice.setText(subVn.getText());
                    PocUtil.name = b.get("name");
                    PocUtil.type = b.get("type");
                    PocUtil.tag = b.get("tag");
                });
                VulTag.getItems().add(subVn);
            }
            VulChoice.getItems().add(VulTag);
        }
    }

    @FXML
    void NewExploit(ActionEvent event) {
        PocUtil.url = this.Address.getText();
        if (PocUtil.GetPocs(PocUtil.tag, PocUtil.name).keySet().contains("exp")){
            if (PocUtil.url!=null && !PocUtil.name.equals("")){
                Stage newTargetStage = new Stage();
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("/com/bcvgh/VulExploit.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                newTargetStage.setTitle("-漏洞利用-");
                Scene scene = new Scene(root);
                newTargetStage.setResizable(false);
                newTargetStage.setScene(scene);
                newTargetStage.show();
            }
            else {
                PromptUtil.Alert("提示","请输入正确url地址或选择正确的漏洞模块！");
            }
        }
        else {
            PromptUtil.Alert("提示","json文件内无exp,请检查json文件并自行配置!");
        }


    }

    @FXML
    void NewScan(ActionEvent event) {
        this.VulOut.setText("");
        String url = Address.getText();
        ArrayList<String> prompt = VulScan(url);
        try {
            for (String i : prompt){
                Platform.runLater(() -> VulOut.appendText(i));
            }
        }catch (Exception e){

        }

    }

    private ArrayList<String> VulScan(String url){
        Pattern urlPat = Pattern.compile("^(https?://)",Pattern.DOTALL);
        Matcher urlMatch = urlPat.matcher(url);
        PocUsageImp pocPocUsageImp =null;
        if (urlMatch.find()){
            if (PocUtil.name.isEmpty()){
                pocPocUsageImp = new PocUsageImp(url, PocUtil.tag);
            }
            else {
                pocPocUsageImp = new PocUsageImp(url, PocUtil.tag,PocUtil.name);
            }
            return pocPocUsageImp.prompt;
        }
        else {
            PromptUtil.Alert("提示","请输入正确的地址!(以http://或https://开头)");
            return null;
        }
    }

//    @FXML
//    public void ReloadPOC(ActionEvent event) {
//        pocParse = PocUtil.PocParse(PocUtil.PocPath);
//        PocUtil.GetTagName();
//        try {
//            GenList(pocParse);
//        }
//        catch (Exception e){
//            System.out.println("加载poc失败，请检查Poc文件格式！");
//        }
//    }
}
