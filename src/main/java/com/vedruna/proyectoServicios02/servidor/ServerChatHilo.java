package com.vedruna.proyectoServicios02.servidor;

import com.vedruna.proyectoServicios02.Usuarios;
import com.vedruna.proyectoServicios02.cliente.LoginController;
import javafx.scene.control.TextArea;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;

public class ServerChatHilo implements Runnable{

    public TextArea txtConsola;

    private List<Usuarios> listaUsuarios;

    public ServerChatHilo(List<Usuarios> listaUsuarios, TextArea txtConsola) {
        this.listaUsuarios = listaUsuarios;
        this.txtConsola = txtConsola;
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
                if(mensajeRecibido.equalsIgnoreCase("stop")) {
                    cerrarServer();
                    break;
                } else if (esImagen(mensajeRecibido)) {
                    txtConsola.setText(txtConsola.getText() + "Llegó una imagen a Descargas" + "\n");
                    String rutaImagen = System.getProperty("user.home") + "\\Downloads\\" ;
                    FileOutputStream fileOutputStream = new FileOutputStream(rutaImagen + "imagenRecibida.png");
                    fileOutputStream.write(paqueteRecibido.getData());
                    //fileOutputStream.close();   CERRAMOS FLUJO
                } else { //si no es un mensaje de cierre o imagen, reenvia los mensajes
                    //obtenemos nick del cliente que envia
                    String clienteNick = obtenerNick(puertoMensajeRecibido);
                    //concatenamos el nick al mensaje para enviarlo al resto de clientes
                    String mensajeFinal = clienteNick + ": " + mensajeRecibido;
                    //enviamos el mensaje
                    enviarMensajes(mensajeFinal, puertoMensajeRecibido);
                    txtConsola.setText(txtConsola.getText() + mensajeFinal + "\n");
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

    private boolean esImagen(String mensaje) {
        return mensaje.contains("�PNG") || mensaje.contains("�JPG");
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
                DatagramPacket paqueteEnvio = new DatagramPacket(data, data.length, usuarios.getDireccion(), usuarios.getPuerto());
                socketEnvio.send(paqueteEnvio);
            }
        }
        socketEnvio.close();
    }

    //cierra el servidor en caso de Stop
    private void cerrarServer() {
        DatagramSocket socketEnvio = null;
        try {
            String mensajeFinal = "Cerrando Servidor";
            byte[] data = (mensajeFinal).getBytes();
            socketEnvio = new DatagramSocket(1060);

            //envía a los clientes el mensaje de que se ha cerrado Server
            for (Usuarios usuario : listaUsuarios) {
                DatagramPacket packet = new DatagramPacket(data, data.length, usuario.getDireccion(), usuario.getPuerto());
                socketEnvio.send(packet);
            }

            //manda al ServerLoginHilo el mensaje para cerrarlo
            DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName("localhost") , 7010);
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
