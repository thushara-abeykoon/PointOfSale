package com.pos.pointofsale.controller.admindashboard;

import com.pos.pointofsale.database.DatabaseConnector;
import com.pos.pointofsale.model.AdminEmployeesTable;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminEmployeesFormController {
    public TableView<AdminEmployeesTable> tblEmployees;
    private final Connection connection = DatabaseConnector.getInstance().getConnection();
    public void initialize(){
        loadTableData();
    }

    private void loadTableData() {
        ObservableList<AdminEmployeesTable> items = tblEmployees.getItems();
        items.clear();
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT emp_id, fname, lname, email FROM employee");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
