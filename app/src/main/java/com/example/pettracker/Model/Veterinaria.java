package com.example.pettracker.Model;

import java.io.Serializable;
public class Veterinaria implements Serializable{
    private Double calificacion;
    private String direccion;
    private String horas;
    private Double latitud;
    private Double longitud;
    private String nombre;
    private String telefono;

    public Veterinaria(Double calificacion, String direccion, String horas, Double latitud, Double longitud, String nombre, String telefono) {
        this.calificacion = calificacion;
        this.direccion = direccion;
        this.horas = horas;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public Double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Double calificacion) {
        this.calificacion = calificacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getHoras() {
        return horas;
    }

    public void setHoras(String horas) {
        this.horas = horas;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
