package com.pos.pointofsale.tasks;

import com.pos.pointofsale.database.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.TimerTask;

public class EmployeeWorkHourUpdate extends TimerTask {
    private final Connection connection = DatabaseConnector.getInstance().getConnection();
    private final String logId;

    public EmployeeWorkHourUpdate(String logId) {
        this.logId = logId;
    }

    @Override
    public void run() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE employee_computer SET logout = current_timestamp() WHERE log_id = ?");
            preparedStatement.setObject(1,logId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
