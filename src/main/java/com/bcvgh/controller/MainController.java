package com.bcvgh.controller;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import com.bcvgh.util.InitUtil;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainController {
    @FXML
    private Tab VulManager;

//    @FXML
//    private VBox vBox;
//
//    @FXML
//    private ChoiceBox<String> VulChoice;

//    public void initialize(){
//        initPane();
//        initPoc();
//
//    }
//
//    private void initPane() {
//        String[] vulName ={"---请选择漏洞类型---","泛微","致远","用友"};
//        VulChoice.getSelectionModel().select(vulName[0]);
//        VulChoice.getItems().addAll(vulName);
//        VulChoice.setOnAction(e->{
//            System.out.println(123);
//            Stage a = new Stage();
//            try {
//                Stage newTargetStage = new Stage();
//                Parent root = FXMLLoader.load(getClass().getResource("/com/bcvgh/VulExploit.fxml"));
//                newTargetStage.setTitle("-添加任务-");
//
//                Scene scene = new Scene(root);
//        JMetro jMetro = new JMetro(Style.LIGHT);
//        jMetro.setScene(scene);
//        scene.getStylesheets().add(
//                getClass().getResource("/css/win7glass.css")
//                        .toExternalForm());


                //scene.getStylesheets().add(MainScene.class.getResource("/css/jfoenix-components.css").toExternalForm());
////                final MenuButton choices = new MenuButton("Obst");
////                final List<CheckMenuItem> items = Arrays.asList(
////                        new CheckMenuItem("Apfel"),
////                        new CheckMenuItem("Banane"),
////                        new CheckMenuItem("Birne"),
////                        new CheckMenuItem("Kiwi")
////                );
////                choices.getItems().addAll(items);
////                ListView<String> selectedItems = new ListView<>();
////                for (final CheckMenuItem item : items) {
////                    item.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
////                        if (newValue) {
////                            selectedItems.getItems().add(item.getText());
////                        } else {
////                            selectedItems.getItems().remove(item.getText());
////                        }
////                    });
////                }
////                BorderPane borderPane = new BorderPane();
////                borderPane.setTop(choices);
////                borderPane.setCenter(selectedItems);
////
////
////                newTargetStage.setScene(scene);
////                newTargetStage.show();
////            } catch (IOException ioException) {
////                ioException.printStackTrace();
//            }


//        });
//    }

//    private void initPoc() {
//
//    }
//
//    @FXML
//    void showAllVul(ActionEvent event){
//        System.out.println("123");
//    }


}
