package com.pos.pointofsale.controller.cashierdashboard;

import com.pos.pointofsale.StageController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Arrays;

public class CashierDashboardController extends DashboardStyleConfiguration {

    public VBox vboxLeft;
    public ImageView imgMore;
    public Pane paneDashboard;
    public Pane paneOrder;
    public Pane paneHistory;
    public Pane paneItems;
    public Label lblItems;
    public Pane paneEmployeeDetails;
    public Pane paneSettings;
    public Pane paneLogOut;
    public Pane paneEmpty;
    public Label lblDashboard;
    public Label lblOrder;
    public Label lblHistory;
    public Label lblEmployeeDetails;
    public Label lblSettings;
    public Label lblLogout;
    public static boolean isOptionVisible = false;
    public BorderPane root;

    public void imgMoreOnMouseClicked(MouseEvent mouseEvent) {

        if (!isOptionVisible){
            Timeline timeline = animateVbox(0.15, 220);
            timeline.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    setVisibleOptions(true);
                }
            });
            timeline.play();
            isOptionVisible=true;
        }else{
            setVisibleOptions(false);
            Timeline timeline = animateVbox(0.15,64);
            timeline.play();
            isOptionVisible=false;
        }

    }

    private void setVisibleOptions(boolean visibility){
        for (Label label : Arrays.asList(lblDashboard, lblOrder, lblHistory, lblItems, lblEmployeeDetails, lblSettings, lblLogout)) {
            label.setVisible(visibility);
        }
    }

    public Timeline animateVbox(double timeDuration, double targetWidth){

        Duration duration = Duration.seconds(timeDuration);

        KeyFrame keyFrame1 = new KeyFrame(Duration.ZERO, new KeyValue(vboxLeft.prefWidthProperty(), vboxLeft.getPrefWidth()));
        KeyFrame keyFrame2 = new KeyFrame(duration, new KeyValue(vboxLeft.prefWidthProperty(), targetWidth));

        return new Timeline(keyFrame1, keyFrame2);
    }


    public void paneDashboardOnMouseClicked(MouseEvent mouseEvent) {

        StageController stageController = new StageController();
        try {
            Parent parent = stageController.loadScene("view/DashboardForm.fxml").getRoot();
            root.setCenter(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void paneOrderOnMouseClicked(MouseEvent mouseEvent) {
    }

    public void paneHistoryOnMouseClicked(MouseEvent mouseEvent) {
    }

    public void paneItemsOnMouseClicked(MouseEvent mouseEvent) {
    }

    public void paneEmpoyeeDetailsOnMouseClicked(MouseEvent mouseEvent) {
    }

    public void paneSettingsOnMouseClicked(MouseEvent mouseEvent) {
    }

    public void paneLogOutOnMoseClicked(MouseEvent mouseEvent) {
    }


    //Mouse Hover Settings

    public void paneDashboardOnMouseEntered( ) {
        setPaneBackground(paneDashboard,"#282929");
    }

    public void paneDashboardOnMouseExited( ) {
        setPaneBackground(paneDashboard,"transparent");
    }

    public void paneOrderOnMouseEntered( ) {
        setPaneBackground(paneOrder,"#282929");
    }

    public void paneOrderOnMouseExited( ) {
        DashboardStyleConfiguration.setPaneBackground(paneOrder,"transparent");
    }

    public void paneHistoryOnMouseEntered( ) {
        DashboardStyleConfiguration.setPaneBackground(paneHistory,"#282929");
    }

    public void paneHistoryOnMouseExited( ) {
        DashboardStyleConfiguration.setPaneBackground(paneHistory,"transparent");
    }

    public void paneItemsOnMouseEntered() {
        DashboardStyleConfiguration.setPaneBackground(paneItems,"#282929");
    }

    public void paneItemsOnMouseExited() {
        DashboardStyleConfiguration.setPaneBackground(paneItems,"transparent");
    }

    public void paneEmployeeDetailsOnMouseEntered( ) {
        DashboardStyleConfiguration.setPaneBackground(paneEmployeeDetails,"#282929");
    }

    public void paneEmployeeDetailsOnMouseExited( ) {
        DashboardStyleConfiguration.setPaneBackground(paneEmployeeDetails,"transparent");
    }

    public void paneSettingsOnMouseEntered( ) {
        DashboardStyleConfiguration.setPaneBackground(paneSettings,"#282929");
    }

    public void paneSettingsOnMouseExited( ) {
        DashboardStyleConfiguration.setPaneBackground(paneSettings,"transparent");
    }

    public void paneLogOutOnMouseEntered( ) {
        DashboardStyleConfiguration.setPaneBackground(paneLogOut,"#282929");
    }

    public void paneLogOutOnMouseExited( ) {
        DashboardStyleConfiguration.setPaneBackground(paneLogOut,"transparent");
    }
}
