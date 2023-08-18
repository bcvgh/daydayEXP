import javafx.application.Application;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class test extends Application {

    @Override

    public void start(Stage stage) {

        ComboBox comboBox2 = new ComboBox();



        ComboBox comboBox = new ComboBox();

        comboBox.getItems().addAll("Country 1", "Country 2", "Country 3");

        comboBox.setOnAction(event -> {

            comboBox2.getItems().clear();

            for (int i = 0; i < 5; i++) {

                comboBox2.getItems().add("State "+i+" "+comboBox.getValue().toString());

            }

        });





        VBox vBox = new VBox();

        vBox.getChildren().addAll(comboBox, comboBox2);



        Scene scene = new Scene(vBox);

        stage = new Stage();

        stage.setScene(scene);

        stage.show();

    }



    public static void main(String[] args) { launch(args); }



}