package com.vedruna.proyectoServicios02.cliente;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    private double x, y;
    public static DatagramSocket socket;

    static {
        try {
            // asignara un puerto aleatorio de envio del login al servidor
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    // login para crear usuario
    public void botonLogin(ActionEvent actionEvent) {
        if (nickname.getText().equals("")) {
            labelError.setText("Introduce un nick");
        } else {
            try {
                // el login envia al puerto 7010 lso datos del cliente, el server esta escuchando por el
                int port = 7010;
                InetAddress destino = InetAddress.getByName("localhost");
                byte[] mensajeBytes = nickname.getText().getBytes();
                DatagramPacket envio = new DatagramPacket(mensajeBytes, mensajeBytes.length, destino, port);
                socket.send(envio);
                // lee el mensaje que envia servidor para dar respuesta de comprobacion
                byte[] bufer = new byte[1024];
                DatagramPacket recibo = new DatagramPacket(bufer, bufer.length);
                socket.receive(recibo);
                String paquete = new String(recibo.getData()).trim();
                if (paquete.equalsIgnoreCase("ok")) {
                    // si el nombre no se repite y el server nos da el ok abre la pantalla de chat y cierra el login
                    ChatController.nickRecibido = nickname.getText();
                    abrirChat();
                    cerrarPantalla(actionEvent);
                } else {
                    nickname.setText("");
                    labelError.setText("Ese nick ya existe");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // es caso todo correcto entra siguiente ventana
    private void abrirChat() {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("chat-view.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setTitle(nickname.getText().toUpperCase());
            stage.getIcons().add(new Image("log.png"));
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
            stage.setResizable(false);
            stage.show();

            scene.setOnMousePressed(event -> {
                x = event.getSceneX();
                y = event.getSceneY();
            });

            scene.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
            });
            stage.show();
            //para cerrar el clientem al pulsar X
            stage.setOnCloseRequest(windowEvent -> {
                //elimina el cliente desconectado de la lista del Server
                eliminarClienteServidor();
                //cierra el hilo de Escuchar del cliente
                cerrarHiloCliente();
                socket.close();
            });
        } catch (IOException e) {
            System.out.println("Error al abrir chat");
        }

    }


    private void cerrarPantalla(ActionEvent e) {
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public static void eliminarClienteServidor() {
        try {
            int port = 7010;
            String desconectar = "desconectado";
            byte[] mensajeBytes = desconectar.getBytes();
            DatagramPacket envio = new DatagramPacket(mensajeBytes, mensajeBytes.length, InetAddress.getByName("localhost"), port);
            socket.send(envio);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void cerrarHiloCliente() {
        try {
            String desconectar = "desconectado";
            byte[] mensajeBytes = desconectar.getBytes();
            DatagramPacket envio = new DatagramPacket(mensajeBytes, mensajeBytes.length, InetAddress.getByName("localhost"), socket.getLocalPort());
            socket.send(envio);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void cerrar(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}