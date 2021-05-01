package com.example.pettracker.Model;

import java.util.List;

public class Paseador extends Usuario {
    private String costoServicio;

    public Paseador() {
    }

    public Paseador(String fotoPerfilURL, String wallpaper, String nombre, String apellido, String correo, String contrasena, String telefono, String direccion, List<Product> productos, String costoServicio) {
        super(fotoPerfilURL, wallpaper, nombre, apellido, correo, contrasena, telefono, direccion, productos);
        this.costoServicio = costoServicio;
    }

    public Paseador(String fotoPerfilURL, String wallpaper, String nombre, String apellido, String correo, String contrasena, String telefono, String direccion, String rol, String costoServicio) {
        super(fotoPerfilURL, wallpaper, nombre, apellido, correo, contrasena, telefono, direccion, rol);
        this.costoServicio = costoServicio;
    }

    public String getCostoServicio() {
        return costoServicio;
    }

    public void setCostoServicio(String costoServicio) {
        this.costoServicio = costoServicio;
    }
}
