module com.pos.pointofsale {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.materialicons;


    opens com.pos.pointofsale to javafx.fxml;
    exports com.pos.pointofsale;
    exports com.pos.pointofsale.controller;
    opens com.pos.pointofsale.controller to javafx.fxml;
}