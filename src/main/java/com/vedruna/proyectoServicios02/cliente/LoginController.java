package com.vedruna.proyectoServicios02.cliente;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class LoginController {

    @FXML
    public Button buttonLogin;
    @FXML
    public TextField nickname;
    @FXML
    public Label labelError;

    public static DatagramSocket socket;

    static {
        try {
            // asignara un puerto aleatorio de envio del login al servidor
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void botonLogin(ActionEvent actionEvent) {
        if (nickname.getText().equals("")) {
            System.out.println("introduce");
        } else {
            try {
                // el login envia al puerto 7010 lso datos del cliente, el server esta escuchando por el
                int port = 7010;
                InetAddress destino = InetAddress.getByName("localhost");
                byte[] mensajeBytes = nickname.getText().getBytes();
                DatagramPacket envio = new DatagramPacket(mensajeBytes, mensajeBytes.length, destino, port);
                socket.send(envio);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // lee el mensaje que envia servidor para dar respuesta de comprobacion
        byte[] bufer = new byte[1024];
        DatagramPacket recibo = new DatagramPacket(bufer, bufer.length);
        try {
            socket.receive(recibo);
            String paquete = new String(recibo.getData()).trim();
            if (paquete.equalsIgnoreCase("ok")) {
                // si el nombre no se repite y el server nos da el ok abre la pantalla de chat y cierra el login
                abrirChat();
                cerrarPantalla(actionEvent);
                System.out.println("deberia abrir");
            } else {
                System.out.println("no se abre");
            }
        } catch (IOException e) {
            System.out.println("Error al enviar nick.");
        }

    }

    private void abrirChat() {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("chat-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 300, 400);
            Stage stage = new Stage();
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error al abrir chat");
        }

    }

    private void cerrarPantalla(ActionEvent e) {
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}