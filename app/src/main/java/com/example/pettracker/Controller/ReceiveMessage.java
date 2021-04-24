package com.example.pettracker.Controller;

import com.example.pettracker.Model.Message;

public class ReceiveMessage extends Message {
    private long hour;

    public ReceiveMessage() {
    }

    public ReceiveMessage(long hour) {
        this.hour = hour;
    }

    public ReceiveMessage(String message, String name, String picture, String urlImage, String type_message, long hour) {
        super(message, name, picture, urlImage, type_message);
        this.hour = hour;
    }

    public long getHour() {
        return hour;
    }

    public void setHour(long hour) {
        this.hour = hour;
    }
}
