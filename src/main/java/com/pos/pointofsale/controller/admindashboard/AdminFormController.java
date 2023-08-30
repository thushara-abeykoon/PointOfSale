package com.pos.pointofsale.controller.admindashboard;

import com.pos.pointofsale.controller.cashierdashboard.CashierFormController;
import com.pos.pointofsale.controller.cashierdashboard.DashboardStyleConfiguration;

public class AdminFormController extends CashierFormController {
    @Override
    public void paneDashboardOnMouseClicked(){
        paneLoader("view/CashierDashboardForm.fxml");
        setPaneActive(0);
        DashboardStyleConfiguration.setPaneBackground(paneDashboard,"#282929");
    }
    @Override
    public void paneOrderOnMouseClicked(){
        paneLoader("view/CashierOrderForm.fxml");
        setPaneActive(1);
        DashboardStyleConfiguration.setPaneBackground(paneOrder,"#282929");
    }
    @Override
    public void paneHistoryOnMouseClicked(){
        paneLoader("view/CashierOrderForm.fxml");
        setPaneActive(2);
        DashboardStyleConfiguration.setPaneBackground(paneOrder,"#282929");
    }
    public void paneItemsOnMouseClicked(){
        paneLoader("view/CashierOrderForm.fxml");
        setPaneActive(3);
        DashboardStyleConfiguration.setPaneBackground(paneOrder,"#282929");
    }
    public void paneSettingsOnMouseClicked(){
        paneLoader("view/CashierOrderForm.fxml");
        setPaneActive(4);
        DashboardStyleConfiguration.setPaneBackground(paneOrder,"#282929");
    }
}
