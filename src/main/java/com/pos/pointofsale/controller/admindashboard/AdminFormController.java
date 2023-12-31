package com.pos.pointofsale.controller.admindashboard;

import com.pos.pointofsale.controller.cashierdashboard.CashierFormController;
import com.pos.pointofsale.controller.cashierdashboard.DashboardStyleConfiguration;

public class AdminFormController extends CashierFormController {
    @Override
    public void initialize() {
        super.initialize();
        paneLoader("view/AdminDashboardForm.fxml");
        setPaneActive(0);
        DashboardStyleConfiguration.setPaneBackground(paneDashboard,"#282929");
    }

    @Override
    public void paneDashboardOnMouseClicked(){
        paneLoader("view/AdminDashboardForm.fxml");
        setPaneActive(0);
        DashboardStyleConfiguration.setPaneBackground(paneDashboard,"#282929");
    }
    @Override
    public void paneOrderOnMouseClicked(){
        paneLoader("view/AdminEmployeesForm.fxml");
        setPaneActive(1);
        DashboardStyleConfiguration.setPaneBackground(paneOrder,"#282929");
    }
    @Override
    public void paneHistoryOnMouseClicked(){
        paneLoader("view/AdminHistoryForm.fxml");
        setPaneActive(2);
        DashboardStyleConfiguration.setPaneBackground(paneHistory,"#282929");
    }

    @Override
    public void paneLogOutOnMoseClicked() {
        super.paneLogOutOnMoseClicked();
        AdminDashboardFormController.adminRevenueUpdater1.cancel();
        AdminDashboardFormController.adminRevenueUpdater2.cancel();
        AdminDashboardFormController.adminTotalOrdersUpdater.cancel();
        AdminDashboardFormController.timer1.purge();
        AdminDashboardFormController.timer2.purge();
    }
}
