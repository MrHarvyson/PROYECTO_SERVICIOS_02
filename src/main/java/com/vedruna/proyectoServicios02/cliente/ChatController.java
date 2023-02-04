package com.vedruna.proyectoServicios02.cliente;

import com.vedruna.proyectoServicios02.Usuarios;
import com.vedruna.proyectoServicios02.servidor.ServerController;
import com.vedruna.proyectoServicios02.servidor.ServerLoginHilo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    @FXML
    public Button botonEnviar;
    @FXML
    public Label labelNick;
    @FXML
    public TextArea areaMostrador;
    @FXML
    public TextField areaEscritor;

    // cuando el chat se abra se inicia un hilo solo para escuchar lo que le llega del server
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ChatHiloEscuchar chatHiloEscuchar = new ChatHiloEscuchar(areaMostrador);
        Thread hilo = new Thread(chatHiloEscuchar);
        hilo.start();
        //para enviar mensajes con tecla Enter
        areaEscritor.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!(areaEscritor.getText().equals(""))) {
                    String mensaje = areaEscritor.getText();
                    // el mensaje escrito por el chat se enviara al puerto 5010 del server
                    int port = 5010;
                    InetAddress destino = null;
                    DatagramSocket socket = null;
                    try {
                        destino = InetAddress.getByName("localhost");
                        byte[] mensajeBytes = mensaje.getBytes();
                        DatagramPacket envio = new DatagramPacket(mensajeBytes, mensajeBytes.length, destino, port);
                        LoginController.socket.send(envio);
                        mostrarMensaje();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public void enviarMensaje() {
        if (!(areaEscritor.getText().equals(""))) {
            String mensaje = areaEscritor.getText();
            // el mensaje escrito por el chat se enviara al puerto 5010 del server
            int port = 5010;
            InetAddress destino = null;
            DatagramSocket socket = null;
            try {
                destino = InetAddress.getByName("localhost");
                byte[] mensajeBytes = mensaje.getBytes();
                DatagramPacket envio = new DatagramPacket(mensajeBytes, mensajeBytes.length, destino, port);
                LoginController.socket.send(envio);
                mostrarMensaje();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //con esto mostramos el mensaje que ha escrito el usuario a ese mismo usuario
    protected void mostrarMensaje() {
        if (areaEscritor.getText().length() > 0) {
            areaMostrador.setText(areaMostrador.getText() + "Tú: " + areaEscritor.getText() + "\n");
            areaEscritor.setText("");
        }
    }


}