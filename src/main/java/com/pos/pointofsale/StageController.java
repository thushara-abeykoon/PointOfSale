package com.pos.pointofsale;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class StageController {
    public Scene scene;
    public static StageController stageController;

    public Stage loadStage(String fxml, String title, AnchorPane root) throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(loadScene(fxml));
        stage.setTitle(title);
        stage.getIcons().add(new Image(this.getClass().getResource("images/icn.png").openStream()));
        return stage;
    }
    public Stage loadStage(String fxml, String title) throws IOException {
        Stage stage = new Stage();
        stage.setScene(loadScene(fxml));
        stage.setTitle(title);
        stage.getIcons().add(new Image(this.getClass().getResource("images/icn.png").openStream()));
        return stage;
    }

    public Scene loadScene(String fxml) throws IOException {
        scene = new Scene(FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource(fxml))));
        return scene;
    }

    public static void closeStage(AnchorPane anchorPane){
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }
    public static void closeStage(BorderPane borderPane){
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.close();
    }

    public Image getImage(){
        try {
            return new Image(this.getClass().getResource("images/icn.png").openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Image getIcon(){
        return (stageController==null) ? new StageController().getImage():stageController.getImage();
    }

}
