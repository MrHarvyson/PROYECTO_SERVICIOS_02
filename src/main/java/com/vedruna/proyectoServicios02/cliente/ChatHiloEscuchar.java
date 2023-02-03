package com.vedruna.proyectoServicios02.cliente;

import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.DatagramPacket;

public class ChatHiloEscuchar implements Runnable{


    private final TextArea mostrador;


    public ChatHiloEscuchar(TextArea mostrador) {
        this.mostrador = mostrador;
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
                LoginController.socket.receive(recibo);
                String paquete = new String(recibo.getData()).trim();
                if ("desconectar".equals(paquete)){
                    break;
                }
                mostrador.setText(mostrador.getText() + paquete + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
