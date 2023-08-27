package com.pos.pointofsale.controller.cashierdashboard;

import com.pos.pointofsale.StageController;
import com.pos.pointofsale.database.DatabaseConnector;
import com.pos.pointofsale.model.OrderHistoryTable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HistoryController {
    public TableView<OrderHistoryTable> tblOrderHistory;
    public String empId = CashierDashboardController.empId;
    private final Connection connection = DatabaseConnector.getInstance().getConnection();
    private int selectedItemIndex = -1;
    public void initialize(){
        tableColumnInitializer();
        loadListData();
        onRowClicked();
    }
    public void loadListData(){
        ObservableList<OrderHistoryTable> items = tblOrderHistory.getItems();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT order_id, total_price, order_date, order_time FROM orders WHERE emp_id = ?");
            preparedStatement.setObject(1,empId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                items.add(new OrderHistoryTable(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4)));
            tblOrderHistory.refresh();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void onSelectRow(){

    }

    private void tableColumnInitializer(){
        tblOrderHistory.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("orderId"));
        tblOrderHistory.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        tblOrderHistory.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        tblOrderHistory.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("orderTime"));
    }

    public void onRowClicked(){
       tblOrderHistory.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
           @Override
           public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
               OrderHistoryTable selectedItem = tblOrderHistory.getSelectionModel().getSelectedItem();
               if (selectedItem==null) {
                   return;
               }

               selectedItemIndex = (int) t1;

               AnchorPane anchorPane = new AnchorPane();
               anchorPane.setPrefWidth(100);
               anchorPane.setPrefHeight(100);
               StackPane stackPane = new StackPane(anchorPane);
               stackPane.setPrefWidth(100);
               stackPane.setPrefHeight(100);
               Stage stage = new Stage();
               stage.setScene( new Scene(stackPane));
               stage.show();
           }
       });
    }

}
