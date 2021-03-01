package com.example.pettracker.Model;

import java.util.List;

public class Usuario {
    public String nombre;
    public String apellido;
    public String correo;
    public String contrasena;
    public String telefono;
    public String direccion;
    List<Product> productoUsuario;
    public Usuario(){

    }

    public Usuario(String nombre, String apellido, String correo, String contrasena, String telefono, String direccion, List<Product> productos){
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.direccion = direccion;
        productoUsuario = productos;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public String getContrasena() {
        return contrasena;
    }
    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public int getCount(){
        return productoUsuario.size();
    }

    public Object getProducto(int posicion){
        return productoUsuario.get(posicion);
    }

    public long getProductoId(int posicion){
        return productoUsuario.indexOf(getProducto(posicion));
    }

}
