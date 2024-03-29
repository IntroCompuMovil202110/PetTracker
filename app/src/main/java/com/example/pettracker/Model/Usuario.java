package com.example.pettracker.Model;

import java.util.List;

public class Usuario {
    public String fotoPerfilURL;
    public String wallpaper;
    public String nombre;
    public String apellido;
    public String correo;
    public String contrasena;
    public String telefono;
    public String direccion;
    public String rol;
    List<Product> productoUsuario;
    private double latitude;
    private double longitude;

    public Usuario(){

    }

    public Usuario(String fotoPerfilURL, String wallpaper, String nombre, String apellido, String correo, String contrasena, String telefono, String direccion, List<Product> productos){
        this.fotoPerfilURL = fotoPerfilURL;
        this.wallpaper = wallpaper;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.direccion = direccion;
        productoUsuario = productos;
    }

    public Usuario(String fotoPerfilURL, String wallpaper, String nombre, String apellido, String correo, String contrasena, String telefono, String direccion, String rol){
        this.fotoPerfilURL = fotoPerfilURL;
        this.wallpaper = wallpaper;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.direccion = direccion;
        this.rol = rol;

    }

    public Usuario(String fotoPerfilURL, String nombre, String apellido){
        this.nombre = nombre;
        this.apellido = apellido;
        this.fotoPerfilURL = fotoPerfilURL;

    }

    public String getFotoPerfilURL() {
        return fotoPerfilURL;
    }

    public void setFotoPerfilURL(String fotoPerfilURL) {
        this.fotoPerfilURL = fotoPerfilURL;
    }

    public String getWallpaper() {
        return wallpaper;
    }

    public void setWallpaper(String wallpaper) {
        this.wallpaper = wallpaper;
    }

    public List<Product> getProductoUsuario() {
        return productoUsuario;
    }

    public void setProductoUsuario(List<Product> productoUsuario) {
        this.productoUsuario = productoUsuario;
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return nombre;
    }

   /* public int getCount(){
        return productoUsuario.size();
    }*/

    public Object getProducto(int posicion){
        return productoUsuario.get(posicion);
    }

    public long getProductoId(int posicion){
        return productoUsuario.indexOf(getProducto(posicion));
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
