package com.pos.pointofsale.controller.admindashboard;

import com.pos.pointofsale.database.DatabaseConnector;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimerTask;

public class AdminRevenueUpdater extends TimerTask {
    private String date;
    private Label label;
    private Connection connection = DatabaseConnector.getInstance().getConnection();

    public AdminRevenueUpdater(String date, Label label) {
        this.date = date;
        this.label = label;
    }

    @Override
    public void run() {
        label.setText(getRevenue());
    }
    public String getRevenue(){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT SUM(total_price) FROM orders WHERE order_date = "+date+";");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()||resultSet.getString(1)==null)
                return "0 LKR";
            else
                return resultSet.getString(1)+" LKR";

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
