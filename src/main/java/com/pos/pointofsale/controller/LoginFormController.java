package com.pos.pointofsale.controller;

import com.pos.pointofsale.StageController;
import com.pos.pointofsale.controller.cashierdashboard.CashierFormController;
import com.pos.pointofsale.database.DatabaseConnector;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;

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
    public static String cmpId;
    public static String logId;
    public static Scene scene;


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

    public void txtPasswordOnAction() {
        login();
    }

    public void btnLoginOnAction() {
        login();
    }

    public void login(){
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

                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO employee_computer (emp_id,cmp_id,login,logout) VALUES (?,?,current_timestamp(),current_timestamp())");
                preparedStatement.setObject(1,txtEmployeeID.getText());
                preparedStatement.setObject(2,txtComputerID.getText());
                int status = preparedStatement.executeUpdate();
                getLogId();
                if (status>0){
                    cmpId = txtComputerID.getText();
                    CashierFormController.empId = txtEmployeeID.getText();
                    StageController stageController = new StageController();
                    scene = stageController.loadScene("view/CashierForm.fxml");
                    Stage stage = new Stage();
                    stage.getIcons().add(StageController.getIcon());
                    stage.setTitle("Cashier Dashboard");
                    stage.setScene(scene);
                    stage.centerOnScreen();
                    stage.setMaximized(true);
                    stage.show();
                    StageController.closeStage(root);
                }
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void getLogId() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT log_id FROM employee_computer ORDER BY log_id desc LIMIT 1;");
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next())
            logId = resultSet.getString(1);
        else
            throw new SQLException();

    }

    public void lblForgotPasswordOnMouseEntered() {
        ControllerCommon.lblOnMouseMoved(lblForgotPassword);
    }

    public void lblForgotPasswordOnMouseExited() {
        ControllerCommon.lblOnMouseExited(lblForgotPassword);
    }

    public void lblCreateNewAccountOnMouseEntered() {
        ControllerCommon.lblOnMouseMoved(lblCreateNewAccount);
    }

    public void lblCreateNewAccountOnMouseExited() {
        ControllerCommon.lblOnMouseExited(lblCreateNewAccount);
    }

    public void lblForgotPasswordOnMouseClicked() {

    }

    public void lblCreateNewAccountOnMouseClicked() throws IOException {
        StageController stageController = new StageController();
        Stage register = stageController.loadStage("view/RegisterForm.fxml", "Register");
        register.initStyle(StageStyle.UNDECORATED);
        register.centerOnScreen();
        register.show();
    }

    public void icnCloseOnClicked() {
        StageController.closeStage(root);
    }

    public void icnCloseOnMouseExited() {
        ControllerCommon.icnCloseOnMouseExited(icnClose);
    }

    public void icnCloseOnMouseEntered() {
        ControllerCommon.icnCloseOnMouseEntered(icnClose);
    }


    public void lblAddThisComputerOnMouseEntered() {
        ControllerCommon.lblOnMouseMoved(lblAddThisComputer);
    }

    public void lblAddThisComputerOnMouseExited() {
        ControllerCommon.lblOnMouseExited(lblAddThisComputer);
    }

    public void lblAddThisComputerOnMouseClicked() throws IOException {
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
