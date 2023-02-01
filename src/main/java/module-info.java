module com.vedruna.proyectoServicios02 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.vedruna.proyectoServicios02 to javafx.fxml;
    exports com.vedruna.proyectoServicios02;
}