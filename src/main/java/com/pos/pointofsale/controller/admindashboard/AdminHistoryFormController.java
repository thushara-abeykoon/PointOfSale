package com.pos.pointofsale.controller.admindashboard;

import com.pos.pointofsale.StageController;
import com.pos.pointofsale.controller.cashierdashboard.CashierOrderDataController;
import com.pos.pointofsale.database.DatabaseConnector;
import com.pos.pointofsale.model.AdminHistoryTable;
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

public class AdminHistoryFormController {
    public TableView<AdminHistoryTable> tblOrderHistory;
    private final Connection connection = DatabaseConnector.getInstance().getConnection();
    public void initialize(){
        tableColumnInitializer();
        loadTableData();
        onRowClicked();
    }

    private void tableColumnInitializer() {
        tblOrderHistory.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("orderId"));
        tblOrderHistory.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("empId"));
        tblOrderHistory.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        tblOrderHistory.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        tblOrderHistory.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("orderTime"));
    }


    private void loadTableData() {
        ObservableList<AdminHistoryTable> items = tblOrderHistory.getItems();
        items.clear();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT order_id, emp_id, total_price, order_date, order_time  FROM orders");
            while (resultSet.next())
                items.add(new AdminHistoryTable(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5)));
            tblOrderHistory.refresh();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void onRowClicked(){
        tblOrderHistory.setRowFactory(event -> {
            TableRow<AdminHistoryTable> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if (!row.isEmpty()&&mouseEvent.getButton().equals(MouseButton.PRIMARY)&&mouseEvent.getClickCount()==1){
                    CashierOrderDataController.orderId = row.getItem().getOrderId();
                    CashierOrderDataController.orderTotal = row.getItem().getTotalPrice();
                    StageController stageController = new StageController();
                    try {
                        Stage stage = stageController.loadStage("view/CashierOrderData.fxml", "Order Data");
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
}
