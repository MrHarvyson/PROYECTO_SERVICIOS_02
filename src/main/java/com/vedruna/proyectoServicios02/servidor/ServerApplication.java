package com.vedruna.proyectoServicios02.servidor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerApplication extends Application {
    private double x, y;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ServerApplication.class.getResource("server-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        stage.setTitle("Hello!");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        scene.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });

        //si pulsamos el botón de cerrar ventana, cierra el server
        stage.setOnCloseRequest(windowEvent -> {
            cerrarServidor();
        });
    }

    //manda mensaje al hilo para que se cierre
    private void cerrarServidor() {
        DatagramSocket socketEnvio = null;
        try {
            socketEnvio = new DatagramSocket();
            int port = 5010;
            InetAddress inetAddress = InetAddress.getByName("localhost");
            //al mandar stop, saca a ServerChatHilo del bucle
            String mensaje = "Stop";
            byte[] data = mensaje.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, inetAddress, port);
            socketEnvio.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socketEnvio != null) {
                socketEnvio.close();
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}