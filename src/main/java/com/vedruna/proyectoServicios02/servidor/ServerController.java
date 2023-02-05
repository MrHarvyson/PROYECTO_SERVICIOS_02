package com.vedruna.proyectoServicios02.servidor;

import com.vedruna.proyectoServicios02.Usuarios;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class ServerController implements Initializable {
    //muestra los mensajes enviados
    public TextArea txtConsola;
    //muestra los usuarios conectados
    public TextArea txtUsuario;
    //FALTA TEXTAREA PARA MOSTRAR LISTA DE CLIENTES
    private final List<Usuarios> listaUsuarios =new LinkedList<Usuarios>();

    // al iniciar el servidor crea los dos hilos
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // un hilo para escuchar los usuarios que se loggean
        ServerLoginHilo serverLoginHilo = new ServerLoginHilo(listaUsuarios, txtUsuario);
        Thread hilo = new Thread(serverLoginHilo);
        hilo.start();

        // otro hilo para mandar mensajes
        ServerChatHilo serverChatHilo = new ServerChatHilo(listaUsuarios, txtConsola);
        Thread hilo2 = new Thread(serverChatHilo);
        hilo2.start();

    }
}