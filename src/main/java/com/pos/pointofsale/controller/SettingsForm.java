package com.pos.pointofsale.controller;

import com.pos.pointofsale.controller.cashierdashboard.CashierFormController;
import com.pos.pointofsale.database.DatabaseConnector;
import com.pos.pointofsale.model.PrinterComboBox;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.print.*;

public class SettingsForm {
    public Label lblEmpId;
    public TextField txtFname;
    public TextField txtLname;
    public TextField txtEmail;
    public TextField txtPhoneNo;
    public Button btnSaveChanges;
    public Button btnChangePassword;
    public ComboBox<PrinterComboBox> cmbPrinter;
    private final Connection connection = DatabaseConnector.getInstance().getConnection();

    public void initialize(){
        lblEmpId.setText(CashierFormController.empId);
        getTextFieldValues();
        getPrinters();
    }
    private void getTextFieldValues(){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT fname, lname, email, phone_no FROM employee WHERE emp_id = ?");
            preparedStatement.setObject(1,lblEmpId.getText());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                txtFname.setText(resultSet.getString(1));
                txtLname.setText(resultSet.getString(2));
                txtEmail.setText(resultSet.getString(3));
                txtPhoneNo.setText(resultSet.getString(4));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void btnChangePassword(ActionEvent event) {
    }

    public void btnSaveChangesOnAction(ActionEvent event) {
        if (txtFname.getText().isEmpty())
            txtFname.requestFocus();
        else if (txtLname.getText().isEmpty()) {
            txtLname.requestFocus();
        } else if (txtEmail.getText().isEmpty()) {
            txtEmail.requestFocus();
        } else if (txtPhoneNo.getText().isEmpty()) {
            txtPhoneNo.requestFocus();
        }
        else {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE employee SET fname = ? , lname = ?, email = ?, phone_no = ? WHERE emp_id = ?");
                preparedStatement.setObject(1,txtFname.getText());
                preparedStatement.setObject(2,txtLname.getText());
                preparedStatement.setObject(3,txtEmail.getText());
                preparedStatement.setObject(4,txtPhoneNo.getText());
                preparedStatement.setObject(5,lblEmpId.getText());
                int status = preparedStatement.executeUpdate();
                if (status>0){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"SUCCESS",ButtonType.OK);
                    alert.showAndWait();
                }
                else {
                    throw new SQLException();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            selectDefaultPrinter();
        }
    }

    private void getPrinters(){
        PrintService defaultPrinter = PrintServiceLookup.lookupDefaultPrintService();
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null,null);
        ObservableList<PrinterComboBox> items = cmbPrinter.getItems();
        items.clear();
        for (PrintService printService : printServices)
            items.add(new PrinterComboBox(printService, printService.getName()));

        cmbPrinter.setValue(new PrinterComboBox(CashierFormController.printService,CashierFormController.printService.getName()));
    }

    public void selectDefaultPrinter(){
       CashierFormController.printService = cmbPrinter.getSelectionModel().getSelectedItem().getPrintService();
    }

}

