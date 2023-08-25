package com.pos.pointofsale.database;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {
    private static DatabaseConnector databaseConnector;
    private Connection connection;

    private DatabaseConnector(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos","root","");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e) {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306","root","");
                Statement statement = connection.createStatement();
                statement.executeUpdate("CREATE DATABASE pos");
                statement.execute("USE pos");
                statement.executeUpdate("CREATE TABLE employee (emp_id VARCHAR(7) PRIMARY KEY, fname VARCHAR(30) NOT NULL, lname VARCHAR(30) NOT NULL, email VARCHAR(30) NOT NULL, password VARCHAR(100) NOT NULL, UNIQUE (email));");
                statement.executeUpdate("CREATE TABLE computer (cmp_id VARCHAR(7) PRIMARY KEY, description VARCHAR(100), mac_address VARCHAR(20) NOT NULL, UNIQUE (mac_address));");
                statement.executeUpdate("CREATE TABLE employee_computer (emp_id VARCHAR(7) NOT NULL, cmp_id VARCHAR(7) NOT NULL, login DATETIME NOT NULL, logout DATETIME NOT NULL, FOREIGN KEY (emp_id) REFERENCES employee(emp_id), FOREIGN KEY (cmp_id) REFERENCES computer(cmp_id));");
                statement.executeUpdate("CREATE TABLE item (itm_id VARCHAR(10) PRIMARY KEY, itm_name VARCHAR(20) NOT NULL, itm_desc VARCHAR(100), price DECIMAL(10, 2) NOT NULL);");
                statement.executeUpdate("CREATE TABLE orders (order_id VARCHAR(20) PRIMARY KEY, order_time DATETIME NOT NULL, total_price DECIMAL(10, 2) NOT NULL, emp_id VARCHAR(7) NOT NULL, FOREIGN KEY (emp_id) REFERENCES employee(emp_id));");
                statement.executeUpdate("CREATE TABLE order_item (order_id VARCHAR(20) NOT NULL, itm_id VARCHAR(10) NOT NULL, quantity INT, price DECIMAL(10, 2) NOT NULL, FOREIGN KEY (order_id) REFERENCES orders(order_id), FOREIGN KEY (itm_id) REFERENCES item(itm_id));");


            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Database Connection Error!");
                alert.showAndWait();
            }
        }
    }

    public static DatabaseConnector getInstance(){
        return (databaseConnector==null)?databaseConnector=new DatabaseConnector():databaseConnector;
    }

    public Connection getConnection(){
        return connection;
    }

}
