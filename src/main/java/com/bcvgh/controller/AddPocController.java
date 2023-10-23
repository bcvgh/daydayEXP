package com.bcvgh.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bcvgh.core.pojo.Exp;
import com.bcvgh.core.pojo.Poc;
import com.bcvgh.core.pojo.PocAll;
import com.bcvgh.utils.FileUtil;
import com.bcvgh.utils.PocUtil;
import com.bcvgh.utils.PromptUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import java.util.*;
import java.util.regex.Matcher;

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
    private TextField id;

    @FXML
    private TextField poc_get;

    @FXML
    private TextArea poc_post;

    @FXML
    private TextField VulName;

    @FXML
    private TextField poc_Pattern;

    @FXML
    private TextArea poc_header;

    @FXML
    private ChoiceBox<String> tag;

    @FXML
    private ComboBox<String> type;

    private Integer StepNum = 1;

    public PocAll pocAll;

    public void initialize(){
        this.type.setItems(FXCollections.observableArrayList(new String[]{"include", "exec","fileread","upload","sql"}));
//        this.tag.setItems(FXCollections.observableList(PocUtil.getTags()));
        this.tag.setItems(FXCollections.observableList(PocUtil.GetTagCNS()));
        this.poc_header.setPromptText("例:\r\n" +
                "Content-Length: 29\r\n" +
                "content-type: application/json\r\n" +
                "Accept-Encoding: */*\r\n" +
                "User-Agent: Mozilla/5.0\r\n" +
                "Connection: close" +
                ""); // 设置默认字符
        this.id.setPromptText("1");
        this.id.setStyle("-fx-prompt-text-fill: lightgray;");
        this.VulName.setPromptText("用友NCcloud uapjs上传命令执行");
        this.VulName.setStyle("-fx-prompt-text-fill: lightgray;");
        this.poc_get.setPromptText("/api/upload");
        this.poc_get.setStyle("-fx-prompt-text-fill: lightgray;");
        this.poc_post.setPromptText("id=1&uid=2");
        this.poc_post.setStyle("-fx-prompt-text-fill: lightgray;");
        this.poc_Pattern.setPromptText("\\d+/\\d+\\.aspx");
        this.poc_Pattern.setStyle("-fx-prompt-text-fill: lightgray;");
    }


    @FXML
    void AddStep(ActionEvent event) {
        GridPane gridPane = new GridPane();
        ColumnConstraints col0 = new ColumnConstraints(56); // 列 1 的宽度为 100
        ColumnConstraints col1 = new ColumnConstraints(100); // 列 2 的宽度为 200
        ColumnConstraints col2 = new ColumnConstraints(200);
        ColumnConstraints col3 = new ColumnConstraints(50);
        RowConstraints row0 = new RowConstraints(30);
        RowConstraints row1 = new RowConstraints(30);
        RowConstraints row2 = new RowConstraints(30);
        RowConstraints row3 = new RowConstraints(30);
        RowConstraints row4 = new RowConstraints();
        gridPane.getColumnConstraints().addAll(col0,col1,col2,col3);
        gridPane.getRowConstraints().addAll(row0,row1,row2,row3,row4);
        Label l_get = new Label("get:");
        Label l_post = new Label("post:");
        Label l_Pattern = new Label("Pattern:");
        Label l_header = new Label("header:");
        TextField t_get = new TextField();
        t_get.setPromptText("/api/upload");
        t_get.setStyle("-fx-prompt-text-fill: lightgray;");
        TextArea t_post = new TextArea();
        t_post.setPromptText("id=1&uid=2");
        t_post.setStyle("-fx-prompt-text-fill: lightgray;");
//        TextField t_post = new TextField();
//        t_post.setPromptText("id=1&uid=2");
        t_post.setStyle("-fx-prompt-text-fill: lightgray;");
        TextField t_Pattern = new TextField();
        t_Pattern.setPromptText("\\d+/\\d+\\.aspx");
        t_Pattern.setStyle("-fx-prompt-text-fill: lightgray;");
        TextArea t_header = new TextArea();
        t_header.setPromptText("例:\r\n" +
                "Content-Length: 29\r\n" +
                "Accept-Encoding: */*\r\n" +
                "User-Agent: Mozilla/5.0\r\n" +
                "Connection: close" +
                "");
        t_header.setStyle("-fx-prompt-text-fill: lightgray;");
        t_header.setPrefHeight(100);
        gridPane.add(l_get,0,0);
        gridPane.add(l_post,0,1);
        gridPane.add(l_Pattern,0,3);
        gridPane.add(l_header,0,4);
        gridPane.add(t_get,1,0);
        gridPane.add(t_post,1,1);
        gridPane.add(t_Pattern,1,3);
        gridPane.add(t_header,1,4);
        GridPane.setRowSpan(t_post,2);
        GridPane.setColumnSpan(t_header,3);
        GridPane.setColumnSpan(t_get,2);
        GridPane.setColumnSpan(t_post,3);
        GridPane.setColumnSpan(t_Pattern,2);
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
        }catch (Exception e){}
    }

    @FXML
    void PocCancel(ActionEvent event) {
        this.PocCancel.getScene().getWindow().hide();
    }

    @FXML
    void PocSave(ActionEvent event) {
        this.tag.setValue(PocUtil.GetTagEn(this.tag.getValue()));
//        this.poc_post.setText(this.poc_post.getText().replaceAll("\n","\r\n"));
//        ArrayList<Exp> expSteps = new ArrayList<>();
        LinkedHashMap<String,Exp> expSteps = new LinkedHashMap<>();
        Integer stepNum = 1;
        try {
            for(TitledPane titledPane : StepAccordion.getPanes()){
                Exp expStep = new Exp();
                Node node = titledPane.getContent();
                AnchorPane anchorPane = (AnchorPane) node;
                for (Node anode : anchorPane.getChildren()){
                    ArrayList arrayList = new ArrayList();
//                Exp expStep = new Exp();
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
                    expStep.setExp_Pattern((String) arrayList.get(2));
                    expStep.setExp_header((String) arrayList.get(3));
                }
                expSteps.put("step"+String.valueOf(stepNum),expStep);
                stepNum = stepNum +1;
            }
            //异常处理
            if (this.type.getValue().isEmpty()) throw new Exception();
            java.util.regex.Pattern ExpPattern = java.util.regex.Pattern.compile(".*\\(.*\\).*" , java.util.regex.Pattern.DOTALL);
            Matcher matcher = ExpPattern.matcher(this.poc_Pattern.getText());
            if (!matcher.find()) throw  new Exception();
            if (this.poc_Pattern.getText().isEmpty()) throw new Exception();
            //
            Poc poc = new Poc(this.poc_get.getText(),this.poc_post.getText(),this.poc_Pattern.getText(),this.poc_header.getText());
            this.pocAll = new PocAll(Integer.parseInt(this.id.getText()),this.VulName.getText(),this.tag.getValue(),this.type.getValue(),expSteps,poc);
            JSONObject exp = PocUtil.Object_toJSon(this.pocAll);
            String expString = JSONObject.toJSONString(exp,SerializerFeature.PrettyFormat);
//            if (!FileUtil.FileWrite(this.pocAll.getVulName()+".json",expString,this.pocAll.getTag())){
//                PromptUtil.Alert("警告","POC写入失败，请检查poc格式!");
//            }
            FileUtil.FileWrite(this.pocAll.getVulName()+".json",expString,this.pocAll.getTag());
            PromptUtil.Alert("提示","POC添加成功，请及时一键更新POC");
            this.PocCancel.getScene().getWindow().hide();

        }catch (Exception e){
            PromptUtil.Alert("警告","poc输入有误");
        }
    }
}
