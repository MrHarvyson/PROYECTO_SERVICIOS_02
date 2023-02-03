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

    public void setDireccion(InetAddress direccion) {
        this.direccion = direccion;
    }

    public Integer getPuerto() {
        return puerto;
    }

    public void setPuerto(Integer puerto) {
        this.puerto = puerto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Usuario= " + nombre + ", direccion=" + direccion + ", puerto=" + puerto ;
    }
}
