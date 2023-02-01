package com.vedruna.proyectoServicios02;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Servidor {
    public static void main(String[] args) {
        servidor();
    }

    private static void servidor() {

        // el mensaje
        byte[] bufer = new byte[1024];

        // puerto por el que recibe como para escuchar
        DatagramSocket socket;
        try {
            socket = new DatagramSocket(5010);
            // paquete del mensaje
            DatagramPacket recibo = new DatagramPacket(bufer, bufer.length);
            System.out.println("Server: esperando Datagrama .......... ");
            while (true) {
                // lo que recibe por el socket
                socket.receive(recibo);
                int bytesRec = recibo.getLength();//obtengo numero de bytes
                String paquete = new String(recibo.getData());//obtengo String
                System.out.println(paquete);
                //break;
            }

            //socket.close();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
