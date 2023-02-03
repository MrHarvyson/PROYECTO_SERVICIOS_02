module com.vedruna.proyectoServicios02 {
    requires javafx.controls;
    requires javafx.fxml;


    exports com.vedruna.proyectoServicios02.cliente;
    opens com.vedruna.proyectoServicios02.cliente to javafx.fxml;
    exports com.vedruna.proyectoServicios02.servidor;
    opens com.vedruna.proyectoServicios02.servidor to javafx.fxml;
}