package com.pos.pointofsale.controller.admindashboard;

import com.pos.pointofsale.database.DatabaseConnector;
import com.pos.pointofsale.model.EmployeeLogDetailsTable;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeMoreDetailsFormController {
    public Label lblEmpId;
    public Label lblEmpName;
    public Label lblWorkHours;
    public Label lblMonthlyTotalOrders;
    public Label lblMonthlyRevenue;
    public TableView<EmployeeLogDetailsTable> tblLogDetails;
    private final Connection connection = DatabaseConnector.getInstance().getConnection();
    public void initialize(){
        lblEmpId.setText(AdminEmployeesFormController.curEmpId);
        lblEmpName.setText(AdminEmployeesFormController.curEmpName);
        setMonthlyTotalOrdersAndMonthlyRevenue();
        setLblMonthlyWorkHours();
        tableColumnInitializer();
        loadTableData();
    }
    private void setMonthlyTotalOrdersAndMonthlyRevenue(){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT SUM(total_price),COUNT(total_price) FROM orders WHERE DATE_FORMAT(order_date,'%Y-%m')=DATE_FORMAT(curdate(),'%Y-%m') AND emp_id = ?");
            preparedStatement.setObject(1,lblEmpId.getText());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                lblMonthlyRevenue.setText("MONTHLY REVENUE: "+resultSet.getString(1));
                lblMonthlyTotalOrders.setText("MONTHLY TOTAL ORDERS: "+ resultSet.getString(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void setLblMonthlyWorkHours() {
        double hours = 0;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT TIMESTAMPDIFF(HOUR,login,logout) FROM employee_computer WHERE emp_id = ?");
            preparedStatement.setObject(1,lblEmpId.getText());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                hours+=resultSet.getDouble(1);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        lblWorkHours.setText("WORK HOURS : "+ hours);
    }
    private void loadTableData(){
        ObservableList<EmployeeLogDetailsTable> items = tblLogDetails.getItems();
        items.clear();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT cmp_id, login, logout FROM employee_computer WHERE emp_id = ?");
            preparedStatement.setObject(1,lblEmpId.getText());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                items.add(new EmployeeLogDetailsTable(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3)));
            tblLogDetails.refresh();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void tableColumnInitializer(){
        tblLogDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("cmpId"));
        tblLogDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("login"));
        tblLogDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("logout"));
    }
}
