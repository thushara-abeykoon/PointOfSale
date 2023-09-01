module com.pos.pointofsale {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.materialicons;
    requires java.sql;
    requires mysql.connector.j;
    requires org.controlsfx.controls;
    requires jasperreports;
    requires java.desktop;

    opens com.pos.pointofsale.model to javafx.fxml;
    opens com.pos.pointofsale to javafx.fxml;
    exports com.pos.pointofsale.model;
    exports com.pos.pointofsale;
    exports com.pos.pointofsale.controller;
    opens com.pos.pointofsale.controller to javafx.fxml;
    exports com.pos.pointofsale.controller.registerformcontroller;
    opens com.pos.pointofsale.controller.registerformcontroller to javafx.fxml;
    exports com.pos.pointofsale.controller.cashierdashboard;
    opens com.pos.pointofsale.controller.cashierdashboard to javafx.fxml;
    opens com.pos.pointofsale.controller.admindashboard to javafx.fxml;
    exports com.pos.pointofsale.controller.admindashboard;

}