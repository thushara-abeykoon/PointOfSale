package com.pos.pointofsale.controller.cashierdashboard;

import com.pos.pointofsale.StageController;
import com.pos.pointofsale.controller.LoginFormController;
import com.pos.pointofsale.database.DatabaseConnector;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

public class CashierDashboardController extends DashboardStyleConfiguration {

    public VBox vboxLeft;
    public ImageView imgMore;
    public Pane paneDashboard;
    public Pane paneOrder;
    public Pane paneHistory;
    public Pane paneItems;
    public Pane paneEmployeeDetails;
    public Pane paneSettings;
    public Pane paneLogOut;
    public Pane paneEmpty;
    public Label lblDashboard;
    public Label lblOrder;
    public Label lblHistory;
    public Label lblItems;
    public Label lblEmployeeDetails;
    public Label lblSettings;
    public Label lblLogout;
    public static boolean isOptionVisible = false;
    public BorderPane root;
    private final boolean[] tabsLoaded = new boolean[6];
    public String logId = LoginFormController.logId;
    public static String empId = LoginFormController.empId;

    public void initialize(){
        setPaneActive(0);
        DashboardStyleConfiguration.setPaneBackground(paneDashboard,"#282929");
        paneLoader("view/DashboardForm.fxml");
    }


    public void imgMoreOnMouseClicked() {
        if (!isOptionVisible){
            Timeline timeline = animateVbox(0.15, 220);
            timeline.setOnFinished(event -> setVisibleOptions(true));
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

    public void setPaneActive(int index){
        Arrays.fill(tabsLoaded,false);
        DashboardStyleConfiguration.setPaneBackground(paneDashboard,"transparent");
        DashboardStyleConfiguration.setPaneBackground(paneOrder,"transparent");
        DashboardStyleConfiguration.setPaneBackground(paneHistory,"transparent");
        DashboardStyleConfiguration.setPaneBackground(paneItems,"transparent");
        DashboardStyleConfiguration.setPaneBackground(paneEmployeeDetails,"transparent");
        DashboardStyleConfiguration.setPaneBackground(paneSettings,"transparent");
        tabsLoaded[index] = true;
    }

    public void paneDashboardOnMouseClicked( ) {
        paneLoader("view/DashboardForm.fxml");
        setPaneActive(0);
        DashboardStyleConfiguration.setPaneBackground(paneDashboard,"#282929");

    }

    public void paneOrderOnMouseClicked( ) {
        paneLoader("view/OrderForm.fxml");
        setPaneActive(1);
        DashboardStyleConfiguration.setPaneBackground(paneOrder,"#282929");
    }

    public void paneHistoryOnMouseClicked( ) {
        paneLoader("view/HistoryForm.fxml");
        setPaneActive(2);
        DashboardStyleConfiguration.setPaneBackground(paneHistory,"#282929");
    }

    public void paneItemsOnMouseClicked( ) {
        paneLoader("view/ItemsForm.fxml");
        setPaneActive(3);
        DashboardStyleConfiguration.setPaneBackground(paneItems,"#282929");
    }

    public void paneEmployeeDetailsOnMouseClicked( ) {
        paneLoader("view/EmployeeDetailsForm.fxml");
        setPaneActive(4);
        DashboardStyleConfiguration.setPaneBackground(paneEmployeeDetails,"#282929");
    }

    public void paneSettingsOnMouseClicked( ) {
        paneLoader("view/SettingsForm.fxml");
        setPaneActive(5);
        DashboardStyleConfiguration.setPaneBackground(paneSettings,"#282929");
    }

    public void paneLogOutOnMoseClicked( ) {
        onLogout();
    }

    public void onLogout(){
        Connection connection = DatabaseConnector.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE employee_computer SET logout = current_timestamp WHERE log_id = ?;");
            preparedStatement.setObject(1,Integer.parseInt(logId));
            int status = preparedStatement.executeUpdate();
            if (status>0){
                System.out.println(root);
                StageController.closeStage(root);
                StageController stageController = new StageController();
                Stage stage = stageController.loadStage("view/LoginForm.fxml", "Login");
                stage.initStyle(StageStyle.UNDECORATED);
                stage.centerOnScreen();
                stage.show();
            }

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void paneLoader(String fxml){
        StageController stageController = new StageController();
        try {
            Parent parent = stageController.loadScene(fxml).getRoot();
            root.setCenter(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Mouse Hover Settings

    public void paneDashboardOnMouseEntered( ) {
        setPaneBackground(paneDashboard,"#282929");
    }

    public void paneDashboardOnMouseExited( ) {
        if(tabsLoaded[0])
            return;
        setPaneBackground(paneDashboard,"transparent");
    }

    public void paneOrderOnMouseEntered( ) {
        setPaneBackground(paneOrder,"#282929");
    }

    public void paneOrderOnMouseExited( ) {
        if (tabsLoaded[1])
            return;
        DashboardStyleConfiguration.setPaneBackground(paneOrder,"transparent");
    }

    public void paneHistoryOnMouseEntered( ) {
        DashboardStyleConfiguration.setPaneBackground(paneHistory,"#282929");
    }

    public void paneHistoryOnMouseExited( ) {
        if (tabsLoaded[2])
            return;
        DashboardStyleConfiguration.setPaneBackground(paneHistory,"transparent");
    }

    public void paneItemsOnMouseEntered() {
        DashboardStyleConfiguration.setPaneBackground(paneItems,"#282929");
    }

    public void paneItemsOnMouseExited() {
        if (tabsLoaded[3])
            return;
        DashboardStyleConfiguration.setPaneBackground(paneItems,"transparent");
    }

    public void paneEmployeeDetailsOnMouseEntered( ) {
        DashboardStyleConfiguration.setPaneBackground(paneEmployeeDetails,"#282929");
    }

    public void paneEmployeeDetailsOnMouseExited( ) {
        if (tabsLoaded[4])
            return;
        DashboardStyleConfiguration.setPaneBackground(paneEmployeeDetails,"transparent");
    }

    public void paneSettingsOnMouseEntered( ) {
        DashboardStyleConfiguration.setPaneBackground(paneSettings,"#282929");
    }

    public void paneSettingsOnMouseExited( ) {
        if (tabsLoaded[5])
            return;
        DashboardStyleConfiguration.setPaneBackground(paneSettings,"transparent");
    }

    public void paneLogOutOnMouseEntered( ) {
        DashboardStyleConfiguration.setPaneBackground(paneLogOut,"#282929");
    }

    public void paneLogOutOnMouseExited( ) {
        DashboardStyleConfiguration.setPaneBackground(paneLogOut,"transparent");
    }
}
