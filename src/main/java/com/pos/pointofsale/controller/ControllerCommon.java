package com.pos.pointofsale.controller;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ControllerCommon {
    private double offset_x;
    private double offset_y;
    public static void lblOnMouseMoved(Label lbl){
        lbl.setUnderline(true);
    }
    public static void lblOnMouseExited(Label lbl){
        lbl.setUnderline(false);
    }

    public void dragPane(Pane pane, AnchorPane anchorPane){
        pane.setOnMousePressed(event -> {
            offset_x = event.getSceneX();
            offset_y = event.getSceneY();
        });

        pane.setOnMouseDragged(event -> {
            anchorPane.getScene().getWindow().setX(event.getScreenX() - offset_x);
            anchorPane.getScene().getWindow().setY(event.getScreenY() - offset_y);
        });
    }


}
