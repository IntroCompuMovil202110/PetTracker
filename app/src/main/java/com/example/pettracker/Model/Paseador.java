package com.example.pettracker.Model;

import java.util.List;

public class Paseador extends Usuario {
    private String costoServicio;

    public Paseador() {
    }

    public Paseador(String nombre, String apellido, String correo, String contrasena, String telefono, String direccion, List<Product> productos, String costoServicio) {
        super(nombre, apellido, correo, contrasena, telefono, direccion, productos);
        this.costoServicio = costoServicio;
    }

    public String getCostoServicio() {
        return costoServicio;
    }

    public void setCostoServicio(String costoServicio) {
        this.costoServicio = costoServicio;
    }
}
