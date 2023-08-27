package com.pos.pointofsale.controller.cashierdashboard;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import com.pos.pointofsale.controller.ControllerCommon;
import com.pos.pointofsale.controller.LoginFormController;
import com.pos.pointofsale.database.DatabaseConnector;
import com.pos.pointofsale.model.OrderTable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;

public class OrderController {
    public Label lblOrderId;
    public TextField txtItemId;
    public TextField txtItemName;
    public TextField txtItemPrice;
    public TextField txtItemQuantity;
    public TextField txtTotal;
    public TableView<OrderTable> tblViewOrder;
    public Label lblTotalPrice;
    public static final Connection connection = DatabaseConnector.getInstance().getConnection();
    ArrayList<String> itemsList = new ArrayList<>();
    public double total = 0.00;
    public boolean isOnEdit = false;
    public int editItemIndex = -1;

    public void initialize(){
        loadColumnData();
        txtTotal.setText("0.0");
        setItemsList();
        AutoCompletionBinding<Object> objectAutoCompletionBinding = TextFields.bindAutoCompletion(txtItemName, itemsList.toArray());
        objectAutoCompletionBinding.setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<Object>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<Object> objectAutoCompletionEvent) {
                String itemId = txtItemName.getText().substring(0,8);
                String itemName = txtItemName.getText().substring(10);
                txtItemName.setText(itemName);
                txtItemId.setText(itemId);
                txtItemPrice.setText(getItemPrice(itemId));
                txtItemQuantity.requestFocus();
            }
        });
        txtItemQuantityFilter();
        txtItemQuantityOnKeyPressed();
        lblOrderId.setText(orderIdGenerator());
       // onSelectTableRow();
        onContextMenuRequestedOnTableRow();
    }


    public void onContextMenuRequestedOnTableRow(){
        tblViewOrder.setRowFactory(
                new Callback<TableView<OrderTable>, TableRow<OrderTable>>() {
                    @Override
                    public TableRow<OrderTable> call(TableView<OrderTable> orderTableTableView) {
                        final TableRow<OrderTable> row = new TableRow<>();
                        ContextMenu contextMenu = new ContextMenu();

                        MenuItem edit = new MenuItem("Edit");
                        edit.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                OrderTable orderTable = tblViewOrder.getSelectionModel().getSelectedItem();
                                if (orderTable==null)
                                    return;
                                txtItemId.setText(orderTable.getItemId());
                                txtItemName.setText(orderTable.getItemName());
                                txtItemPrice.setText(orderTable.getPrice());
                                txtItemQuantity.setText(orderTable.getQuantity());
                                txtTotal.setText(orderTable.getTotal());
                                editItemIndex = row.getIndex();
                                isOnEdit=true;
                                txtItemName.requestFocus();
                            }
                        });

                        MenuItem delete = new MenuItem("Delete");
                        delete.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                tblViewOrder.getItems().remove(row.getItem());
                            }
                        });
                        contextMenu.getItems().addAll(edit,delete);

                        row.contextMenuProperty().bind(Bindings.when(row.emptyProperty()).then((ContextMenu) null).otherwise(contextMenu));
                        return row;
                    }
                }
        );
    }

    public String getItemPrice(String itemId){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT price FROM item WHERE itm_id = ?");
            preparedStatement.setObject(1,itemId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return resultSet.getString(1);
            else
                return "0.00";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setItemsList(){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT itm_id,itm_name FROM item");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                itemsList.add(resultSet.getString(1)+" : "+resultSet.getString(2));
            Collections.sort(itemsList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void txtItemNameOnAction(ActionEvent event) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT itm_id FROM item WHERE itm_name = ?");
            preparedStatement.setObject(1,txtItemName.getText());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                txtItemId.setText(resultSet.getString(1));
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR,"Unable to find Item");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void txtItemNameOnKeyTyped(KeyEvent keyEvent) {

    }

    public void txtItemQuantityOnAction(ActionEvent event) {
        if (txtItemQuantity.getText().isEmpty())
            txtItemQuantity.requestFocus();
        else if(isOnEdit){
            tblViewOrder.getSelectionModel().clearSelection();
            ObservableList<OrderTable> orderItems = tblViewOrder.getItems();
            orderItems.set(editItemIndex,new OrderTable(txtItemId.getText(),txtItemName.getText(),txtItemPrice.getText(),txtItemQuantity.getText(),txtTotal.getText()));
            tblViewOrder.refresh();
            isOnEdit = false;
            onTableValueEntered();
        }
        else{
            loadTableData();
            total += Double.parseDouble(txtTotal.getText());
            lblTotalPrice.setText("TOTAL = "+getCorrectTotalPriceFormat(total)+" LKR");
            onTableValueEntered();
        }
    }

    public void onTableValueEntered(){
        txtItemId.clear();
        txtItemName.clear();
        txtItemPrice.clear();
        txtItemQuantity.clear();
        txtTotal.clear();
        txtItemName.requestFocus();
    }

    public void txtItemQuantityOnKeyPressed(){
        txtItemQuantity.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                getTotal();
            }
        });
        txtItemQuantity.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode()==KeyCode.BACK_SPACE||keyEvent.getCode()== KeyCode.DELETE)
                    getTotal();
            }
        });
    }

    public void getTotal(){
        if (txtItemPrice.getText().isEmpty())
            txtTotal.setText("0.00");
        else if (txtItemQuantity.getText().isEmpty())
            txtTotal.setText("0.00");
        else{
            double price = Double.parseDouble(txtItemPrice.getText());
            double quantity = Double.parseDouble(txtItemQuantity.getText());
            txtTotal.setText(getCorrectTotalPriceFormat(price*quantity));
        }
    }

    public String getCorrectTotalPriceFormat(double total){
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String totalValueString = decimalFormat.format(total);
        if(!totalValueString.contains("."))
            return totalValueString + ".00";
        else if (totalValueString.substring(totalValueString.length()-2).contains(".")) {
            return totalValueString + "0";
        }
        else {
            return totalValueString;
        }
    }

    public void btnCheckoutOnAction(ActionEvent event) {

        ObservableList<OrderTable> orderItems = tblViewOrder.getItems();

        if(orderItems.isEmpty())
            return;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO orders(order_id, emp_id, total_price, order_time, order_date) VALUES (?,?,?,curtime(),curdate())");
            preparedStatement.setObject(1,orderIdGenerator());
            preparedStatement.setObject(2, CashierDashboardController.empId);
            preparedStatement.setObject(3,total);
            int status = preparedStatement.executeUpdate();
            if (status>0){;
                orderItems.clear();
                lblTotalPrice.setText("TOTAL = 0.00 LKR");
                tblViewOrder.refresh();
                lblOrderId.setText(orderIdGenerator());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //In this method It's avoid appearing other than 0-9 and . characters in the Quantity text field
    public void txtItemQuantityFilter(){
        txtItemQuantity.addEventFilter(KeyEvent.KEY_TYPED, event->{
            String character = event.getCharacter();

            String allowedCharacters = "0123456789.";
            if (!allowedCharacters.contains(character))
                event.consume();
        });
    }

    public void loadTableData() {
        ObservableList<OrderTable> orderItems = tblViewOrder.getItems();
        orderItems.add(new OrderTable(txtItemId.getText(), txtItemName.getText(), txtItemPrice.getText(), txtItemQuantity.getText(), txtTotal.getText()));
        tblViewOrder.refresh();
    }

    public void loadColumnData(){
        tblViewOrder.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemId"));
        tblViewOrder.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("itemName"));
        tblViewOrder.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("price"));
        tblViewOrder.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tblViewOrder.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    public String orderIdGenerator(){
        String initialLetter = "0".repeat(16) + 1;
        return ControllerCommon.getID("orders", "order_id", "ODR", initialLetter);
    }

}
