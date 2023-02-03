package com.vedruna.proyectoServicios02.cliente;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    public TextField nickname;
    public Label labelError;

    public static DatagramSocket socket;

    {
        try {
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
                int port = 7010;
                InetAddress destino = InetAddress.getByName("localhost");
                byte[] mensajeBytes = nickname.getText().getBytes();
                DatagramPacket envio = new DatagramPacket(mensajeBytes, mensajeBytes.length, destino, port);
                socket.send(envio);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        byte[] bufer = new byte[1024];
        DatagramPacket recibo = new DatagramPacket(bufer, bufer.length);
        try {
            socket.receive(recibo);
            String paquete = new String(recibo.getData()).trim();
            if (paquete.equalsIgnoreCase("ok")) {
                abrirChat();
                System.out.println("deberia abrir");
            } else {
                System.out.println("no se abre");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void abrirChat() {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("chat-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 320, 240);
            Stage stage = new Stage();
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error al abrir chat");
        }

    }
}