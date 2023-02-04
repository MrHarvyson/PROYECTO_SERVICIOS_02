package com.vedruna.proyectoServicios02.cliente;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

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
                //vbox_message.setText(vbox_message.getText() + paquete + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
