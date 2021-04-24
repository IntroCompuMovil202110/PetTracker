package com.example.pettracker.Controller;

import com.example.pettracker.Model.Message;
import java.util.Map;

public class SendMessage extends Message {
    private Map hour;

    public SendMessage(){

    }

    public SendMessage(Map hour){
        this.hour = hour;
    }

    public SendMessage(String message, String name, String picture, String type_message, Map hour) {
        super(message, name, picture, type_message);
        this.hour = hour;
    }

    public SendMessage(String message, String name, String picture, String urlImage, String type_message, Map hour) {
        super(message, name, picture, urlImage, type_message);
        this.hour = hour;
    }

    public Map getHour() {
        return hour;
    }

    public void setHour(Map hour) {
        this.hour = hour;
    }
}
