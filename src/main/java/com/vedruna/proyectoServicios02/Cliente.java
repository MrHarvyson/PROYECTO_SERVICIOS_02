package com.vedruna.proyectoServicios02;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        cliente();
    }

    private static void cliente() {
        InetAddress destino = null;
        while(true){
            try {
                int port = 5000; // Puerto por el que escucha
                destino = InetAddress.getByName("localhost");
                Scanner entrada = new Scanner(System.in);
                String Saludo = entrada.nextLine();
                byte[] mensaje = Saludo.getBytes(); //codificarlo a bytes para enviarlo
                //construyo el datagrama a enviar
                DatagramPacket envio = new DatagramPacket(mensaje, mensaje.length, destino, 5010);
                DatagramSocket socket = new DatagramSocket(5000); // puerto local que no tiene que ver nada con destino
                socket.send(envio);//envio datagrama a destino y port
                socket.close();
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            } catch (SocketException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }
}
