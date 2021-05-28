package com.example.pettracker.Model;

import java.util.List;

public class Paseador extends Usuario {

    public Paseador() {
    }

    public Paseador(String fotoPerfilURL, String wallpaper, String nombre, String apellido, String correo, String contrasena, String telefono, String direccion, String rol, String costoServicio) {
        super(fotoPerfilURL, wallpaper, nombre, apellido, correo, contrasena, telefono, direccion, rol);
    }
}
