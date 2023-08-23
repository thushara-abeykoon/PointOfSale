package com.pos.pointofsale.controller;

import com.pos.pointofsale.StageController;
import com.pos.pointofsale.database.DatabaseConnector;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.PipedReader;
import java.sql.*;
import java.util.ArrayList;

public class LoginFormController {

    public PasswordField txtPassword;
    public Label lblForgotPassword;
    public Label lblCreateNewAccount;
    public MaterialIconView icnClose;
    public AnchorPane root;
    public TextField txtComputerID;
    public TextField txtEmployeeID;
    public Pane draggablePane;
    public Label lblAddThisComputer;
    private static final Connection connection = DatabaseConnector.getInstance().getConnection();



    public void initialize(){
        ControllerCommon controllerCommon = new ControllerCommon();
        controllerCommon.dragPane(draggablePane,root);
        if (isMacAvailable(ControllerCommon.getMacAddress())){
            lblAddThisComputer.setVisible(false);
            txtComputerID.setText(getComputerId(ControllerCommon.getMacAddress()));
        }

    }

    public String getComputerId(String macAddress){
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT cmp_id FROM computer WHERE mac_address= '" + macAddress + "';");
            if (resultSet.next())
                return resultSet.getString(1);
            else{
                throw new SQLException();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean isMacAvailable(String macAddress){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT mac_address FROM computer where mac_address = '" + macAddress + "';");
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void txtPasswordOnAction(ActionEvent event) {

    }

    public void btnLoginOnAction(ActionEvent event) {

        if (txtComputerID.getText().isEmpty())
            return;

        if (!loginEmpValidate()){
            Alert alert = new Alert(Alert.AlertType.ERROR,"Invalid Employee Id or Password!");
            alert.showAndWait();
            txtEmployeeID.clear();
            txtPassword.clear();
            txtEmployeeID.requestFocus();
        }
        else{
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO employee_computer(emp_id,cmp_id,login,logout) VALUES (?,?,current_timestamp(),current_timestamp())");
                preparedStatement.setObject(1,txtEmployeeID.getText());
                preparedStatement.setObject(2,txtComputerID.getText());
                int status = preparedStatement.executeUpdate();
                if (status>0){
                    StageController.closeStage(root);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void lblForgotPasswordOnMouseEntered(MouseEvent mouseEvent) {
        ControllerCommon.lblOnMouseMoved(lblForgotPassword);
    }

    public void lblForgotPasswordOnMouseExited(MouseEvent mouseEvent) {
        ControllerCommon.lblOnMouseExited(lblForgotPassword);
    }

    public void lblCreateNewAccountOnMouseEntered(MouseEvent mouseEvent) {
        ControllerCommon.lblOnMouseMoved(lblCreateNewAccount);
    }

    public void lblCreateNewAccountOnMouseExited(MouseEvent mouseEvent) {
        ControllerCommon.lblOnMouseExited(lblCreateNewAccount);
    }

    public void lblForgotPasswordOnMouseClicked(MouseEvent mouseEvent) {

    }

    public void lblCreateNewAccountOnMouseClicked(MouseEvent mouseEvent) throws IOException {
        StageController stageController = new StageController();
        Stage register = stageController.loadStage("view/RegisterForm.fxml", "Register");
        register.initStyle(StageStyle.UNDECORATED);
        register.centerOnScreen();
        register.show();
    }

    public void icnCloseOnClicked(MouseEvent mouseEvent) {
        StageController.closeStage(root);
    }

    public void icnCloseOnMouseExited(MouseEvent mouseEvent) {
        ControllerCommon.icnCloseOnMouseExited(icnClose);
    }

    public void icnCloseOnMouseEntered(MouseEvent mouseEvent) {
        ControllerCommon.icnCloseOnMouseEntered(icnClose);
    }


    public void lblAddThisComputerOnMouseEntered(MouseEvent mouseEvent) {
        ControllerCommon.lblOnMouseMoved(lblAddThisComputer);
    }

    public void lblAddThisComputerOnMouseExited(MouseEvent mouseEvent) {
        ControllerCommon.lblOnMouseExited(lblAddThisComputer);
    }

    public void lblAddThisComputerOnMouseClicked(MouseEvent mouseEvent) throws IOException {
        StageController.closeStage(root);
        StageController stageController = new StageController();
        Stage stage = stageController.loadStage("view/ComputerAdder.fxml", "Add Computer");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }


    public boolean loginEmpValidate(){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT emp_id FROM employee WHERE emp_id = ?");
            preparedStatement.setObject(1,txtEmployeeID.getText());
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() && loginPasswordValidate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean loginPasswordValidate(){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT password FROM employee WHERE password = MD5(?)");
            preparedStatement.setObject(1,txtPassword.getText());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
