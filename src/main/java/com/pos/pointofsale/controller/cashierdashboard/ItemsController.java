package com.pos.pointofsale.controller.cashierdashboard;

import com.pos.pointofsale.controller.ControllerCommon;
import com.pos.pointofsale.database.DatabaseConnector;
import com.pos.pointofsale.model.ItemsTable;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;

import java.sql.*;

public class ItemsController {
    public TableView<ItemsTable> tblItems;
    public TextField txtItemId;
    public TextField txtItemName;
    public TextField txtItemDescription;
    public TextField txtItemPrice;
    private boolean isOnEdit = false;
    private int editItemIndex = -1;
    private final Connection connection = DatabaseConnector.getInstance().getConnection();
    public void initialize(){
        txtItemId.setText(getItemId());
        tableColumnInitializer();
        loadTableItems();
        onTableRowContextMenuRequested();
        ControllerCommon.textFieldFilter(txtItemPrice);
    }
    public void loadTableItems(){
        ObservableList<ItemsTable> items = tblItems.getItems();
        items.clear();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT itm_id,itm_name,itm_desc,price FROM item");
            while (resultSet.next())
                items.add(new ItemsTable(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4)));
            tblItems.refresh();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void onTableRowContextMenuRequested(){
        tblItems.setRowFactory(event->{
            TableRow<ItemsTable> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem edit = new MenuItem("Edit");
            edit.setOnAction(eventEdit -> {
                ItemsTable selectedItem = tblItems.getSelectionModel().getSelectedItem();
                if (selectedItem == null)
                    return;
                txtItemId.setText(row.getItem().getItemId());
                txtItemName.setText(row.getItem().getItemName());
                txtItemDescription.setText(row.getItem().getItemDescription());
                txtItemPrice.setText(row.getItem().getItemPrice());
                editItemIndex = row.getIndex();
                isOnEdit = true;
                txtItemName.requestFocus();
            });
            MenuItem delete = new MenuItem("Delete");
            delete.setOnAction(event1 -> {
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM item WHERE itm_id = ?");
                    preparedStatement.setObject(1,row.getItem().getItemId());
                    int status = preparedStatement.executeUpdate();
                    if (!(status>0))
                        throw new SQLException();
                    else {
                        loadTableItems();
                        txtItemId.setText(getItemId());
                    }
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,"Item cannot be deleted due to order history fail...");
                    alert.showAndWait();
                    e.printStackTrace();
                }
            });
            contextMenu.getItems().addAll(edit,delete);
            row.contextMenuProperty().bind(Bindings.when(row.emptyProperty()).then((ContextMenu) null).otherwise(contextMenu));
            return row;
        });
    }


    private void addItem(){
        if (txtItemName.getText().isEmpty())
            txtItemName.requestFocus();
        else if (txtItemPrice.getText().isEmpty()) {
            txtItemPrice.requestFocus();
        }
        else if (isOnEdit){
            tblItems.getSelectionModel().clearSelection();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE item SET itm_name = ?, itm_desc = ?, price=? WHERE itm_id = ?");
                preparedStatement.setObject(1,txtItemName.getText());
                preparedStatement.setObject(2,txtItemDescription.getText());
                preparedStatement.setObject(3,Double.parseDouble(txtItemPrice.getText()));
                preparedStatement.setObject(4,txtItemId.getText());
                int i = preparedStatement.executeUpdate();
                if (i>0)
                    loadTableItems();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            clearAllTextFields();
            isOnEdit=false;
        }
        else {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO item(itm_id, itm_name, itm_desc, price) VALUES (?,?,?,?)");
                preparedStatement.setObject(1, txtItemId.getText());
                preparedStatement.setObject(2,txtItemName.getText());
                preparedStatement.setObject(3,txtItemDescription.getText());
                preparedStatement.setObject(4,txtItemPrice.getText());
                int i = preparedStatement.executeUpdate();
                if (i>0)
                    loadTableItems();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        clearAllTextFields();
    }
    private void clearAllTextFields(){
        txtItemName.clear();
        txtItemPrice.clear();
        txtItemDescription.clear();
        txtItemId.setText(getItemId());
        txtItemName.requestFocus();
    }
    private void tableColumnInitializer(){
        tblItems.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemId"));
        tblItems.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("itemName"));
        tblItems.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("itemDescription"));
        tblItems.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
    }

    public void txtItemNameOnAction(ActionEvent event) {
        txtItemDescription.requestFocus();
    }

    public void txtItemDescriptionOnAction(ActionEvent event) {
        txtItemPrice.requestFocus();
    }

    public void txtItemPriceOnAction(ActionEvent event) {
        addItem();
    }

    public void btnAddNewItemOnAction(ActionEvent event) {
        addItem();
    }

    public void txtItemPriceOnKeyTyped(KeyEvent keyEvent) {
    }

    private String getItemId(){
        return ControllerCommon.getID("item","itm_id","ITM","0000001");
    }
}
