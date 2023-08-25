// -*- coding: utf-8 -*-
package com.bcvgh.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcvgh.util.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;


import com.bcvgh.util.PocUtil;


public class VulManagerController {

//    public   HashSet<String> vulTypes = new HashSet<>();
//    public  HashMap<String , JSONObject> vulName = new HashMap<>();
    public static   HashMap<String , ArrayList<String>> vulName = new HashMap<>();
    public static String VulName;
    public static String tag;
    public static String Url;
    public static String type;

    @FXML
//    private ChoiceBox<String> VulChoice;

    public  Menu VulChoice;

    @FXML
    private Button VulScan;

    @FXML
    private TextArea VulOut;

    @FXML
    private Button VulExploit;

    @FXML
    public TextField Address;


    static {
        HashMap<String, ArrayList> VulPoc = new HashMap<>();
//        VulPoc.put("泛微",{"泛微1","泛微2"});
    }


    public void initialize() {
        initPane();
    }

    private void initPane() {

//        VulChoice.getItems().add("--请选择漏洞类型--");
//        VulChoice.getSelectionModel().selectFirst();
//        VulChoice.getItems().add("--请选择漏洞类型--");
//        MenuItem a = new MenuItem("123");
//        VulChoice.getItems().add(a);

        /**遍历poc jsonW文件，显示漏洞列表**/
//        HashMap<String , JSONObject> vulName = new HashMap();
//        HashSet<String> vulTypes = new HashSet<>();
        /**方法1**/
//        String[] dirNames = FileUtil.FileList("src/main/java/com/bcvgh/poc/json");
//        for (String dirName : dirNames){
//            String[] fileNames = FileUtil.FileList("src/main/java/com/bcvgh/poc/json/"+dirName);
//            for (String fileName : fileNames){
//                String poc = FileUtil.FileRead("src/main/java/com/bcvgh/poc/json/"+dirName+"/"+fileName);
//                JSONObject poc3 = JsonUtil.StringToJson(poc);
//                this.vulName.put(poc3.get("name").toString(),poc3);
//                this.vulTypes.add(JsonUtil.StringToJson(poc).get("tag").toString());
//            }
//        }

        /**方法2**/
        String[] dirNames = FileUtil.FileList("src/main/java/com/bcvgh/poc/json/");
        for (String dirName : dirNames) {
            ArrayList<String> filenames = new ArrayList<>();
            String[] fileNames = FileUtil.FileList("src/main/java/com/bcvgh/poc/json/" + dirName);
            for (String fileName : fileNames) {
                String poc = FileUtil.FileRead("src/main/java/com/bcvgh/poc/json/" + dirName + "/" + fileName);
                JSONObject pocJson = JsonUtil.StringToJson(poc);
                filenames.add(pocJson.getString("name"));
            }
            vulName.put(dirName, filenames);
        }
        for (Map.Entry<String, ArrayList<String>> entry : this.vulName.entrySet()) {
            String tag = entry.getKey();
            ArrayList<String> names = entry.getValue();
            Menu VulType = new Menu(tag);
            VulType.setOnAction(event -> {
                VulChoice.setText(VulType.getText());
                VulChoice.hide();
            });
            Iterator<String> iterator = names.iterator();
            while (iterator.hasNext()) {
//                String a = iterator.next();
                MenuItem subVn = new MenuItem(iterator.next());
                subVn.setOnAction(event -> {
                    VulChoice.setText(subVn.getText());
                    VulManagerController.VulName = subVn.getText();
                });
                VulType.getItems().add(subVn);

            }
            VulChoice.getItems().add(VulType);
        }
    }
//        Iterator<String> vts = vulTypes.iterator();
//        while (vts.hasNext()) {
//            String vt = vts.next();
//            Menu VulType = new Menu(vt);
//            VulType.setOnAction(event -> {
////                System.out.println(VulType.getText());
//                VulChoice.setText(VulType.getText());
//                VulChoice.hide();
//            });
//            Iterator vns = vulName.entrySet().iterator();
//            while (vns.hasNext()){
//                Map.Entry entry = (Map.Entry)vns.next();
//                JSONObject a = (JSONObject) entry.getValue();
//                if (a.get("tag").equals(vt)){
//                    MenuItem subVn = new MenuItem((String) entry.getKey());
//
//                    subVn.setOnAction(event -> {
//                        VulChoice.setText(subVn.getText());
//                        VulManagerController.VulName = subVn.getText();
////                        ModelUtil.VulModel((JSONObject)entry.getValue());
//                        System.out.println(subVn.getText());
//                    });
//
//                    VulType.getItems().add(subVn);
//                }
//            }
//            VulChoice.getItems().add(VulType);
//        }

        /**漏洞利用，根据选择的漏洞名称，生生成新的stage舞台**/





//        Menu tutorialManeu = new Menu("Tutorial");
//        tutorialManeu.getItems().addAll(
//                new CheckMenuItem("Java"),
//                new CheckMenuItem("JavaFX"),
//                new CheckMenuItem("Swing"));
//        VulChoice.getItems().add(tutorialManeu);

//        VulChoice.setOnAction((event -> {
////            Integer vulIndex = VulChoice.getSelectionModel().getSelectedIndex();
//
//            }));
//        }


    public void NewExploit(ActionEvent event) {
        VulManagerController.Url = this.Address.getText();
        if (VulManagerController.VulName!=null){
            Stage newTargetStage = new Stage();
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/com/bcvgh/VulExploit.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            newTargetStage.setTitle("-漏洞利用-");
            Scene scene = new Scene(root);
            newTargetStage.setScene(scene);
            newTargetStage.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示");
            alert.setHeaderText(null);
            alert.setContentText("请选择要利用的漏洞。");
            alert.showAndWait();
        }
    }


    @FXML
    public void NewScan(ActionEvent event) {
        String url =Address.getText();
        VulOut.setText("");
        Boolean isVulType = false;
        here:
        if (!url.equals("") && VulChoice.getText()!=null && isVulType.equals(false)){
            for (Map.Entry<String,ArrayList<String>> entry : this.vulName.entrySet()){
                String tag = entry.getKey();
                ArrayList<String> names = entry.getValue();
                Iterator<String> iterator = names.iterator();
                if (VulChoice.getText().equals(tag)){
                    isVulType = true;
                    List<Future<Object>> futures = new ArrayList<>();
                    ExecutorService executor = Executors.newFixedThreadPool(5);  /**创建线程池，多线程处理并返回结果**/
                    while (iterator.hasNext()){
                        String name = iterator.next();
                        Callable c = new MyCallable(url,tag,name);
                        Future<Object> future = executor.submit(c);
                        futures.add(future);
                    }
                    for (Future<Object> future : futures) {
                        try {
                            PocUtil result = (PocUtil) future.get();
                            if (!result.POC().equals("")){
                                Platform.runLater(() -> VulOut.appendText("目标存在"+result.POC()+"漏洞!\n"));
                            }
                            else {
                                Platform.runLater(() -> VulOut.appendText("目标不存在"+result.POC()+"漏洞!\n"));
                            }
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    executor.shutdown();
                    if (isVulType.equals(true)) break here;
                }
                else {
                    while (iterator.hasNext()) {
                        String name = iterator.next();
                        if (VulChoice.getText().equals(name)){
                            isVulType = true;
                            PocUtil poc = new PocUtil(url, tag, name);
                            if (!poc.POC().equals("")){
                                Platform.runLater(() -> VulOut.appendText("目标存在"+poc.POC()+"漏洞!\n"));
                            }
                            else {
                                Platform.runLater(() -> VulOut.appendText("目标不存在"+poc.POC()+"漏洞!\n"));
                            }
                            if (isVulType.equals(true)) break here;
                        }
                    }
                }
            }
        }


//        if (!url.equals("") && VulChoice.getText()!=null){
//            for(String name : this.vulTypes){
//                List<Future<Object>> futures = new ArrayList<>();
//                if (VulChoice.getText().equals(name)){
//                    String[] FileName = FileUtil.FileList("src/main/java/com/bcvgh/poc/json/"+name);
//                    ExecutorService executor = Executors.newFixedThreadPool(5);  /**创建线程池，多线程处理并返回结果**/
//                    for (int i =0;i < FileName.length;i++){
//                        Callable c = new MyCallable(url,FileName[i],name);
//                        Future<Object> future = executor.submit(c);
//                        futures.add(future);
////                        Future<String> future = executor.submit(new PocThread(url,FileName[i],name));
////                        futures.add(future);
//                    }
//                    executor.shutdown();
//                    for (Future<Object> future : futures) {
//                        try {
//                            PocUtil result = (PocUtil) future.get();
//                            if (!result.equals("")){
//                                Platform.runLater(() -> VulOut.appendText("目标存在"+result+"漏洞!\n"));
//                            }
//                            else {
//                                Platform.runLater(() -> VulOut.appendText("目标不存在"+result+"漏洞!\n"));
//                            }
//                        } catch (InterruptedException | ExecutionException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    executor.shutdown();
//
//                    System.out.println(FileName.toString());
//                }
//            }

            /**old **/
//            for(String name : this.vulName.keySet()){
//                if (VulChoice.getText().equals(name)){
//                    String tag = this.vulName.get(name).getString("tag");
//                    System.out.println("开始扫描"+name);
//                    PocUtil poc = new PocUtil(url, name, tag);
//                    if (!poc.POC().equals("")){
//                        Platform.runLater(() -> VulOut.appendText("目标存在"+poc.POC()+"漏洞!\n"));
//                    }
//                    else {
//                        Platform.runLater(() -> VulOut.appendText("目标不存在"+poc.POC()+"漏洞!\n"));
//                    }
//                }
//            }
        }
//        else {
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("提示");
//            alert.setHeaderText(null);
//            alert.setContentText("请选择漏洞poc并输入目标url");
//            alert.showAndWait();
//        }

//    }
}

class MyCallable implements Callable<Object> {
    private String url;
    private String tag;
    private String name;

    MyCallable(String url , String tag , String name) {
        this.url = url;
        this.tag= tag;
        this.name = name;
    }

    @Override
    public Object call() throws Exception {
        PocUtil poc = new PocUtil(this.url, this.tag ,this.name);
        return poc;
    }
}