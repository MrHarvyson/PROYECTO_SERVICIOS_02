package com.vedruna.proyectoServicios02.cliente;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.DatagramPacket;

public class ChatHiloEscuchar implements Runnable{
    @FXML
    private final VBox vbox_message;



    public ChatHiloEscuchar(VBox vbox_message) {
        this.vbox_message = vbox_message;
    }
    @Override
    public void run() {
        escucharMensaje();
        LoginController.socket.close();
    }

    private void escucharMensaje() {
        while (true) {
            try {
                byte[] bufer = new byte[1024];
                DatagramPacket recibo = new DatagramPacket(bufer, bufer.length);
                // escuha por su puerto
                LoginController.socket.receive(recibo);
                String paquete = new String(recibo.getData()).trim();
                if ("desconectar".equals(paquete)){
                    break;
                }
                ChatController.mostrarMensaje2(paquete,vbox_message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
