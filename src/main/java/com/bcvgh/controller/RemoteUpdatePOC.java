package com.bcvgh.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bcvgh.core.RemoteUpdatePoc;
import com.bcvgh.utils.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


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

    private static final Logger LOGGER = LogManager.getLogger(RemoteUpdatePOC.class.getName());

    public static  AtomicReference<Double> progress = new AtomicReference<>(new Double("0.00"));

    public void initialize(){
        Tips.setWrapText(true);
        this.RemotePOCURL.setText("https://github.com/bcvgh/daydayExp-pocs/");
        this.RemotePOCURL.setStyle("-fx-prompt-text-fill: lightgray;");
        this.PocURL = this.RemotePOCURL.getText();
    }

    private void updateBoard(String text,Double schedule){
        Platform.runLater(()->{
            downloadInfo.setText("正在下载config.json文件！");
            progress.set(0.10);
            downloadProgress.setProgress(this.progress.get());
        });
    }

    @FXML
    void UpdateCancel(ActionEvent event) {
        this.UpdateCancel.getScene().getWindow().hide();
    }

    @FXML
    void UpdatePOC(ActionEvent event) {
        String pocUrl = this.RemotePOCURL.getText();
        this.UpdatePOC.setDisable(true);
        this.UpdateCancel.setDisable(true);
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> {
                    downloadInfo.setText("正在连接仓库！");
                    progress.set(0.0);
                    downloadProgress.setProgress(progress.get());
                });
                RemoteUpdatePoc remoteUpdatePoc = new RemoteUpdatePoc(pocUrl);
                if (remoteUpdatePoc.type == null) {
                    Platform.runLater(() -> {
                        downloadInfo.setText("仓库地址连接失败！");
                        progress.set(0.0);
                        downloadProgress.setProgress(progress.get());
                    });
                    return null;
                }
                Platform.runLater(() -> {
                    downloadInfo.setText("正在更新配置文件!");
                    progress.set(0.2);
                    downloadProgress.setProgress(progress.get());
                });
                if (remoteUpdatePoc.getPocConfig() == null) {
                    Platform.runLater(() -> {
                        downloadInfo.setText("配置文件更新失败！");
                        progress.set(0.1);
                        downloadProgress.setProgress(progress.get());
                    });
                    return null;
                }
                Platform.runLater(() -> {
                    downloadInfo.setText("正在更新POC目录!");
                    progress.set(0.2);
                    downloadProgress.setProgress(progress.get());
                });
                if (!remoteUpdatePoc.MkNewDir()) {
                    Platform.runLater(() -> {
                        downloadInfo.setText("更新POC目录失败!");
                        progress.set(0.2);
                        downloadProgress.setProgress(progress.get());
                    });
                    return null;
                }
                Platform.runLater(() -> {
                    downloadInfo.setText("正在更新POC!");
                    progress.set(0.3);
                    downloadProgress.setProgress(progress.get());
                });
                ExecutorService executor = Executors.newFixedThreadPool(5);
                List<String> dirs = Arrays.asList(FileUtil.DirList(Constant.PocPath)).stream().filter(s -> !s.equals("config.json")).collect(Collectors.toList());
                try {
                    for (String dir : dirs) {
                        List<String> pocs = remoteUpdatePoc.getPocs(dir);
                        for (String poc : pocs) {
                            executor.submit(() -> {
                                final double currentProgress = RemoteUpdatePOC.progress.get() + 0.01;
                                RemoteUpdatePOC.progress.set(currentProgress);
                                Platform.runLater(() -> {
                                    RemoteUpdatePOC.progress.set(currentProgress);
                                    downloadInfo.setText("正在下载" + EncodeUtil.UrlDecode(poc) + "!");
                                    downloadProgress.setProgress(progress.get());
                                });
                                String content = remoteUpdatePoc.getContent(dir, poc);
                                FileUtil.FileWrite(Constant.PocPath + dir + File.separator + EncodeUtil.UrlDecode(poc), content);
                            });
                        }
                    }
                    executor.shutdown();
                    try {
                        if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {

                            LOGGER.warn("线程池未在指定时间内完成所有任务!");
                        }
                    } catch (InterruptedException e) {
                        LOGGER.error(e.getMessage()+"(多线程任务失败!)");
                    }
                }catch (Exception e){
                    Platform.runLater(() -> {
                    downloadInfo.setText("下载出错了!");
                        progress.set(0.0);
                        downloadProgress.setProgress(progress.get());
                    });
                }
                Platform.runLater(() -> {
                    downloadInfo.setText("下载已完成!");
                    progress.set(1.0);
                    downloadProgress.setProgress(progress.get());
                });
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                PromptUtil.Alert("提示","结束!");
                UpdatePOC.setDisable(false);
                UpdateCancel.setDisable(false);
            }
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}
