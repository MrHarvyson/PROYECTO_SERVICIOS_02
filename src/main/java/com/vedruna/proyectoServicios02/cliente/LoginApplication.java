package com.vedruna.proyectoServicios02.cliente;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 300, 400);
        stage.setResizable(false);
        //stage.setTitle(LoginController.nickname.getText());
        stage.setScene(scene);
        stage.getIcons().add(new Image("log.png"));
        stage.show();
        //cierra el socket en caso de salirnos desde la primera ventana
        stage.setOnCloseRequest(windowEvent -> {
            LoginController.socket.close();
        });
    }

    public static void main(String[] args) {
        launch();
    }
}