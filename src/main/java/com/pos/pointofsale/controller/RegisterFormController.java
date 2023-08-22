package com.pos.pointofsale.controller;

import com.pos.pointofsale.StageController;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

public class RegisterFormController {
    public MaterialIconView icnClose;
    public AnchorPane root;
    public Pane draggablePane;
    public TextField txtEmpID;
    public TextField txtFirstName;
    public TextField txtLastName;
    public TextField txtPhoneNo;
    public PasswordField txtPassword;
    public PasswordField txtConfirmPassword;
    public TextField txtEmail;


    private String empId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNo;
    private String password;

    public void initialize(){
        ControllerCommon controllerCommon = new ControllerCommon();
        controllerCommon.dragPane(draggablePane,root);
    }

    public void icnCloseOnClicked(MouseEvent mouseEvent) {
        StageController.closeStage(root);
    }

    public void icnCloseOnMouseEntered(MouseEvent mouseEvent) {
        icnClose.setFill(Paint.valueOf("#ff2700"));
    }

    public void icnCloseOnMouseExited(MouseEvent mouseEvent) {
        icnClose.setFill(Paint.valueOf("#d0d0d0"));
    }

    public void btnRegisterOnAction(ActionEvent event) {
    }
}
