module com.example.ims_app {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.apache.commons.csv;

    opens com.example.ims_app to javafx.fxml;
    exports com.example.ims_app;
}