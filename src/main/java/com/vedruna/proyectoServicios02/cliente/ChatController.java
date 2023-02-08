package com.vedruna.proyectoServicios02.cliente;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.media.Media;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.*;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    @FXML
    public ImageView botonEnviar;
    @FXML
    public ImageView enviarImagen;
    @FXML
    public Label labelNombreUsuario;
    @FXML
    private TextField tf_message;
    @FXML
    private VBox vbox_message;
    @FXML
    private ScrollPane sp_main;
    public static DatagramSocket socket;
    public static String nickRecibido;

    // cuando el chat se abra se inicia un hilo solo para escuchar lo que le llega del server
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ChatHiloEscuchar chatHiloEscuchar = new ChatHiloEscuchar(vbox_message);
        Thread hilo = new Thread(chatHiloEscuchar);
        hilo.start();

        labelNombreUsuario.setText(nickRecibido);

        sp_main.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        //para enviar mensajes con tecla Enter
        tf_message.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                enviarMensaje();
            }
        });

        vbox_message.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sp_main.setVvalue((Double) newValue);
            }
        });

    }

    //evento del boton de enviar. Envia mensajes
    public void enviarMensaje() {

        if (!(tf_message.getText().equals(""))) {
            String mensaje = tf_message.getText();
            // el mensaje escrito por el chat se enviara al puerto 5010 del server
            int port = 5010;
            DatagramSocket socket = null;
            try {
                byte[] mensajeBytes = mensaje.getBytes();
                DatagramPacket envio = new DatagramPacket(mensajeBytes, mensajeBytes.length, InetAddress.getByName("localhost"), port);
                LoginController.socket.send(envio);
                mostrarMensaje(mensaje);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void mostrarMensaje(String mensaje) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(mensaje);
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle("-fx-color: rgb(239,242,255);-fx-background-color: rgb(15,125,242);-fx-background-radius: 5px");
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

        textFlow.setStyle("-fx-color: rgb(239,242,255);-fx-background-color: rgb(159,159,159);-fx-background-radius: 5px;-fx-text-fill:rgb(15,125,242) ");
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

        String path = "src/main/resources/com/vedruna/proyectoServicios02/musica/tono.mp3";
        Media media = new Media(new File(path).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();

    }

    public void cerrar(MouseEvent e){
        LoginController.eliminarClienteServidor();
        LoginController.cerrarHiloCliente();
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    //SOLO FALTA BOTON PARA BUSCAR IMAGEN
    public void enviarImagen() {
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