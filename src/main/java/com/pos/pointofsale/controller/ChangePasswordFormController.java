package com.pos.pointofsale.controller;

import com.pos.pointofsale.StageController;
import com.pos.pointofsale.controller.registerformcontroller.RegisterValidation;
import com.pos.pointofsale.database.DatabaseConnector;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChangePasswordFormController {
    public static String empId;
    public PasswordField txtOldPassword;
    public PasswordField txtNewPassword;
    public PasswordField txtConfirmNewPassword;
    private final Connection connection = DatabaseConnector.getInstance().getConnection();
    public AnchorPane root;

    public void btnChangeOnAction() {
        changePassword();
    }

    public void txtConfirmNewPasswordOnAction() {
        changePassword();
    }

    private void changePassword(){
        if (!isOldPasswordValid()){
            Alert alert = new Alert(Alert.AlertType.ERROR,"Invalid Old Password", ButtonType.OK);
            alert.showAndWait();
            txtOldPassword.clear();
            txtOldPassword.requestFocus();
        }
        else if (RegisterValidation.invalidPassword(txtNewPassword.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR,"Invalid Password Format",ButtonType.OK);
            alert.showAndWait();
            txtNewPassword.clear();
            txtNewPassword.requestFocus();
        }
        else if (!txtNewPassword.getText().equals(txtConfirmNewPassword.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR,"Password does not matches!",ButtonType.OK);
            alert.showAndWait();
            txtNewPassword.clear();
            txtConfirmNewPassword.clear();
            txtNewPassword.requestFocus();
        }
        else {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE employee SET password = MD5(?) WHERE emp_id=?");
                preparedStatement.setObject(1,txtNewPassword.getText());
                preparedStatement.setObject(2,empId);
                int i = preparedStatement.executeUpdate();
                if (i>0){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Success!",ButtonType.OK);
                    alert.showAndWait();
                    StageController.closeStage(root);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private boolean isOldPasswordValid(){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT password FROM employee WHERE emp_id = ? AND password=MD5(?)");
            preparedStatement.setObject(1,empId);
            preparedStatement.setObject(2,txtOldPassword.getText());
            return preparedStatement.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
