package com.vedruna.proyectoServicios02.servidor;
import com.vedruna.proyectoServicios02.Usuarios;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

public class ServerLoginHilo implements Runnable {
    public List<Usuarios> listaUsuarios;

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

    // crea un usuario en el caso de no existir
    private void crearUsuarios() throws IOException {
        // socket por el que recibe peticion de nuevo usuario del login(cliente)
        DatagramSocket socketUsuario = new DatagramSocket(7010);
        while (true) {
            byte[] bufer = new byte[1024];
            DatagramPacket usuarioPacket = new DatagramPacket(bufer, bufer.length);
            socketUsuario.receive(usuarioPacket);
            // nos llega la informacion del login del cliente y la enviamos para crear un usuario nuevo
            anadirUsuario(usuarioPacket);
            for (int i = 0; i < listaUsuarios.size(); i++) {
                System.out.println(listaUsuarios.get(i).toString());
            }
            System.out.println(listaUsuarios.size());

        }

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
            System.out.println(usuario.getNombre() + " " + usuario.getPuerto());
        } else {
            System.out.println("Usuario existe.");
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
        for (int i = 0; i < listaUsuarios.size(); i++) {
            if (nombreUsuario.equals(listaUsuarios.get(i).getNombre())) {
                existe = true;
            }
        }
        return existe;
    }

}
