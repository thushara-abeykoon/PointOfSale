package com.pos.pointofsale.controller.cashierdashboard;

import javafx.scene.layout.Pane;

public class DashboardStyleConfiguration {

    static void setPaneBackground(Pane pane, String color){
        pane.setStyle("-fx-background-color: "+color);
    }
}
