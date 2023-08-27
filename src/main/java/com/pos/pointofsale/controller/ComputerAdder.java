package com.pos.pointofsale.controller;

import com.pos.pointofsale.StageController;
import com.pos.pointofsale.database.DatabaseConnector;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ComputerAdder {
    public TextField txtComputerId;
    public TextField txtMacAddress;
    public TextField txtDescription;
    public AnchorPane computerAdder;
    public MaterialIconView icnClose;
    public Pane draggablePane;

    StageController stageController = new StageController();

    public void initialize(){
        ControllerCommon controllerCommon = new ControllerCommon();
        controllerCommon.dragPane(draggablePane,computerAdder);
        txtComputerId.setText(ControllerCommon.getID("computer","cmp_id","CMP","0001"));
        txtMacAddress.setText(ControllerCommon.getMacAddress());
    }
    public void btnOkOnAction(ActionEvent event) {

        Connection connection = DatabaseConnector.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO computer(cmp_id,mac_address,description) VALUES (?,?,?);");
            preparedStatement.setObject(1,txtComputerId.getText());
            preparedStatement.setObject(2,txtMacAddress.getText());
            preparedStatement.setObject(3,txtDescription.getText());

            int status = preparedStatement.executeUpdate();
            if (status>0){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Success!!");
                alert.showAndWait();
                StageController.closeStage(computerAdder);
                Stage stage = stageController.loadStage("view/LoginForm.fxml", "Login");
                stage.centerOnScreen();
                stage.show();

            }

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void icnCloseOnMouseEntered(MouseEvent mouseEvent) {
        ControllerCommon.icnCloseOnMouseEntered(icnClose);
    }

    public void icnCloseOnMouseExited(MouseEvent mouseEvent) {
        ControllerCommon.icnCloseOnMouseExited(icnClose);
    }

    public void icnCloseOnMouseClicked(MouseEvent mouseEvent) throws IOException {
        StageController.closeStage(computerAdder);
        Stage stage = stageController.loadStage("view/LoginForm.fxml", "Login");
        stage.centerOnScreen();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
}
