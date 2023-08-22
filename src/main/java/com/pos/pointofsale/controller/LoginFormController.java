package com.pos.pointofsale.controller;

import com.pos.pointofsale.StageController;
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

public class LoginFormController {

    public PasswordField txtPassword;
    public Label lblForgotPassword;
    public Label lblCreateNewAccount;
    public MaterialIconView icnClose;
    public AnchorPane root;
    public TextField txtComputerID;
    public TextField txtEmployeeID;
    public Pane draggablePane;
    public void initialize(){
        ControllerCommon controllerCommon = new ControllerCommon();
        controllerCommon.dragPane(draggablePane,root);
    }
    public void txtPasswordOnAction(ActionEvent event) {

    }

    public void btnLoginOnAction(ActionEvent event) {
    }

    public void lblForgotPasswordOnMouseMoved(MouseEvent mouseEvent) {
        ControllerCommon.lblOnMouseMoved(lblForgotPassword);
    }

    public void lblForgotPasswordOnMouseExited(MouseEvent mouseEvent) {
        ControllerCommon.lblOnMouseMoved(lblForgotPassword);
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
        icnClose.setFill(Paint.valueOf("#d0d0d0"));
    }

    public void icnCloseOnMouseEntered(MouseEvent mouseEvent) {
        icnClose.setFill(Paint.valueOf("#ff2700"));
    }
}
