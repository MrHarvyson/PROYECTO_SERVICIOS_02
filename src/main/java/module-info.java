module com.vedruna.proyectoServicios {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;


    exports com.vedruna.proyectoServicios02.cliente;
    opens com.vedruna.proyectoServicios02.cliente to javafx.fxml;
    exports com.vedruna.proyectoServicios02.servidor;
    opens com.vedruna.proyectoServicios02.servidor to javafx.fxml;
    exports com.vedruna.proyectoServicios02;
}