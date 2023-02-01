module com.vedruna.proyecto_servicios_02 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.vedruna.proyecto_servicios_02 to javafx.fxml;
    exports com.vedruna.proyecto_servicios_02;
}