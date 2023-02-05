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
        //cierra el socket cuando termina el hilo
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
                //si se cierra el chat, acaba el hilo
                if ("desconectado".equals(paquete)){
                    break;
                }
                //concatena los mensajes entrantes
                mostrador.setText(mostrador.getText() + paquete + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
