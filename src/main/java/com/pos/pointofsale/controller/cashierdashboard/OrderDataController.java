package com.pos.pointofsale.controller.cashierdashboard;

import com.pos.pointofsale.database.DatabaseConnector;
import com.pos.pointofsale.model.OrderDataTable;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDataController {
    public Label lblOrderItemId;
    public Label lblOrderHistoryItemTotal;
    public TableView<OrderDataTable> tblOrderHistoryItem;
    private String orderId = HistoryController.currentOrderId;
    private String orderTotal = HistoryController.currentOrderTotal;
    private final Connection connection = DatabaseConnector.getInstance().getConnection();

    public void initialize(){
        lblOrderItemId.setText(orderId);
        lblOrderHistoryItemTotal.setText(orderTotal);
        tableColumnInitializer();
        loadTableData();
    }

    private void loadTableData(){
        ObservableList<OrderDataTable> items = tblOrderHistoryItem.getItems();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT order_item.itm_id,item.itm_name,  item.price, order_item.quantity, quantity*price as total FROM order_item join item on item.itm_id = order_item.itm_id where order_item.order_id=?");
            preparedStatement.setObject(1,orderId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                items.add(new OrderDataTable(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5)));

            tblOrderHistoryItem.refresh();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void tableColumnInitializer(){
        tblOrderHistoryItem.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemId"));
        tblOrderHistoryItem.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("itemName"));
        tblOrderHistoryItem.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
        tblOrderHistoryItem.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tblOrderHistoryItem.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("total"));

    }

}
