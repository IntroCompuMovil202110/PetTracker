package com.example.pettracker.Model;

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
