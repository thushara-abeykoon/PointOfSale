package com.pos.pointofsale.controller.cashierdashboard;

import com.pos.pointofsale.database.DatabaseConnector;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashBoardController {

    public Label lblTodayRevenue;
    public Label lblYesterdayRevenue;
    public Label lblAverageDailyRevenue;
    public Label lblMonthlyWorkHours;
    public Label lblMonthlyTotalOrders;
    public Label lblMonthlyRevenue;

    private final Connection connection = DatabaseConnector.getInstance().getConnection();
    public static String empId;
    public static BorderPane rootDashboard;

    public void initialize(){
        empId = CashierDashboardController.empId;
        setLblTodayRevenue();
        setLblYesterdayRevenue();
        setLblAverageDailyRevenue();
        setLblMonthlyRevenueAndTotalOrders();
        setLblMonthlyWorkHours();

    }

    public void setLblTodayRevenue(){
        lblTodayRevenue.setText(getRevenue("curdate()"));
    }

    public void setLblYesterdayRevenue(){
        lblYesterdayRevenue.setText(getRevenue("DATE_SUB(CURDATE(), INTERVAL 1 DAY)"));
    }

    public String getRevenue(String date){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT SUM(total_price) FROM orders WHERE emp_id = ? AND order_date = "+date+";");
            preparedStatement.setObject(1,empId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()||resultSet.getString(1)==null)
                return "0 LKR";
            else
                return resultSet.getString(1)+" LKR";

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setLblAverageDailyRevenue(){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT SUM(total_price) FROM orders where emp_id = ?");
            preparedStatement.setObject(1,empId);
            ResultSet resultSet = preparedStatement.executeQuery();

            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT COUNT(order_date) OVER() FROM orders WHERE emp_id = ? GROUP BY order_date asc");
            preparedStatement1.setObject(1,empId);
            ResultSet resultSet1 = preparedStatement1.executeQuery();


            if(resultSet.next()&& resultSet.getString(1)!=null&& resultSet1.next()) {
                double sum = resultSet.getDouble(1);
                double count = resultSet1.getDouble(1);
                lblAverageDailyRevenue.setText(sum / count +" LKR");
            }
            else
                lblAverageDailyRevenue.setText("0.0 LKR");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void setLblMonthlyRevenueAndTotalOrders(){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT SUM(total_price),COUNT(total_price) FROM orders WHERE DATE_FORMAT(order_date,'%Y-%m')=DATE_FORMAT(curdate(),'%Y-%m') AND emp_id = ?");
            preparedStatement.setObject(1,empId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()||resultSet.getString(1)==null){
                lblMonthlyRevenue.setText("0.0 LKR");
                lblMonthlyTotalOrders.setText("0");
            }
            else{
                lblMonthlyTotalOrders.setText(resultSet.getString(2));
                lblMonthlyRevenue.setText(resultSet.getString(1)+" LKR");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setLblMonthlyWorkHours() {
        double hours = 0;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT TIMESTAMPDIFF(HOUR,login,logout) FROM employee_computer WHERE emp_id = ?");
            preparedStatement.setObject(1,empId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                hours+=resultSet.getDouble(1);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        lblMonthlyWorkHours.setText(String.valueOf(hours));

    }
}
