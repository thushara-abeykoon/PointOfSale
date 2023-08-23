package com.pos.pointofsale.controller;

import com.pos.pointofsale.database.DatabaseConnector;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.sql.*;

public class ControllerCommon {
    public static Connection connection = DatabaseConnector.getInstance().getConnection();
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

    public static String getID(String tableName, String columnName){
        String empId;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select "+columnName+" from "+tableName+" order by "+columnName+" desc limit 1;");

            if (resultSet.next()) {
                empId = resultSet.getString(1);
                String empIdInt = Integer.toString(Integer.parseInt(empId.substring(3))+1);
                if (empId.length()-empIdInt.length()<3)
                    throw new RuntimeException("Too much IDs");
                else
                    return empId.substring(0,empId.length()-empIdInt.length()).concat(empIdInt);
            }
            else
                return "EMP0001";

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
