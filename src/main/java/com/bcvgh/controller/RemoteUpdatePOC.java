package com.bcvgh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bcvgh.utils.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;


public class RemoteUpdatePOC {

    @FXML
    private Label downloadInfo;

    @FXML
    private ProgressBar downloadProgress;

    @FXML
    private Button UpdateCancel;

    @FXML
    private Button UpdatePOC;

    @FXML
    private TextField RemotePOCURL;

    @FXML
    private Label Tips;

    private String PocURL;

//    private HashMap<String,Object> headers = new HashMap<>(PocUtil.PojoHeader_toJson(PocUtil.defaultHeader));

    public static  AtomicReference<Double> progress = new AtomicReference<>(new Double("0.00"));

    public void initialize(){
        Tips.setWrapText(true);
        this.RemotePOCURL.setText("https://github.com/bcvgh/daydayExp-pocs/");
        this.RemotePOCURL.setStyle("-fx-prompt-text-fill: lightgray;");
        this.PocURL = this.RemotePOCURL.getText();
    }

    @FXML
    void UpdateCancel(ActionEvent event) {
        this.UpdateCancel.getScene().getWindow().hide();
    }

    @FXML
    void UpdatePOC(ActionEvent event) {
//        this.PocURL = this.RemotePOCURL.getText();
//        this.PocURL = this.RemotePOCURL.getText();
        String pocUrl = this.RemotePOCURL.getText();
        this.UpdatePOC.setDisable(true);
        this.UpdateCancel.setDisable(true);
        try {
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    Response response = HttpTools.get(pocUrl,UpdatePoc.headers,"UTF-8");
                    String type ="";
                    if (pocUrl.contains("github")){
                        type = "github";
                    }
                    else {
                        type = "customize";
                    }
                    if (response.getCode()==200){
                        if (UpdatePoc.getPocConfig(pocUrl)){
                            Platform.runLater(()->{
                                downloadInfo.setText("正在下载config.json文件！");
                                progress.set(0.10);
                                downloadProgress.setProgress(progress.get());
                            });
                        }
                        Platform.runLater(()->{
                            downloadInfo.setText("正在新建poc目录！");
                            progress.set(0.20);
                            downloadProgress.setProgress(progress.get());
                        });
                        MkNewDir(pocUrl,type);
                        List<String> localDirs = Arrays.asList(getLocalDirs());
                        ExecutorService executor = Executors.newFixedThreadPool(5);
                        for (String localDir : localDirs){
//                   List<String> localPocs = Arrays.asList(getLocalPocs(localDir));
                            if (type.equals("github")){
                                JSONArray jsonArray = UpdatePoc.getPocsgit(pocUrl,localDir);
//                                ExecutorService executor = Executors.newFixedThreadPool(5);
                                for (int i = 0 ;i < jsonArray.size();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String poc = String.valueOf(jsonObject.get("name"));
                                    Runnable runningTask = () -> {
                                        String pocContent = UpdatePoc.getPocContentgit(pocUrl,localDir,poc);
                                        FileUtil.FileWrite(poc,pocContent,localDir);
                                        final double currentProgress = RemoteUpdatePOC.progress.get()+0.01;
                                        RemoteUpdatePOC.progress.set(currentProgress);
                                        Platform.runLater(() -> {
                                            RemoteUpdatePOC.progress.set(currentProgress);
                                            downloadInfo.setText(poc+"正在下载！");
                                            downloadProgress.setProgress(currentProgress);
                                        });
                                    };
                                    executor.submit(runningTask);
                                }
                            }
                            else {
                                List<String> pocs = UpdatePoc.getPocs(pocUrl,localDir);
                                for(String poc : pocs){
                                    Runnable runningTask = () -> {
                                        String pocContent = UpdatePoc.getPocContent(pocUrl,localDir,poc);

                                        FileUtil.FileWrite(URLDecoder.decode(poc),pocContent,localDir);
                                        final double currentProgress = RemoteUpdatePOC.progress.get()+0.01;
                                        RemoteUpdatePOC.progress.set(currentProgress);
                                        Platform.runLater(() -> {
                                            RemoteUpdatePOC.progress.set(currentProgress);
                                            downloadInfo.setText(URLDecoder.decode(poc) +"正在下载！");
                                            downloadProgress.setProgress(currentProgress);
                                        });
                                    };
                                    executor.submit(runningTask);
                                }
                            }
                        }
                        executor.shutdown();
                        try {
                            // 等待所有任务执行完成或者超时
                            while (!executor.awaitTermination(1, java.util.concurrent.TimeUnit.SECONDS)) {
                                System.out.println("Waiting...");
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                            Platform.runLater(() -> {
//                                progress.set(1.00);
//                                downloadInfo.setText("poc更新完成！");
//                                downloadProgress.setProgress(progress.get());
//                            });
                        Platform.runLater(()->PromptUtil.Alert("提示","远程更新poc仓库成功，请手动更新poc！"));
                        Platform.runLater(() -> {
                            downloadInfo.setText("poc更新完成！");
                            downloadProgress.setProgress(1.00);
                        });
                    }
                    else {
                        Platform.runLater(()->PromptUtil.Alert("警告","连接超时，仓库地址无法访问，请检查网络（代理）情况"));
                    }

                    return null;
                }

                @Override
                protected void succeeded(){
                    super.succeeded();
                    UpdatePOC.setDisable(false);
                    UpdateCancel.setDisable(false);

//                    UpdateCancel.getScene().getWindow().hide();
                }
            };
            Thread thread = new Thread(task);
            thread.setDaemon(true); // 设置为守护线程，以便在应用程序关闭时结束
            thread.start();
        }catch (Exception e){

        }

//        if (response.getCode()!=200){
//            PromptUtil.Alert("警告","连接超时，仓库无法访问！");
//            return;
//        }
//        if (this.PocURL.contains("github.com")){
//            Response response = HttpTools.get(this.PocURL,headers,"Utf-8");
//            if (response.getCode()==200){
//               MkNewDir();
//               List<String> localDirs = Arrays.asList(getLocalDirs());
//               for (String localDir : localDirs){
////                   List<String> localPocs = Arrays.asList(getLocalPocs(localDir));
//                   JSONArray jsonArray = getPocs(this.PocURL,localDir);
//                   for (int i = 0 ;i < jsonArray.size();i++){
//                       JSONObject jsonObject = jsonArray.getJSONObject(i);
//                       String poc = String.valueOf(jsonObject.get("name"));
//                       String pocContent = getPocContent(this.PocURL,localDir,poc);
//                       FileUtil.FileWrite(poc,pocContent,localDir);
//                       this.downloadInfo.setText(poc+"正在下载！");
//                       progress = progress +0.1;
//                       downloadProgress.setProgress(progress);
//                   }
//               }
//               PromptUtil.Alert("提示","远程更新poc仓库成功，请手动更新poc！");
//            }
//            else {
//                PromptUtil.Alert("警告","连接超时，仓库地址无法访问，请检查网络（代理）情况");
//            }
//        }
//        else {
//
//        }

    }

//    private List<String> getDirs(String pocURL){
//        ArrayList<String> dirs = new ArrayList<>();
//        String dirsUrl = pocURL+"/tree/main/";
//        Response response = HttpTools.get(dirsUrl,this.headers,"UTF-8");
//        String regex = "/tree/main/[0-9a-zA-Z]*\">([0-9a-zA-Z]*)<";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(response.getText());
//        while (matcher.find()){
//            dirs.add(matcher.group(1));
//        }
//        if (!dirs.isEmpty()){
//            return dirs;
//        }else {
//            PromptUtil.Alert("警告","连接出错啦，请确认github仓库地址是否正确！");
//            return null;
//        }
//    }
//
//    private JSONArray getPocs(String pocURL, String dirname){
//        String PocsUrl = pocURL+"/tree/main/"+dirname;
//        Response response = HttpTools.get(PocsUrl,this.headers,"UTF-8");
//        String regex = "<script type=\"application/json\".*data-target=\"react-app.embeddedData\">(.*)</script>";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(response.getText());
//        if (matcher.find()){
//            JSONObject json = JSON.parseObject(matcher.group(1));
//            JSONArray jsonArray = json.getJSONObject("payload").getJSONObject("tree").getJSONArray("items");
//            return jsonArray;
//        }
//        else {
//            PromptUtil.Alert("警告","连接出错啦，请确认github仓库地址是否正确！");
//            return null;
//        }
//    }

    private String[] getLocalDirs(){
        String[] dirs = FileUtil.FileList(PocUtil.PocPath+File.separator+"json"+File.separator);
        return  dirs;
    }

    private String[] getLocalPocs(String dir){
        String[] pocs = FileUtil.FileList(PocUtil.PocPath+File.separator+"json"+File.separator+dir+File.separator);
        return pocs;
    }

//    private String getPocContent(String pocURL,String dir,String dirname) {
//        String pocUrl = "";
//        try {
//             pocUrl = pocURL + "blob/main/" + dir + "/" + URLEncoder.encode(dirname, "UTF-8").replaceAll("\\+", "%20");
//        } catch (UnsupportedEncodingException e) {
//            PromptUtil.Alert("警告","出错啦");
//        }
//        Response response1 = HttpTools.get(pocUrl,this.headers, "UTF-8");
//        String regex1 = "<script type=\"application/json\".*data-target=\"react-app.embeddedData\">(.*)</script>";
//        Pattern pattern1 = Pattern.compile(regex1);
//        Matcher matcher1 = pattern1.matcher(response1.getText());
//        if (matcher1.find()) {
//            String pocs = "";
//            JSONObject jsonObject = JSONObject.parseObject(matcher1.group(1));
//            JSONArray jsonArray1 = jsonObject.getJSONObject("payload").getJSONObject("blob").getJSONArray("rawLines");
//            for (int s = 0; s < jsonArray1.size(); s++) {
//                String poc = jsonArray1.getString(s);
//                pocs = pocs + poc;
//            }
//            return pocs;
//        }
//        return null;
//    }

    private Boolean MkNewDir(String pocURL,String type){
        List<String> localDirs = Arrays.asList(getLocalDirs());
        List<String> Dirs = new ArrayList<>();
        if (type.equals("github")){
            Dirs = UpdatePoc.getDirs(pocURL,"github");
        }
        else {
            Dirs = UpdatePoc.getDirs(pocURL,"customize");
        }
        try {
            Dirs.removeAll(localDirs);
            for (String Dir : Dirs){
                FileUtil.Mkdir(Dir);
            }
        }catch (Exception e) {return false;}
        return true;
    }

//    private void Mkdir(){
//        List<String> localDirs = Arrays
//    }

}
