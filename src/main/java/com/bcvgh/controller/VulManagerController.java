package com.bcvgh.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcvgh.core.poc.PocUsageImp;
import com.bcvgh.utils.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VulManagerController {

    @FXML
    private TextField Address;

    @FXML
    private ProgressBar scanProgress;

    @FXML
    private Button VulScan;

    @FXML
    private MenuBar VulSelect;

    @FXML
    private Menu VulChoice;

    @FXML
    private TextArea VulOut;

    @FXML
    private CheckBox dnsUrlCheck;

    @FXML
    private TextField dnsUrl;

    @FXML
    private Button RenewPoc;


    public HashMap<String , ArrayList<HashMap<String,String>>> pocParse = new HashMap<>();

    public void initialize(){
        this.VulOut.setText("\n\n" +
                "注：该工具存在一些POC验证不严谨导致漏报误报的可能，最好开启代理结合burp食用!\n\n"+
                "1.漏洞列表选择一级列表可对该类型下的所有漏洞检测\n" +
                "2.针对无法根据返回包进行准确判断漏洞是否存在的的poc漏洞，如(反序列化、命令执行\n无回显等)可设置好dnslog地址和api配置,降低误报率和漏报率，也可自行检测dns记录\n" +
                "3.漏洞利用模块暂只支持可RCE漏洞，其他类型漏洞（如未授权、sql注入）可只设置poc\n进行漏洞检测\n" +
                "4.dnslog api暂只支持ceye，其他平台的请求情况需要自己去对应平台验证\n\n"+
                "工具详细使用说明请参考readme.md\n"+
                "\n\n\n\n\n\n\n\n\n\n\n该程序仅用于安全人员本地测试使用！\n" +
                "用户滥用造成的一切后果与作者无关!\n");
        this.VulOut.setEditable(false);
        this.dnsUrl.setEditable(false);
        if (!checkPocDir()){
            PromptUtil.Alert("警告","poc目录创建失败，请检查当前目录权限或自行添加目录。");
        }
        pocParse = PocUtil.PocParse(PocUtil.PocPath+"json"+ File.separator);
        if (pocParse==null){
            this.dnsUrlCheck.getScene().getWindow().hide();
        }
        PocUtil.GetTagName();
        try {
            GenList(pocParse);
        }
        catch (Exception e){
            PromptUtil.Alert("警告","加载poc失败，请检查Poc文件格式！");
        }
        this.Address.setPromptText("例:http://www.baidu.com");
        this.Address.setStyle("-fx-prompt-text-fill: lightgray;");
    }

    /**生成漏洞列表**/
    private void GenList(HashMap<String , ArrayList<HashMap<String,String>>> pocParse){
        VulChoice.getItems().clear();
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
                /**选择一级tag时展示漏洞详情**/
                String[] VulFiles =PocUtil.GetTagVulFiles(PocUtil.tag);
                this.VulOut.setText("目前加载的poc情况:\n");
                for (String VulFile : VulFiles){
                    String VulName ="";
                    VulName =  JSONObject.parseObject(FileUtil.FileRead(PocUtil.PocPath+"json"+File.separator+PocUtil.tag+File.separator+VulFile)).keySet().contains("exp") ? "[+] "+VulFile.split("\\.json")[0]+"\n" : "[-] "+VulFile.split("\\.json")[0] +"(该漏洞暂无EXP)\n";
                    this.VulOut.appendText(VulName);
                }
                /****/
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
    void RenewPoc(ActionEvent event) {
        List<String> files = Arrays.asList(FileUtil.FileList(PocUtil.PocPath));
        ArrayList<String> conf_files = PocUtil.getTags();
        for (String conf_file : conf_files){
            if(!files.contains(conf_file)){
                if (!FileUtil.Mkdir(conf_file)) PromptUtil.Alert("警告","新增"+conf_file+"文件夹失败，请自行添加!");
            }
        }
        pocParse = PocUtil.PocParse(PocUtil.PocPath+"json"+ File.separator);
        PocUtil.GetTagName();
        try {
            GenList(pocParse);
            PromptUtil.Alert("提示","POC更新成功");
        }catch (Exception e){
            PromptUtil.Alert("警告","POC更新失败");
        }
    }

    @FXML
    void NewExploit(ActionEvent event) {
        PocUtil.url = this.Address.getText();
        try{
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
        }catch (Exception e){

        }


    }

    @FXML
    void NewScan(ActionEvent event) {
        if (!this.dnsUrl.getText().isEmpty()){
            PocUtil.dnsUrl = Utils.getRandomString(4)+"."+this.dnsUrl.getText();
            PocUtil.DnsCommand = Utils.getDnsCommand(PocUtil.dnsUrl);
        }
        else{
            PocUtil.DnsCommand = "whoami";
        }
        this.VulOut.setText("");
        String url = Address.getText();
        ArrayList<String> prompt = VulScan(url);
        try {
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    for (String i : prompt){
                        Thread.sleep(200);
                        Platform.runLater(() -> VulOut.appendText(i));
                    }
                    return null;
                }

                @Override
                protected void succeeded(){
                    super.succeeded();
                    PromptUtil.Alert("提示","漏洞检测已完成!");
                }
            };
            Thread thread = new Thread(task);
            thread.setDaemon(true); // 设置为守护线程，以便在应用程序关闭时结束
            thread.start();
        }catch (Exception e){

        }

    }

    @FXML
    void DnsUrlCheck(ActionEvent event) {
        if (this.dnsUrlCheck.isSelected()){
            this.dnsUrl.setEditable(true);
//            PocUtil.dnsUrl= Utils.getRandomString(4)+"."+this.dnsUrl.getText();
        }
        else {
            this.dnsUrl.setText("");
//            PocUtil.dnsUrl = "www.baidu.com";
            this.dnsUrl.setEditable(false);
            PocUtil.dnsUrl="";
            PocUtil.DnsCommand="";
        }
    }

    private ArrayList<String> VulScan(String url){
        Pattern urlPat = Pattern.compile("^(https?://)",Pattern.DOTALL);
        Matcher urlMatch = urlPat.matcher(url);
        PocUsageImp pocPocUsageImp =null;
        try {
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
            }
        }catch (Exception e){
            PromptUtil.Alert("警告","POC文件加载有误,请检测POC文件格式是否正确!");
        }
        return null;
    }

    private Boolean checkPocDir(){
        String path = System.getProperty("user.dir");
        String dirs[] = FileUtil.FileList(path);
        List<String> dirss = Arrays.asList(dirs);
        if (dirss.contains("poc")){
            List<String> pocDirs = Arrays.asList(FileUtil.FileList(path+File.separator+"poc"));
            if (pocDirs.contains("json")){
                return true;
            }else {
                return FileUtil.MkRootDir(path+File.separator+"poc"+File.separator,"json");
            }
        }else {
            if (FileUtil.MkRootDir(path+File.separator,"poc")){
                return FileUtil.MkRootDir(path+File.separator+"poc"+File.separator,"json");
            }
        }
        return false;
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
