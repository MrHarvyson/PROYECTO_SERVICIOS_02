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

                //si recibe de un cliente stop, sale del bucle y cierra el servidor
                if(mensajeRecibido.equalsIgnoreCase("stop")){
                    cerrarServer();
                    break;
                } else { //si no es un mensaje de cierre, reenvia los mensajes
                    //obtenemos nick del cliente que envia
                    String clienteNick = obtenerNick(puertoMensajeRecibido);
                    //concatenamos el nick al mensaje para enviarlo al resto de clientes
                    String mensajeFinal = clienteNick + ": " + mensajeRecibido;
                    //enviamos el mensaje
                    enviarMensajes(mensajeFinal, puertoMensajeRecibido);

                    //FALTA METER EL TEXTAREA PARA MOSTRARLO POR PANTALLA
                    System.out.println(mensajeRecibido);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socketRecibo != null) {
                socketRecibo.close();
            }
        }
    }


    private String obtenerNick(int port) {
        String nickCliente = "";
        for (Usuarios usuario : listaUsuarios) {
            if (port == usuario.getPuerto()) {
                nickCliente = usuario.getNombre();
                break;
            }
        }

        return nickCliente;
    }

    private void enviarMensajes(String mensajeEnvio, int puertoMensajeRecibido) throws IOException {
        byte[] data = mensajeEnvio.getBytes();
        DatagramSocket socketEnvio = new DatagramSocket(6010);

        for (Usuarios usuarios : listaUsuarios) {
            if (!(usuarios.getPuerto() == puertoMensajeRecibido)) {
                DatagramPacket paqueteEnvio = new DatagramPacket(data, data.length, InetAddress.getByName("localhost"), usuarios.getPuerto());
                socketEnvio.send(paqueteEnvio);
                System.out.println("puerto que recibe " + usuarios.getPuerto());
            }
        }
        socketEnvio.close();
    }



    //cierra el servidor en caso de Stop
    private void cerrarServer() {
        DatagramSocket socketEnvio = null;
        try {
            String mensajeFinal = "Servidor Cerrado";
            byte[] data = (mensajeFinal).getBytes();
            socketEnvio = new DatagramSocket(1060);

            //envía a los clientes el mensaje de que se ha cerrado Server
            for (Usuarios usuario : listaUsuarios) {
                InetAddress direccionCliente = usuario.getDireccion();
                int puertoCliente = usuario.getPuerto();
                DatagramPacket packet = new DatagramPacket(data, data.length, direccionCliente, puertoCliente);
                socketEnvio.send(packet);
            }

            //manda al ServerLoginHilo el mensaje para cerrarlo
            InetAddress inetAddress = InetAddress.getByName("localhost");
            DatagramPacket packet = new DatagramPacket(data, data.length, inetAddress , 7010);
            socketEnvio.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socketEnvio != null) {
                socketEnvio.close();
            }
        }
    }

}
