package com.example.pettracker.Model;
/*
* Reemplazar botón mi mascota por buscar paseadores (para cliente) y solicitudes de paseos (para paseador)
* Allí se desplega una lista de paseos, para el usuario se muestran los propios, para el paseador se muestran todos
* El paseador puede aceptar un paseo y ahí se abre el mapa
* */
public class Walk {
    String key;
    String clientID;
    String walkerID;

    public Walk() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
}
