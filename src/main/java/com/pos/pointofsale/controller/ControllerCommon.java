package com.pos.pointofsale.controller;

import com.pos.pointofsale.database.DatabaseConnector;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

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

    public static void icnCloseOnMouseExited(MaterialIconView icn){
        icn.setFill(Paint.valueOf("#d0d0d0"));
    }
    public static void icnCloseOnMouseEntered(MaterialIconView icn){
        icn.setFill(Paint.valueOf("#ff2700"));
    }

    public static String getID(String tableName, String columnName,String firstLetters, String initialLetters){
        if (firstLetters.length()!=3)
            throw new RuntimeException("Wrong letter input!");

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
                return firstLetters+initialLetters;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getMacAddress(){
        try{
            InetAddress localhost = InetAddress.getLocalHost();
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localhost);
            byte [] macAddressBytes = networkInterface.getHardwareAddress();

            StringBuilder macAddress = new StringBuilder();

            for (byte macAddressByte : macAddressBytes) {
                macAddress.append(String.format("%02X", macAddressByte));
            }
            return macAddress.toString();
        }catch(UnknownHostException |SocketException e){
            e.printStackTrace();
        }
        return "";
    }
    public static void textFieldFilter(TextField textField){
        keyFilter(textField,"0123456789.");
    }

    public static void keyFilter(TextField textField, String allowedCharacters){
        textField.addEventFilter(KeyEvent.KEY_TYPED,keyEvent -> {
            String character = keyEvent.getCharacter();
            if (!allowedCharacters.contains(character))
                keyEvent.consume();
        });
    }
    public static String getCorrectPriceFormat(double total) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String totalValueString = decimalFormat.format(total);
        if (!totalValueString.contains("."))
            return totalValueString + ".00";
        else if (totalValueString.substring(totalValueString.length() - 2).contains(".")) {
            return totalValueString + "0";
        } else {
            return totalValueString;
        }
    }
}
