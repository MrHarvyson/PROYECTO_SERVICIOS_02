package com.vedruna.proyectoServicios02.cliente;

import com.vedruna.proyectoServicios02.Usuarios;
import com.vedruna.proyectoServicios02.servidor.ServerController;
import com.vedruna.proyectoServicios02.servidor.ServerLoginHilo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    @FXML
    public Button botonEnviar;
    @FXML
    //para mostrar en la interfaz de que cliente se trata
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
                enviarMensaje();
            }
        });
    }

    //evento del boton de enviar. Envia mensajes
    public void enviarMensaje() {
        if (!(areaEscritor.getText().equals(""))) {
            String mensaje = areaEscritor.getText();
            // el mensaje escrito por el chat se enviara al puerto 5010 del server
            int port = 5010;
            DatagramSocket socket = null;
            try {
                byte[] mensajeBytes = mensaje.getBytes();
                DatagramPacket envio = new DatagramPacket(mensajeBytes, mensajeBytes.length, InetAddress.getByName("localhost"), port);
                LoginController.socket.send(envio);
                mostrarMensaje();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //con esto mostramos el mensaje que ha escrito el usuario a ese mismo usuario
    protected void mostrarMensaje() {
        if (!(areaEscritor.getText().equals(""))) {
            areaMostrador.setText(areaMostrador.getText() + "Tú: " + areaEscritor.getText() + "\n");
            areaEscritor.setText("");
        }
    }

    //SOLO FALTA BOTON PARA BUSCAR IMAGEN
    public void enviarImagen(){
        FileChooser selectorArchivos = new FileChooser();
        selectorArchivos.setTitle("Enviar Imagen");
        selectorArchivos.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg")
        );
        Stage stage = new Stage();
        Button seleccion = new Button();
        VBox vBox = new VBox(seleccion);
        Scene scene = new Scene(vBox, 960, 600);
        stage.setScene(scene);
        File imagen = selectorArchivos.showOpenDialog(stage);
        if (imagen != null) {
            FileInputStream imagenAEnviar = null;
            try {
                imagenAEnviar = new FileInputStream(imagen);
                byte[] imagenBytes = imagenAEnviar.readAllBytes();
                int port = 5010;
                InetAddress destino = InetAddress.getByName("localhost");
                DatagramPacket envio = new DatagramPacket(imagenBytes, imagenBytes.length, destino, port);
                LoginController.socket.send(envio);
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No se pudo enviar imagen");
                alert.setHeaderText(null);
                alert.setContentText("No puedes enviar esta imagen. Elige una más pequeña.");
                alert.showAndWait();
            } finally {
                if (imagenAEnviar != null) {
                    try {
                        imagenAEnviar.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}