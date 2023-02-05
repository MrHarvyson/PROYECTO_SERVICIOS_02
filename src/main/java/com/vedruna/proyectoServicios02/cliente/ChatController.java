package com.vedruna.proyectoServicios02.cliente;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.media.Media;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    @FXML
    public Button botonEnviar;
    @FXML
    private TextField tf_message;
    @FXML
    private VBox vbox_message;
    @FXML
    private ScrollPane sp_main;

    // cuando el chat se abra se inicia un hilo solo para escuchar lo que le llega del server
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ChatHiloEscuchar chatHiloEscuchar = new ChatHiloEscuchar(vbox_message);
        Thread hilo = new Thread(chatHiloEscuchar);
        hilo.start();

        sp_main.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        //para enviar mensajes con tecla Enter
        tf_message.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!(tf_message.getText().equals(""))) {
                    String mensaje = tf_message.getText();
                    // el mensaje escrito por el chat se enviara al puerto 5010 del server
                    int port = 5010;
                    InetAddress destino = null;
                    DatagramSocket socket = null;
                    try {
                        destino = InetAddress.getByName("localhost");
                        byte[] mensajeBytes = mensaje.getBytes();
                        DatagramPacket envio = new DatagramPacket(mensajeBytes, mensajeBytes.length, destino, port);
                        LoginController.socket.send(envio);
                        mostrarMensaje(mensaje);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        vbox_message.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sp_main.setVvalue((Double) newValue);
            }
        });

    }

    public void enviarMensaje() {

        if (!(tf_message.getText().equals(""))) {
            String mensaje = tf_message.getText();
            // el mensaje escrito por el chat se enviara al puerto 5010 del server
            int port = 5010;
            InetAddress destino = null;
            DatagramSocket socket = null;
            try {
                destino = InetAddress.getByName("localhost");
                byte[] mensajeBytes = mensaje.getBytes();
                DatagramPacket envio = new DatagramPacket(mensajeBytes, mensajeBytes.length, destino, port);
                LoginController.socket.send(envio);
                mostrarMensaje(mensaje);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //con esto mostramos el mensaje que ha escrito el usuario a ese mismo usuario
    public void mostrarMensaje(String mensaje) {
        String mess = tf_message.getText();
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(mess);
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle("-fx-color: rgb(239,242,255);-fx-background-color: rgb(15,125,242);-fx-background-radius: 20px");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(0.934, 0.945, 0.996));

        hBox.getChildren().add(textFlow);
        vbox_message.getChildren().add(hBox);

        tf_message.clear();
    }

    public static void mostrarMensaje2(String paquete, VBox vbox) {
        play();
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(paquete);
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle("-fx-color: rgb(239,242,255);-fx-background-color: rgb(159,159,159);-fx-background-radius: 20px;-fx-text-fill:rgb(15,125,242) ");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(0.934, 0.945, 0.996));

        hBox.getChildren().add(textFlow);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vbox.getChildren().add(hBox);
            }
        });
    }

    // musicon
    public static void play() {
        String path = "C:\\Users\\josec\\Documents\\Git\\PROYECTO_SERVICIOS_02\\src\\main\\img\\tono.mp3";
        Media media = new Media(new File(path).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

}