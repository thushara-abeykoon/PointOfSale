package com.pos.pointofsale.controller.admindashboard;

import com.pos.pointofsale.database.DatabaseConnector;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimerTask;

public class AdminTotalOrdersUpdater extends TimerTask {
    private Label label;
    private final Connection connection = DatabaseConnector.getInstance().getConnection();

    public AdminTotalOrdersUpdater(Label label) {
        this.label = label;
    }

    @Override
    public void run() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) OVER() FROM orders WHERE order_date=CURDATE()");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                label.setText(resultSet.getString(1));
            else
                label.setText("0");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
