package com.bcvgh.controller;

import com.alibaba.fastjson.JSONObject;
import com.bcvgh.core.UnserPayload;
import com.bcvgh.core.exploit.poc.PocUsageImp;
import com.bcvgh.core.pojo.Input;
import com.bcvgh.core.pojo.Payload;
import com.bcvgh.core.unser.core.pojo.UnSerInput;
import com.bcvgh.core.unser.gadgets.ObjectPayload;
import com.bcvgh.utils.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;

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

    @FXML
    private Button MutiScan;

    @FXML
    private TextField threadNum;

    @FXML
    private Button stopScan;

    @FXML
    private Button ResultOutPut;

    private volatile boolean stopRequested = false;

    private String tag;

    private String name;

    private String type;

    private static final Logger LOGGER = LogManager.getLogger(VulManagerController.class.getName());


    public void initialize(){
        if (!this.InitDir(Constant.LogPath)){
            LOGGER.error("log目录初始化失败!");
            return;
        }
        LOGGER.info("应用初始化开始!");
        if (this.InitDir(Constant.PocPath)){
            if (!Arrays.asList(FileUtil.DirList(Constant.PocPath)).contains("config.json")){
                if (!FileUtil.FileWrite(Constant.ConfigPath,Constant.ConfigContent)){
                    LOGGER.error("poc目录初始化失败!");
                    return;
                }
            }
        }
        try {
            PocUtil.tag_vul = PocUtil.getTagVul();
            PocUtil.TagCn = PocUtil.getTagCn();
            Boolean isGenList =  GenList(PocUtil.tag_vul);
            if (isGenList==false){
                LOGGER.error("初始化失败!");
                this.dnsUrlCheck.getScene().getWindow().hide();
            }
        }catch (Exception e){
            LOGGER.error(e);
            this.dnsUrlCheck.getScene().getWindow().hide();
            return;
        }
        this.threadNum.setText("5");



        this.VulOut.setText("\n\n" +
                "注：该工具存在一些POC验证不严谨导致漏报误报的可能，最好开启代理结合burp食用!\n\n"+
                "1.漏洞列表选择一级列表可对该类型下的所有漏洞检测\n" +
                "2.针对无法根据返回包进行准确判断漏洞是否存在的的poc漏洞，如(反序列化、命令执行\n无回显等)可设置好dnslog地址和api配置,降低误报率和漏报率，也可自行检测dns平台记录\n" +
                "3.漏洞利用模块暂只支持可RCE漏洞，其他类型漏洞（如未授权、sql注入）可只设置poc\n进行漏洞检测\n" +
                "工具详细使用说明请参考readme.md\n"+
                "\n\n\n\n\n\n\n\n\n\n\n该程序仅用于安全人员本地测试使用！\n" +
                "用户滥用造成的一切后果与作者无关!\n");
        this.VulOut.setEditable(false);
        this.dnsUrl.setEditable(false);
        this.Address.setPromptText("例:http://www.baidu.com");
        this.Address.setStyle("-fx-prompt-text-fill: lightgray;");
    }



    @FXML
    void RenewPoc(ActionEvent event) {
        try {
            PocUtil.tag_vul = PocUtil.getTagVul();
            PocUtil.TagCn = PocUtil.getTagCn();
            if (GenList(PocUtil.tag_vul)){
                PromptUtil.Alert("提示","POC更新成功");
            }else {
                PromptUtil.Alert("警告","POC更新失败");
                LOGGER.error("初始化失败!");
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            PromptUtil.Alert("警告","POC更新失败");
        }
    }

    @FXML
    void NewExploit(ActionEvent event) {
        String url = this.Address.getText();
        if (!OtherUtil.checkUrl(url)){
            PromptUtil.Alert("提示","请输入正确的地址!(以http://或https://开头)");
            return;
        }
        try{
            JSONObject PocJson = PocUtil.getPocJSon(this.tag,this.name);
            if (PocJson.keySet().contains("exp")){
                this.type = PocJson.getString("type");
                if (url!=null && !this.name.equals("")){
                    Stage newTargetStage = new Stage();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bcvgh/VulExploit.fxml"));
                    Parent root = null;
                    try {
                        VulExploitController  vulExploitController = new VulExploitController(url,this.tag,this.name,this.type);
                        loader.setController(vulExploitController);
                        root = loader.load();
                    } catch (IOException e) {
                       LOGGER.error(e.getMessage()+"(exploit场景初始化失败!)");
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
        this.VulOut.setText("");
        String url = this.Address.getText();
        if (!OtherUtil.checkUrl(url)){
            PromptUtil.Alert("提示","请输入正确的地址!(以http://或https://开头)");
            return;
        }
        Scan(url);
    }

    @FXML
    void MutiScan(ActionEvent event) {
        stopRequested = false;
        this.VulOut.setText("");
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("text (*.txt)","*.txt");
        fileChooser.setTitle("选择文件");
        Stage selectFile = new Stage();
        fileChooser.setInitialDirectory(new File("."));
        File file = fileChooser.showOpenDialog(selectFile);
        if (file!=null){
            String[] urls = FileUtil.FileRead(file.getAbsolutePath(),"").split("\n");
            for (String url :urls){
                if (!url.contains("http")){
                    PromptUtil.Alert("警告","URL请以http://或https://开头!");
                    return;
                }
            }
            Scan(urls);
        }
    }

    private Input InitScanInput(String url){
        String random = OtherUtil.getRandomString(4);
        Input input = new Input(null,null,null,null,url,random,null);
        if (!this.dnsUrl.getText().isEmpty()){
            String dnsUrl = random+"."+this.dnsUrl.getText();
            byte[] serialBytes = UnserPayload.GenSerial(new UnSerInput("URLDNS","other",null,null,dnsUrl,null,null,null,null));
            input.setDnslog(dnsUrl);
            input.setSerialization(serialBytes);
        }
        else{
            input.setDnslog("127.0.0.1");
            input.setSerialization(new byte[1]);
        }
        return input;
    }

    private <T> void Scan(T url){
        this.ResultOutPut.setDisable(true);
        if (!this.threadNum.getText().matches("^\\d{1,2}$")){
            PromptUtil.Alert("警告","请输入2位数以内的数字!");
            return;
        }
        Constant.ThreadNum = Integer.valueOf(this.threadNum.getText());
        if (this.tag==null){
            PromptUtil.Alert("警告","请选择漏洞类型!");
            return;
        }
        try {
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    if (url instanceof String){
                        Input input = InitScanInput((String) url);
                        PocUsageImp pocPocUsageImp = new PocUsageImp((String) url,tag,name,input,VulOut);
                    }else if (url instanceof String[]){
                        ExecutorService executor = Executors.newFixedThreadPool(Constant.ThreadNum);
                        for (String u :(String[]) url){
                            executor.submit(() -> {
                                if (!stopRequested){
                                    Input input = InitScanInput(u);
                                    PocUsageImp pocPocUsageImp = new PocUsageImp(u,tag,name,input,VulOut);
                                }
                            });
                        }
                        executor.shutdown();
                        try {
                            if (!executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS)) {
                                LOGGER.warn("线程池未在指定时间内完成所有任务!");
                            }
                        } catch (InterruptedException e) {
                            LOGGER.error(e.getMessage()+"(多线程任务失败!)");
                        }
                    }
                    return null;
                }

                @Override
                protected void succeeded(){
                    super.succeeded();
                    ResultOutPut.setDisable(false);
                    PromptUtil.Alert("提示","漏洞检测已完成!");
                }
            };
            Thread thread = new Thread(task);
            thread.setDaemon(true); // 设置为守护线程，以便在应用程序关闭时结束
            thread.start();
        }catch (Exception e){
            LOGGER.error(e.getMessage()+"(poc scan异常!)");
        }
    }

    @FXML
    void DnsUrlCheck(ActionEvent event) {
        if (this.dnsUrlCheck.isSelected()){
            this.dnsUrl.setEditable(true);
        }
        else {
            this.dnsUrl.setText("");
            this.dnsUrl.setEditable(false);
        }
    }



    private Boolean InitDir(String path){
        String[] dirsArray = FileUtil.DirList(path);
        if (dirsArray==null){
            return FileUtil.Mkdir(path,"");
        }
        return true;
    }

    private Boolean GenList(HashMap<String , ArrayList<String>> tag_vul){
        VulChoice.getItems().clear();
        try {
            for (String tag : tag_vul.keySet()) {
                String tag_name = PocUtil.TagCn.get(tag)==null? tag : PocUtil.TagCn.get(tag);
                Menu VulTag = new Menu(tag_name);
                VulTag.setOnAction(event -> {
                    this.tag = tag;
                    this.name =null;
                    VulChoice.setText(VulTag.getText());
                    //选择一级tag时展示漏洞详情
                    this.VulOut.setText("目前加载的poc情况:\n");
                    for (String VulFile : tag_vul.get(tag)){
                        String VulName ="";
                        VulName =  JSONObject.parseObject(FileUtil.FileRead(Constant.PocPath+tag+File.separator+VulFile+".json","")).keySet().contains("exp") ? "[+] "+VulFile.split("\\.json")[0]+"\n" : "[-] "+VulFile.split("\\.json")[0] +"(该漏洞暂无EXP)\n";
                        this.VulOut.appendText(VulName);
                    }
                    VulChoice.hide();
                });
                for (String vul:tag_vul.get(tag)) {
                    MenuItem subVn = new MenuItem(vul);
                    subVn.setOnAction(event -> {
                        this.tag =tag;
                        this.name = vul;
                        VulChoice.setText(subVn.getText());
                    });
                    VulTag.getItems().add(subVn);
                }
                VulChoice.getItems().add(VulTag);
            }
            return true;
        }catch (Exception e)
        {
            LOGGER.error(e.getMessage()+"(poc列表生成失败!)");
            return false;
        }
    }

    @FXML
    void stopScan(ActionEvent event) {
        try {
            stopRequested = true;
            PromptUtil.Alert("提示","任务已中断!");
        }catch (Exception e){
            LOGGER.error(e.getMessage()+"(任务中断异常)");
        }
    }

    @FXML
    void ResultOutPut(ActionEvent event) {
        String res = this.VulOut.getText();
        HashMap<String,ArrayList<String>> vul_urls = this.getVulUrl(res);
        if (!vul_urls.isEmpty()){
            Workbook workbook = this.genXlSX(vul_urls);
            if (this.xlsxExport(workbook)){
                PromptUtil.Alert("提示","导出成功!");
            }
        }else {
            PromptUtil.Alert("提示","无可导出项目!");
        }
    }

    private HashMap<String,ArrayList<String>> getVulUrl(String res){
        HashMap<String,ArrayList<String>> vul_urls = new HashMap<>();
        List<String> results = Arrays.asList(res.split("\n"));
        String regex = "[+].*存在(.*)漏洞!请";
        Pattern pattern = Pattern.compile(regex);
        String regex1 = "[+].\\s(.*)存在";
        Pattern pattern1 = Pattern.compile(regex1);
        for (String result : results){
            Matcher matcher = pattern.matcher(result);
            if (matcher.find()){
                if (!vul_urls.keySet().contains(matcher.group(1))){
                    vul_urls.put(matcher.group(1),new ArrayList<>());
                }
                Matcher matcher1 = pattern1.matcher(result);
                if (matcher1.find()){
                    vul_urls.get(matcher.group(1)).add(matcher1.group(1));
                }
            }
        }
        return vul_urls;
    }

    private Workbook genXlSX(HashMap<String,ArrayList<String>> map){
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("扫描情况");
        int columnIndex = 0;
        for (String vul : map.keySet()) {
            XSSFRow headerRow = sheet.getRow(0);
            if (headerRow == null) {
                headerRow = sheet.createRow(0);
            }
            Cell headerCell = headerRow.createCell(columnIndex);
            headerCell.setCellValue(vul);
            int desiredColumnWidth = 30;
            sheet.setColumnWidth(columnIndex, desiredColumnWidth * 256);
            int rowIndex = 0;

            for (String url : map.get(vul)) {
                rowIndex++;

                XSSFRow dataRow = sheet.getRow(rowIndex);
                if (dataRow == null) {
                    dataRow = sheet.createRow(rowIndex);
                }

                Cell dataCell = dataRow.createCell(columnIndex);
                dataCell.setCellValue(url);
            }

            columnIndex++;
        }
        return workbook;
    }

    private Boolean xlsxExport(Workbook workbook){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("导出文件");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("xlsx (*.xlsx)","*.xlsx");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File file = fileChooser.showSaveDialog(null);
        if (file !=null){
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                workbook.write(fileOutputStream);
                fileOutputStream.close();
                return true;
            } catch (FileNotFoundException e) {
                LOGGER.error(e.getMessage());
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
        return false;
    }


}
