package com.vedruna.proyectoServicios02.servidor;

import com.vedruna.proyectoServicios02.Usuarios;
import com.vedruna.proyectoServicios02.cliente.LoginController;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;

public class ServerChatHilo implements Runnable{

    private List<Usuarios> listaUsuarios;

    public ServerChatHilo(List<Usuarios> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public void run(){
        enviarMensajeAClientes();
    }

    private void enviarMensajeAClientes(){
        DatagramSocket socketRecibo = null;
        try {
            socketRecibo = new DatagramSocket(5010);
            while(true) {
                byte[] bufer = new byte[1024];
                DatagramPacket paqueteRecibido = new DatagramPacket(bufer, bufer.length);
                socketRecibo.receive(paqueteRecibido);

                String mensajeRecibido = new String(paqueteRecibido.getData()).trim();
                int puertoMensajeRecibido = paqueteRecibido.getPort();
                enviarMensajes(mensajeRecibido, puertoMensajeRecibido);
                System.out.println(mensajeRecibido);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socketRecibo != null) {
                socketRecibo.close();
            }
        }
    }

    private void enviarMensajes(String mensajeAEnviar, int puertoMensajeRecibido) throws IOException {
        byte[] data = mensajeAEnviar.getBytes();
        DatagramSocket socketEnvio = new DatagramSocket(6010);

        for (Usuarios usuarios : listaUsuarios) {
            if (!(usuarios.getPuerto() == puertoMensajeRecibido)) {
                DatagramPacket paqueteEnvio = new DatagramPacket(data, data.length, InetAddress.getByName("localhost"), usuarios.getPuerto());
                socketEnvio.send(paqueteEnvio);
                System.out.println("puerto" + usuarios.getPuerto());
            }
        }


        socketEnvio.close();
    }

}
