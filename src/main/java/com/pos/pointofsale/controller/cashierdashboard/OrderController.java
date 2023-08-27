package com.pos.pointofsale.controller.cashierdashboard;

import com.pos.pointofsale.database.DatabaseConnector;
import com.pos.pointofsale.model.OrderTable;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.sql.*;
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

    public void initialize(){
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

    }

    public void txtItemQuantityOnKeyTyped(KeyEvent keyEvent) {
        if (txtItemQuantity.getText().isEmpty())
            txtTotal.setText("");
        else {
            double price = Double.parseDouble(txtItemPrice.getText());
            double quantity = Double.parseDouble(txtItemQuantity.getText());
            txtTotal.setText(String.valueOf(price*quantity));
        }
    }

    public void btnCheckoutOnAction(ActionEvent event) {
    }

    public void txtItemQuantityFilter(){
        txtItemQuantity.addEventFilter(KeyEvent.KEY_TYPED, event->{
            String character = event.getCharacter();

            String allowedCharacters = "0123456789.";
            if (!allowedCharacters.contains(character))
                event.consume();
        });
    }
}
