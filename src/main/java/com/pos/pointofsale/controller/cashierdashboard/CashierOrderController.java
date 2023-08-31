package com.pos.pointofsale.controller.cashierdashboard;

import com.pos.pointofsale.controller.ControllerCommon;
import com.pos.pointofsale.database.DatabaseConnector;
import com.pos.pointofsale.model.InvoiceOrderItems;
import com.pos.pointofsale.model.OrderTable;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;

public class CashierOrderController {
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
    private static String empId;

    public void initialize(){
        empId = CashierFormController.empId;
        loadColumnData();
        txtTotal.setText("0.0");
        setItemsList();
        AutoCompletionBinding<Object> objectAutoCompletionBinding = TextFields.bindAutoCompletion(txtItemName, itemsList.toArray());
        objectAutoCompletionBinding.setOnAutoCompleted(objectAutoCompletionEvent -> {
            String itemId = txtItemName.getText().substring(0, 10);
            String itemName = txtItemName.getText().substring(12);
            txtItemName.setText(itemName);
            txtItemId.setText(itemId);
            txtItemPrice.setText(getItemPrice(itemId));
            txtItemQuantity.requestFocus();
        });
        ControllerCommon.textFieldFilter(txtItemQuantity);
        txtItemQuantityOnKeyPressed();
        lblOrderId.setText(orderIdGenerator());
       // onSelectTableRow();
        onContextMenuRequestedOnTableRow();
    }


    public void onContextMenuRequestedOnTableRow(){
        tblViewOrder.setRowFactory(
                orderTableTableView -> {
                    final TableRow<OrderTable> row = new TableRow<>();
                    ContextMenu contextMenu = new ContextMenu();

                    MenuItem edit = new MenuItem("Edit");
                    edit.setOnAction(event -> {
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
                    });

                    MenuItem delete = new MenuItem("Delete");
                    delete.setOnAction(event -> tblViewOrder.getItems().remove(row.getItem()));
                    contextMenu.getItems().addAll(edit,delete);

                    row.contextMenuProperty().bind(Bindings.when(row.emptyProperty()).then((ContextMenu) null).otherwise(contextMenu));
                    return row;
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

    public void txtItemNameOnAction() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT itm_id FROM item WHERE itm_name = ?");
            preparedStatement.setObject(1,txtItemName.getText());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                txtItemId.setText(resultSet.getString(1));
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR,"Unable to find Item");
                alert.showAndWait();
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void txtItemNameOnKeyTyped() {

    }

    public void txtItemQuantityOnAction() {
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
        txtItemQuantity.setOnKeyTyped(keyEvent -> getTotal());
        txtItemQuantity.setOnKeyReleased(keyEvent -> {
            if(keyEvent.getCode()==KeyCode.BACK_SPACE||keyEvent.getCode()== KeyCode.DELETE)
                getTotal();
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

    public void btnCheckoutOnAction() {

        ObservableList<OrderTable> orderItems = tblViewOrder.getItems();
        String currentOrderID = orderIdGenerator();

        if(orderItems.isEmpty())
            return;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO orders(order_id, emp_id, total_price, order_time, order_date) VALUES (?,?,?,curtime(),curdate())");
            preparedStatement.setObject(1,currentOrderID);
            preparedStatement.setObject(2,empId);
            preparedStatement.setObject(3,total);
            int status = preparedStatement.executeUpdate();
            if (status>0){
                insertIntoOrderItemTable(currentOrderID);
                printInvoice();
                orderItems.clear();
                lblTotalPrice.setText("TOTAL = 0.00 LKR");
                tblViewOrder.refresh();
                lblOrderId.setText(orderIdGenerator());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    public void insertIntoOrderItemTable(String orderId) {
        try {
            ObservableList<OrderTable> orderItems = tblViewOrder.getItems();
            for (int i = 0;i<orderItems.toArray().length; i++) {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO order_item(order_id, itm_id, quantity) VALUES (?,?,?)");
                preparedStatement.setObject(1,orderId);
                preparedStatement.setObject(2,orderItems.get(i).getItemId());
                preparedStatement.setObject(3,Integer.parseInt(orderItems.get(i).getQuantity()));
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void printInvoice() throws SQLException {
        String jrxmlPath = "E:\\PROJECTS\\JavaFX Projects\\PointOfSale\\src\\main\\resources\\com\\pos\\pointofsale\\jasper\\invoice.jrxml";
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("orderId",lblOrderId.getText());
        parameters.put("totalAmount",lblTotalPrice.getText());
        List<InvoiceOrderItems> orderItemsList = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT order_item.itm_id,i.itm_name, i.price, order_item.quantity, order_item.quantity*i.price as total FROM order_item JOIN item i on i.itm_id = order_item.itm_id  WHERE order_id = ?");
        preparedStatement.setObject(1,lblOrderId.getText());
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1) + resultSet.getString(2) + resultSet.getString(3) + resultSet.getString(4) + resultSet.getString(5));
            orderItemsList.add(new InvoiceOrderItems(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5)));
        }
        JRBeanCollectionDataSource orderItems = new JRBeanCollectionDataSource(orderItemsList);
        parameters.put("OrderItems",orderItems);
        try {
            JasperReport report = JasperCompileManager.compileReport(jrxmlPath);
            JasperPrint print = JasperFillManager.fillReport(report,parameters,new JREmptyDataSource());
            JasperExportManager.exportReportToPdfFile(print,"E:\\PROJECTS\\JavaFX Projects\\PointOfSale\\src\\main\\resources\\com\\pos\\pointofsale\\jasper\\invoice.pdf");
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }
}
