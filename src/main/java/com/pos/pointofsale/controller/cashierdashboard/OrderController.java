package com.pos.pointofsale.controller.cashierdashboard;

import com.pos.pointofsale.database.DatabaseConnector;
import com.pos.pointofsale.model.OrderTable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OrderController {
    public Label lblOrderId;
    public TextField txtItemId;
    public TextField txtItemName;
    public TextField txtItemPrice;
    public TextField txtItemQuantity;
    public TextField txtTotal;
    public TableView<OrderTable> tblViewOrder;
    public Label lblTotalPrice;
    private AutoCompletionBinding<String> autoCompletionBinding;
    private String [] _possibleSuggestions = {"bakathapas","gona","meeharaka","chamuditha"};
    private Set<String> possibleSuggestions = new HashSet<>(Arrays.asList(_possibleSuggestions));
    public static final Connection connection = DatabaseConnector.getInstance().getConnection();

    public void initialize(){
        tblViewOrder.placeholderProperty().setValue(new Label(""));
        autoCompletionBinding = TextFields.bindAutoCompletion(txtItemName, possibleSuggestions);
        txtItemName.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()){
                    case ENTER:
                        autoCompletionBindingWord(txtItemName.getText().trim());
                        break;
                    default:
                        break;
                }
            }
        });

    }

    private void autoCompletionBindingWord(String newWord){
        if(autoCompletionBinding!=null)
            autoCompletionBinding.dispose();
        autoCompletionBinding = TextFields.bindAutoCompletion(txtItemName,possibleSuggestions);
    }

    public void txtItemNameOnAction(ActionEvent event) {

    }

    public void txtItemNameOnKeyTyped(KeyEvent keyEvent) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT itm_id FROM item WHERE itm_name LIKE ?");
            preparedStatement.setObject(1,txtItemName.getText());
            ResultSet resultSet = preparedStatement.executeQuery();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void txtItemQuantityOnAction(ActionEvent event) {
    }

    public void txtItemQuantityOnKeyTyped(KeyEvent keyEvent) {
    }

    public void btnCheckoutOnAction(ActionEvent event) {
    }
}
