package com.pos.pointofsale.controller.registerformcontroller;

import com.pos.pointofsale.StageController;
import com.pos.pointofsale.controller.ControllerCommon;
import com.pos.pointofsale.database.DatabaseConnector;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public Label emailExistStatus;
    Connection connection = DatabaseConnector.getInstance().getConnection();

    public void initialize(){
        ControllerCommon controllerCommon = new ControllerCommon();
        controllerCommon.dragPane(draggablePane,root);
        txtEmpID.setText(ControllerCommon.getID("employee","emp_id","EMP"));
        txtPhoneNoEventFilter();
    }

    public void icnCloseOnClicked() {
        StageController.closeStage(root);
    }

    public void icnCloseOnMouseEntered() {
        icnClose.setFill(Paint.valueOf("#ff2700"));
    }

    public void icnCloseOnMouseExited() {
        icnClose.setFill(Paint.valueOf("#d0d0d0"));
    }

    public void btnRegisterOnAction() {
        if (txtFirstName.getText().isEmpty())
            txtFirstName.requestFocus();
        else if (txtLastName.getText().isEmpty())
            txtLastName.requestFocus();
        else if (txtEmail.getText().isEmpty()||!registerValidation.isValidEmail(txtEmail.getText())||isEmailExists(txtEmail.getText())) {
            txtEmail.clear();
            txtEmail.setStyle("-fx-background-color: white");
            txtEmail.requestFocus();
        }
        else if (registerValidation.invalidPassword(txtPassword.getText())){
            txtPassword.clear();
            txtConfirmPassword.clear();
            txtPasswordsSetBackgroundColor("white");
            txtPassword.requestFocus();
        } else if (registerValidation.invalidMobileNumber(txtPhoneNo.getText())) {
            txtPhoneNo.clear();
            txtPhoneNo.requestFocus();
        } else{

            try {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO employee(emp_id,fname,lname,email,password) VALUES (?,?,?,?,MD5(?));");
                preparedStatement.setObject(1,txtEmpID.getText());
                preparedStatement.setObject(2,txtFirstName.getText());
                preparedStatement.setObject(3,txtLastName.getText());
                preparedStatement.setObject(4,txtEmail.getText());
                preparedStatement.setObject(5,txtPassword.getText());
                int status = preparedStatement.executeUpdate();
                if (status>0) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"ACCOUNT CREATED!!", ButtonType.OK);
                    alert.showAndWait();
                }
                StageController.closeStage(root);

            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR,"Database Connection Failed!");
                alert.showAndWait();
                StageController.closeStage(root);
            }

        }
    }

    private boolean isEmailExists(String email){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select email from employee where email = '"+email+"';");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                emailExistStatus.setVisible(true);
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void txtEmailOnKeyTyped() {
        emailExistStatus.setVisible(false);
        if (!registerValidation.isValidEmail(txtEmail.getText())||isEmailExists(txtEmail.getText()))
            txtEmail.setStyle("-fx-background-color: #ff7070");
        else
            txtEmail.setStyle("-fx-background-color: white");
    }

    public void txtPasswordOnKeyTyped() {
        if (registerValidation.invalidPassword(txtPassword.getText()))
            txtPasswordsSetBackgroundColor("#ff7070");
        else {
            txtPassword.setStyle("-fx-background-color: white");
            if (txtPassword.getText().equals(txtConfirmPassword.getText()))
                txtConfirmPassword.setStyle("-fx-background-color: white");
        }
    }

    public void txtConfirmPasswordOnKeyTyped(KeyEvent keyEvent) {
        if (!txtPassword.getText().equals(txtConfirmPassword.getText())|| registerValidation.invalidPassword(txtConfirmPassword.getText()))
            txtConfirmPassword.setStyle("-fx-background-color: #ff7070");
        else
            txtPasswordsSetBackgroundColor("white");

    }
    public void txtPasswordsSetBackgroundColor(String color){
        txtPassword.setStyle("-fx-background-color: "+color);
        txtConfirmPassword.setStyle("-fx-background-color: "+color);
    }


    public void txtPhoneNoEventFilter(){
        txtPhoneNo.addEventFilter(KeyEvent.KEY_TYPED, event->{
            String character = event.getCharacter();

            String allowedCharacters = "0123456789";
            if (!allowedCharacters.contains(character))
                event.consume();
        });
    }
}
