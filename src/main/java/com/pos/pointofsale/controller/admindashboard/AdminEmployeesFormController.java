package com.pos.pointofsale.controller.admindashboard;

import com.pos.pointofsale.StageController;
import com.pos.pointofsale.database.DatabaseConnector;
import com.pos.pointofsale.model.AdminEmployeesTable;
import javafx.collections.ObservableList;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminEmployeesFormController {
    public TableView<AdminEmployeesTable> tblEmployees;
    private final Connection connection = DatabaseConnector.getInstance().getConnection();
    public static String curEmpId;
    public static String curEmpName;
    public void initialize(){
        tableColumnInitializer();
        loadTableData();
        onRowClicked();
    }

    private void loadTableData() {
        ObservableList<AdminEmployeesTable> items = tblEmployees.getItems();
        items.clear();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT emp_id, fname, lname, email,phone_no FROM employee");
            while (resultSet.next())
                items.add(new AdminEmployeesTable(resultSet.getString(1),resultSet.getString(2)+" "+resultSet.getString(3),resultSet.getString(4),resultSet.getString(5)));
            tblEmployees.refresh();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void onRowClicked(){
        tblEmployees.setRowFactory(event->{
            TableRow<AdminEmployeesTable> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if (!row.isEmpty()&&mouseEvent.getClickCount()==1&&mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    StageController stageController = new StageController();
                    curEmpId = row.getItem().getEmpId();
                    curEmpName = row.getItem().getEmpName();
                    try {
                        Stage stage = stageController.loadStage("view/EmployeeMoreDetailsForm.fxml", "More Details");
                        stage.setResizable(false);
                        stage.show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return row;
        });
    }
    public void tableColumnInitializer(){
        tblEmployees.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("empId"));
        tblEmployees.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("empName"));
        tblEmployees.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("empEmail"));
        tblEmployees.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
    }
}
