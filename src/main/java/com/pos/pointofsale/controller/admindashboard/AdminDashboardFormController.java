package com.pos.pointofsale.controller.admindashboard;

import com.pos.pointofsale.controller.ControllerCommon;
import com.pos.pointofsale.database.DatabaseConnector;
import com.pos.pointofsale.tasks.AdminRevenueUpdater;
import com.pos.pointofsale.tasks.AdminTotalOrdersUpdater;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.sql.*;
import java.util.Timer;

public class AdminDashboardFormController {
    public BorderPane rootDashboard;
    public Label lblTodayRevenue;
    public Label lblYesterdayRevenue;
    public Label lblAverageDailyRevenue;
    public Label lblEarningsRate;
    public Label lblMonthlyTotalOrders;
    public Label lblMonthlyRevenue;
    public Label lblNoOfComputers;
    public Label lblNoOfEmployees;
    public Label lblDailyTotalOrders;

    private final Connection connection = DatabaseConnector.getInstance().getConnection();
    public static AdminRevenueUpdater adminRevenueUpdater1;
    public static AdminRevenueUpdater adminRevenueUpdater2;
    public static AdminTotalOrdersUpdater adminTotalOrdersUpdater;
    public static Timer timer1;
    public static Timer timer2;

    public void initialize(){
        setLblTodayRevenue();
        setLblYesterdayRevenue();
        setLblAverageDailyRevenue();
        setLblDailyTotalOrders();
        setLblMonthlyTotalOrders();
        setLblEarningsRate();
        setLblNoOfEmployees();
        setLblNoOfComputers();
    }
    private void setLblTodayRevenue(){
        adminRevenueUpdater1 = new AdminRevenueUpdater("curdate()",lblTodayRevenue);
        timer1 = new Timer();
        timer1.schedule(adminRevenueUpdater1,0,1000);
    }

    private void setLblYesterdayRevenue(){
        adminRevenueUpdater2 = new AdminRevenueUpdater("DATE_SUB(CURDATE(), INTERVAL 1 DAY)",lblYesterdayRevenue);
        lblYesterdayRevenue.setText(adminRevenueUpdater2.getRevenue());
    }

    private void setLblAverageDailyRevenue(){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT SUM(total_price) FROM orders");
            ResultSet sumResults = preparedStatement.executeQuery();

            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT COUNT(order_date) OVER() FROM orders GROUP BY order_date asc");
            ResultSet countResults = preparedStatement1.executeQuery();


            if(sumResults.next()&& sumResults.getString(1)!=null&& countResults.next()) {
                double sum = sumResults.getDouble(1);
                double count = countResults.getDouble(1);
                lblAverageDailyRevenue.setText(ControllerCommon.getCorrectPriceFormat(sum / count) +" LKR");
            }
            else
                lblAverageDailyRevenue.setText("0.00 LKR");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setLblMonthlyTotalOrders(){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT SUM(total_price),COUNT(total_price) FROM orders WHERE DATE_FORMAT(order_date,'%Y-%m')=DATE_FORMAT(curdate(),'%Y-%m')");
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
    private void setLblEarningsRate(){
        double currentYesterdayRevenue = Double.parseDouble(lblAverageDailyRevenue.getText().substring(0,lblAverageDailyRevenue.getText().length()-4));
        double average;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT SUM(total_price) FROM orders WHERE orders.order_date != curdate()");
            ResultSet totalSum = preparedStatement.executeQuery();
            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT COUNT(total_price) OVER() FROM orders WHERE order_date != CURDATE()");
            ResultSet totalCount = preparedStatement1.executeQuery();
            if (totalCount.next()&&totalSum.next()) {
                average = totalSum.getDouble(1) / totalCount.getDouble(1);
                lblEarningsRate.setText(String.valueOf(currentYesterdayRevenue / average));
            }
            else{
                lblEarningsRate.setText("Not enough data");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void setLblDailyTotalOrders() {
        adminTotalOrdersUpdater = new AdminTotalOrdersUpdater(lblDailyTotalOrders);
        timer2 = new Timer();
        timer2.schedule(adminTotalOrdersUpdater,0,1000);
    }

    public void setLblNoOfEmployees(){
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM employee");
            if (resultSet.next())
                lblNoOfEmployees.setText(resultSet.getString(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void setLblNoOfComputers(){
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM computer");
            if (resultSet.next())
                lblNoOfComputers.setText(resultSet.getString(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
