package com.vedruna.proyectoServicios02.cliente;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.DatagramPacket;

public class ChatHiloEscuchar implements Runnable {
    @FXML
    private final VBox vbox_message;

    public ChatHiloEscuchar(VBox vbox_message) {
        this.vbox_message = vbox_message;
    }

    @Override
    public void run() {
        escucharMensaje();
        //cierra el socket cuando termina el hilo
        LoginController.socket.close();
        //para el caso de cerrar a través de stop
        Platform.exit();
    }

    // escucha al servidor que le envie mensajes de otros clientes
    private void escucharMensaje() {
        while (true) {
            try {
                byte[] bufer = new byte[1024];
                DatagramPacket recibo = new DatagramPacket(bufer, bufer.length);
                // escuha por su puerto
                LoginController.socket.receive(recibo);
                String paquete = new String(recibo.getData()).trim();
                //si se cierra el chat, acaba el hilo
                if ("desconectado".equals(paquete)) {
                    break;
                }
                ChatController.mostrarMensaje2(paquete, vbox_message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
