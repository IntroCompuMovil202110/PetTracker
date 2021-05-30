package com.example.pettracker.Model;
/*
* Reemplazar botón mi mascota por buscar paseadores (para cliente) y solicitudes de paseos (para paseador)
* Allí se desplega una lista de paseos, para el usuario se muestran los propios, para el paseador se muestran todos
* El paseador puede aceptar un paseo y ahí se abre el mapa
* */
public class Walk {
    String walkID;
    String clientID;
    String walkerID;
    // Status could be "Pendiente", "Aceptado", "Terminado"
    String status;

    public Walk() {
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getWalkerID() {
        return walkerID;
    }

    public void setWalkerID(String walkerID) {
        this.walkerID = walkerID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWalkID() {
        return walkID;
    }

    public void setWalkID(String walkID) {
        this.walkID = walkID;
    }
}
