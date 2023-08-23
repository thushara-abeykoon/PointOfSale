package com.pos.pointofsale.controller;

import com.pos.pointofsale.StageController;
import com.pos.pointofsale.database.DatabaseConnector;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.event.ActionEvent;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            txtComputerID.setText();
        }

    }


    public void setComputerId(String computerId){
        lblAddThisComputer.setVisible(false);
        txtComputerID.setText(computerId);
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
        StageController stageController = new StageController();
        Stage stage = stageController.loadStage("view/ComputerAdder.fxml", "Add Computer");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
}
