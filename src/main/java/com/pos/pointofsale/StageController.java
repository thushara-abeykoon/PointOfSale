package com.pos.pointofsale;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class StageController {
    public Stage loadStage(String fxml, String title, AnchorPane root) throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(loadScene(fxml));
        stage.setTitle(title);
        return stage;
    }
    public Stage loadStage(String fxml, String title) throws IOException {
        Stage stage = new Stage();
        stage.setScene(loadScene(fxml));
        stage.setTitle(title);
        return stage;
    }

    private Scene loadScene(String fxml) throws IOException {
        return new Scene(FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource(fxml))));
    }

    public static void closeStage(AnchorPane anchorPane){
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }
}
