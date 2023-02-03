package com.vedruna.proyectoServicios02.servidor;

import com.vedruna.proyectoServicios02.Usuarios;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;

public class ServerLoginHilo implements Runnable {
    private Usuarios usuarios;
    private List<Usuarios> listaUsuarios;

    public ServerLoginHilo(List<Usuarios> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    @Override
    public void run() {
        try {
            crearUsuarios();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void crearUsuarios() throws IOException {
        // socket por el que recibe peticion de nuevo usuario
        DatagramSocket socketUsuario = new DatagramSocket(8010);
        while (true) {
            byte[] bufer = new byte[1024];
            DatagramPacket usuarioPacket = new DatagramPacket(bufer, bufer.length);
            socketUsuario.receive(usuarioPacket);
            anadirUsuario(usuarioPacket);
        }

    }

    private void anadirUsuario(DatagramPacket recibo) {
        String nombreUsuario = new String(recibo.getData()).trim();
        InetAddress direccion = recibo.getAddress();
        int puerto = recibo.getPort();

        String mensaje;
        if(!comprobarUsuario(nombreUsuario)){
            Usuarios usuario = new Usuarios(direccion, puerto, nombreUsuario);
            listaUsuarios.add(usuario);
            mensaje = "ok";
            System.out.println(usuario.getNombre() + " " + usuario.getPuerto());
            for (int i = 0; i < listaUsuarios.size(); i++) {
                System.out.println(listaUsuarios.get(i).toString());
            }
        } else {
            System.out.println("Usuario existe.");
            mensaje = "noOk";
        }
        envioComprobacion(mensaje, recibo);
    }

    private void envioComprobacion(String mensaje,DatagramPacket recibo) {
        byte[] data = mensaje.getBytes();
        DatagramSocket socketEnvio = null;
        try {
            socketEnvio = new DatagramSocket(9010);
            DatagramPacket packet = new DatagramPacket(data, data.length, recibo.getAddress(), recibo.getPort());
            socketEnvio.send(packet);
            socketEnvio.close();
        } catch (IOException e) {
            System.out.println(e);
            System.out.println("Error al enviar comprobación.");
        }

    }

    private boolean comprobarUsuario(String nombreUsuario) {
        boolean existe = false;
        for (int i = 0; i < listaUsuarios.size(); i++) {
            if (nombreUsuario.equals(listaUsuarios.get(i).getNombre())) {
                existe = true;
            }
        }
        return existe;
    }


}
