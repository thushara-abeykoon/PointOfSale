package com.pos.pointofsale;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class AppInitializer extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        Parent parent = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("view/LoginForm.fxml")));
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}