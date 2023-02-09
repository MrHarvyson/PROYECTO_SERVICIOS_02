package com.vedruna.proyectoServicios02;

import java.net.InetAddress;

public class Usuarios {

    private InetAddress direccion;
    private Integer puerto;
    private String nombre;


    public Usuarios(InetAddress direccion, Integer puerto, String nombre) {
        this.direccion = direccion;
        this.puerto = puerto;
        this.nombre = nombre;
    }

    public InetAddress getDireccion() {
        return direccion;
    }

    public Integer getPuerto() {
        return puerto;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return "Usuario= " + nombre + ", direccion=" + direccion + ", puerto=" + puerto;
    }
}
