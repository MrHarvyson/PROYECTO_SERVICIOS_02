package com.vedruna.proyectoServicios02.servidor;

import com.vedruna.proyectoServicios02.Usuarios;
import com.vedruna.proyectoServicios02.cliente.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.*;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class ServerController implements Initializable {
    //muestra los mensajes enviados
    @FXML
    public TextArea txtSistema, txtUsuario;
    //muestra los usuarios conectados
    @FXML
    public AnchorPane ApSistema,ApUsuario,ApTotal;
    public boolean verSistema = true;
    public boolean verUsuario = true;


    //FALTA TEXTAREA PARA MOSTRAR LISTA DE CLIENTES
    private final List<Usuarios> listaUsuarios =new LinkedList<Usuarios>();
    @FXML
    public ImageView sistema,usuario,cerrar,minimizar;

    // al iniciar el servidor crea los dos hilos
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtSistema.setText("<<<<<-----   SERVIDOR LEVANTADO   ----->>>>>\n");

        // un hilo para escuchar los usuarios que se loggean
        ServerLoginHilo serverLoginHilo = new ServerLoginHilo(listaUsuarios, txtUsuario);
        Thread hilo = new Thread(serverLoginHilo);
        hilo.start();

        // otro hilo para mandar mensajes
        ServerChatHilo serverChatHilo = new ServerChatHilo(listaUsuarios, txtSistema);
        Thread hilo2 = new Thread(serverChatHilo);
        hilo2.start();

    }

    public void clickSistema(MouseEvent mouseEvent) {
        if(!verSistema){
            ApSistema.setVisible(false);
            verSistema = true;
        }else{
            ApSistema.setVisible(true);
            ApUsuario.setVisible(false);
            verSistema = false;
            verUsuario = true;
        }

    }

    public void clickUsuario(MouseEvent mouseEvent) {
        if(!verUsuario){
            ApUsuario.setVisible(false);
            verUsuario = true;
        }else{
            ApUsuario.setVisible(true);
            ApSistema.setVisible(false);
            verSistema = true;
            verUsuario = false;
        }
    }

    public void clickCerrar(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

}