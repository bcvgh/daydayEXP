package com.bcvgh.utils;

import javafx.scene.control.Alert;

public class PromptUtil {
    public static void Alert(String type, String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(type);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}

