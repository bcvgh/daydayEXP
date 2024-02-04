package com.bcvgh.controller;

import com.bcvgh.core.pojo.StringExp;
import com.bcvgh.core.pojo.StringPayload;
import com.bcvgh.core.pojo.StringPoc;
import com.bcvgh.utils.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Stack;
import java.util.regex.Pattern;

public class AddPocController {
    @FXML
    private Button AddStep;

    @FXML
    private Button DeleteStep;

    @FXML
    private Accordion StepAccordion;

    @FXML
    private Button PocCancel;

    @FXML
    private Button PocSave;


    @FXML
    private TextField poc_get;

    @FXML
    private TextArea poc_post;

    @FXML
    private TextField name;

    @FXML
    private TextField poc_pattern;

    @FXML
    private TextArea poc_header;

    @FXML
    private ChoiceBox<String> tag;

    @FXML
    private ComboBox<String> type;

    @FXML
    private TextField poc_status_code;

    private Integer StepNum = 1;

    private static final Logger LOGGER = LogManager.getLogger(AddPocController.class.getName());


    public void initialize(){
        this.type.setItems(FXCollections.observableArrayList(new String[]{"exec","upload","deserialize","other","custom"}));
//        this.tag.setItems(FXCollections.observableList(PocUtil.getTags()));
        this.tag.setItems(FXCollections.observableList(new ArrayList<>(PocUtil.TagCn.values())));
        this.poc_header.setPromptText(Constant.StringHeader); // 设置默认字符
        this.name.setPromptText("用友NCcloud uapjs上传命令执行");
        this.name.setStyle("-fx-prompt-text-fill: lightgray;");
        this.poc_get.setPromptText("/api/upload");
        this.poc_get.setStyle("-fx-prompt-text-fill: lightgray;");
        this.poc_post.setPromptText("id=1&uid=2");
        this.poc_post.setStyle("-fx-prompt-text-fill: lightgray;");
        this.poc_pattern.setPromptText("(\\d+/\\d+\\.aspx)");
        this.poc_pattern.setStyle("-fx-prompt-text-fill: lightgray;");
        this.poc_status_code.setPromptText("200,403");
        this.poc_status_code.setStyle("-fx-prompt-text-fill: lightgray;");

    }


    @FXML
    void AddStep(ActionEvent event) {
        GridPane gridPane = new GridPane();
        gridPane.setPrefWidth(400);
        ColumnConstraints col0 = new ColumnConstraints(100);
        ColumnConstraints col1 = new ColumnConstraints(300);
        ColumnConstraints col2 = new ColumnConstraints();
        RowConstraints row0 = new RowConstraints(30);
        RowConstraints row1 = new RowConstraints(30);
        RowConstraints row2 = new RowConstraints(30);
        RowConstraints row3 = new RowConstraints(30);
        RowConstraints row4 = new RowConstraints(30);
        RowConstraints row5 = new RowConstraints();
        gridPane.getColumnConstraints().addAll(col0,col1,col2);
        gridPane.getRowConstraints().addAll(row0,row1,row2,row3,row4,row5);
        Label l_get = new Label("get:");
        l_get.setMinWidth(80);
        Label l_post = new Label("post:");
        l_post.setMinWidth(80);
        Label l_status_code = new Label("status_code:");
        l_status_code.setMinWidth(80);
        Label l_pattern = new Label("pattern:");
        l_pattern.setMinWidth(80);
        Label l_header = new Label("header:");
        l_header.setMinWidth(80);
        TextField t_get = new TextField();
        t_get.setPromptText("/api/upload");
        t_get.setStyle("-fx-prompt-text-fill: lightgray;");
        TextArea t_post = new TextArea();
        t_post.setPromptText("id=1&uid=2");
        t_post.setStyle("-fx-prompt-text-fill: lightgray;");
//        TextField t_post = new TextField();
//        t_post.setPromptText("id=1&uid=2");
        t_post.setStyle("-fx-prompt-text-fill: lightgray;");
        TextField t_status_code = new TextField();
        t_status_code.setPromptText("200");
        t_status_code.setStyle("-fx-prompt-text-fill: lightgray;");
        TextField t_pattern = new TextField();
        t_pattern.setPromptText("(\\d+/\\d+\\.aspx)");
        t_pattern.setStyle("-fx-prompt-text-fill: lightgray;");
        TextArea t_header = new TextArea();
        t_header.setPromptText(Constant.StringHeader);
        t_header.setStyle("-fx-prompt-text-fill: lightgray;");
        t_header.setPrefHeight(100);
        gridPane.add(l_get,0,0);
        gridPane.add(l_post,0,1);
        gridPane.add(l_status_code,0,3);
        gridPane.add(l_pattern,0,4);
        gridPane.add(l_header,0,5);
        gridPane.add(t_get,1,0);
        gridPane.add(t_post,1,1);
        gridPane.add(t_status_code,1,3);
        gridPane.add(t_pattern,1,4);
        gridPane.add(t_header,1,5);
        GridPane.setRowSpan(t_post,2);
        GridPane.setColumnSpan(t_header,3);
        GridPane.setColumnSpan(t_get,2);
        GridPane.setColumnSpan(t_status_code,2);
        GridPane.setColumnSpan(t_post,3);
        GridPane.setColumnSpan(t_pattern,2);
        gridPane.setPadding(new Insets(0,0,0,15));
        AnchorPane anchorPane = new AnchorPane(gridPane);
        TitledPane titledPane = new TitledPane("Step"+String.valueOf(StepNum),anchorPane);
        StepAccordion.getPanes().add(titledPane);
        StepNum = StepNum+1;
    }

    @FXML
    void DeleteStep(ActionEvent event) {
        Stack<TitledPane> stack = new Stack<>();
        for (TitledPane titledPane : StepAccordion.getPanes()) {
            stack.push(titledPane);
        }
        try{
            StepAccordion.getPanes().remove(stack.pop());
            StepNum = StepNum - 1;
        }catch (Exception e){
            LOGGER.error(e.getMessage()+"(删除step出错!)");
        }
    }

    @FXML
    void PocCancel(ActionEvent event) {
        this.PocCancel.getScene().getWindow().hide();
    }

    @FXML
    void PocSave(ActionEvent event) {
        this.poc_post.setText(this.poc_post.getText().replaceAll("\n","\r\n"));
        LinkedHashMap<String, StringExp> expSteps = new LinkedHashMap<>();
        Integer stepNum = 1;
        try {
            for(TitledPane titledPane : StepAccordion.getPanes()){
                StringExp expStep = new StringExp();
                Node node = titledPane.getContent();
                AnchorPane anchorPane = (AnchorPane) node;
                for (Node anode : anchorPane.getChildren()){
                    ArrayList arrayList = new ArrayList();
                    GridPane gridPane = (GridPane) anode;
                    for(Node textNode : gridPane.getChildren()){
                        if (textNode instanceof TextField){
                            TextField textField = (TextField) textNode;
                            if (textField.getText().isEmpty()) throw new Exception();
                            arrayList.add(textField.getText());
                        }
                        if (textNode instanceof TextArea){
                            TextArea textArea = (TextArea) textNode;
                            arrayList.add(textArea.getText());
                        }
                    }
                    expStep.setExp_get((String) arrayList.get(0));
                    expStep.setExp_post((String) arrayList.get(1));
                    expStep.setExp_status_code((String) arrayList.get(2));
                    expStep.setExp_pattern((String) arrayList.get(3));
                    expStep.setExp_post((String) ((String) arrayList.get(1)).replaceAll("\n","\r\n"));
                    if (!Pattern.compile(".*\\(.*\\).*" , Pattern.DOTALL).matcher(expStep.getExp_pattern()).find()) throw new NewException("pattern格式有误");
                    expStep.setExp_header((String) arrayList.get(4));
                }
                expSteps.put("step"+String.valueOf(stepNum),expStep);
                stepNum = stepNum +1;
            }
            if (this.name.getText().isEmpty()) throw new Exception();
            if (this.tag.getValue().isEmpty()) throw  new Exception();
            if (this.type.getValue().isEmpty()) throw new Exception();
            if (this.poc_pattern.getText().isEmpty()) throw new Exception();
            if (this.poc_status_code.getText().isEmpty()) throw new Exception();
            String tag = OtherUtil.getKey(PocUtil.TagCn,this.tag.getValue());
            if (!Pattern.compile(".*\\(.*\\).*" , Pattern.DOTALL).matcher(this.poc_pattern.getText()).find()) throw new NewException("pattern格式有误");
            StringPayload stringPayload = new StringPayload(this.name.getText(),tag,this.type.getValue(),expSteps,new StringPoc(this.poc_get.getText(),this.poc_post.getText().replaceAll("\\n","\r\n"),this.poc_pattern.getText(),this.poc_header.getText(),this.poc_status_code.getText()));
            if (!FileUtil.FileWrite(Constant.PocPath+tag+ File.separator+this.name.getText()+".json",stringPayload.toString())){
                PromptUtil.Alert("警告","POC写入失败，请检查poc格式!");
            }
            PromptUtil.Alert("提示","POC添加成功，请及时一键更新POC");
            this.PocCancel.getScene().getWindow().hide();
        }catch (NewException e){
            LOGGER.warn(e.getMessage());
        }
        catch (Exception e){
            PromptUtil.Alert("警告","poc输入有误");
            LOGGER.warn("poc输入有误");
        }
    }

    class NewException extends Exception{
        public NewException(String message){
            super(message);
            PromptUtil.Alert("警告",message);
        }
    }
}
