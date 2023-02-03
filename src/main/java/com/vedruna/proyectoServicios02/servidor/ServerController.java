package com.vedruna.proyectoServicios02.servidor;

import com.vedruna.proyectoServicios02.Usuarios;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class ServerController implements Initializable {

    private final List<Usuarios> listaUsuarios =new LinkedList<Usuarios>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ServerLoginHilo serverLoginHilo = new ServerLoginHilo(listaUsuarios);
        Thread hilo = new Thread(serverLoginHilo);
        hilo.start();

        ServerChatHilo serverChatHilo = new ServerChatHilo(listaUsuarios);
        Thread hilo2 = new Thread(serverChatHilo);
        hilo2.start();

    }
}