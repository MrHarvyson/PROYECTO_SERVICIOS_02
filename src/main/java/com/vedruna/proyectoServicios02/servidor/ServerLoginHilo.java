package com.vedruna.proyectoServicios02.servidor;
import com.vedruna.proyectoServicios02.Usuarios;
import com.vedruna.proyectoServicios02.cliente.ChatController;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

public class ServerLoginHilo implements Runnable {

    private final TextArea txtUsuario;
    private final TextArea txtSistema;
    private final List<Usuarios> listaUsuarios;

    public ServerLoginHilo(List<Usuarios> listaUsuarios, TextArea txtUsuario, TextArea txtSistema) {
        this.listaUsuarios = listaUsuarios;
        this.txtUsuario = txtUsuario;
        this.txtSistema = txtSistema;
    }

    @Override
    public void run() {
        try {
            //Crea usuarios o cierra hilo
            crearUsuarios();
            //Cuando termina el hilo, cierra ventana
            Platform.exit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // crea un usuario en el caso de no existir
    private void crearUsuarios() throws IOException {
        // socket por el que recibe peticion de nuevo usuario del login(cliente)
        DatagramSocket socketUsuario = new DatagramSocket(7010);
        while (true) {
            byte[] bufer = new byte[1024];
            DatagramPacket usuarioPacket = new DatagramPacket(bufer, bufer.length);
            socketUsuario.receive(usuarioPacket);
            //almacenamos el mensaje que recibimos
            String mensajeRecibido = new String(usuarioPacket.getData()).trim();

            if (mensajeRecibido.equals("Cerrando Servidor")){
                break;
            } else if (mensajeRecibido.equalsIgnoreCase("desconectado")){
                txtSistema.setText(txtSistema.getText() + "[" + usuarioPacket.getSocketAddress() + "] " + sacarUsuario(usuarioPacket.getPort()) + " se ha desconectado."+ "\n");
                cerrarCliente(usuarioPacket);
            } else {
                // nos llega la informacion del login del cliente y la enviamos
                // para crear un usuario nuevo
                anadirUsuario(usuarioPacket);
            }

            txtUsuario.setText("");
            for (Usuarios usuario : listaUsuarios) {
                txtUsuario.setText(txtUsuario.getText() + usuario.getNombre() + "\n");
            }
        }
        socketUsuario.close();
    }

    private void anadirUsuario(DatagramPacket recibo) {
        // obtenermos los datos del datagramPacket
        String nombreUsuario = new String(recibo.getData()).trim();
        InetAddress direccion = recibo.getAddress();
        int puerto = recibo.getPort();

        String mensaje;
        // comprobamos si existe el usuario ya
        if(!comprobarUsuario(nombreUsuario)){
            Usuarios usuario = new Usuarios(direccion, puerto, nombreUsuario);
            listaUsuarios.add(usuario);
            mensaje = "ok";
        } else {
            mensaje = "noOk";
        }
        // enviamos al cliente la comprobacion de que el usuario sea correcto
        envioComprobacion(mensaje, recibo);
    }

    private void envioComprobacion(String mensaje,DatagramPacket recibo) {
        byte[] data = mensaje.getBytes();
        DatagramSocket socketEnvio = null;
        try {
            // enviamos comprobacion por el puerto 8010
            socketEnvio = new DatagramSocket(8010);
            // enviamos a la direccion y puerto del que nos llego el mensaje
            DatagramPacket packet = new DatagramPacket(data, data.length, recibo.getAddress(), recibo.getPort());
            socketEnvio.send(packet);
            socketEnvio.close();
        } catch (IOException e) {
            System.out.println("Error al enviar comprobación.");
        }
    }

    private boolean comprobarUsuario(String nombreUsuario) {
        boolean existe = false;
        for (Usuarios usuario : listaUsuarios) {
            if (nombreUsuario.equals(usuario.getNombre())) {
                existe = true;
                break;
            }
        }
        return existe;
    }

    private void cerrarCliente(DatagramPacket cliente){
        for (Usuarios usuario : listaUsuarios) {
            if (cliente.getPort() == usuario.getPuerto()) {
                listaUsuarios.remove(usuario);
                break;
            }
        }
    }

    private String sacarUsuario(int port){
        String nick = "";
        for (Usuarios user: listaUsuarios) {
            if (user.getPuerto() == port){
                nick = user.getNombre();
                break;
            }
        }
        return nick;
    }
}
