package com.example.pettracker.Model.Firebase;

import com.example.pettracker.Model.Message;

import java.util.Date;
import java.util.Locale;

import org.ocpsoft.prettytime.PrettyTime;

public class LMessage {
    private String key;
    private Message mensaje;
    private LUsuario lUser;

    public LMessage(String key, Message mensaje) {
        this.key = key;
        this.mensaje = mensaje;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Message getMessage() {
        return mensaje;
    }

    public void setMessage(Message mensaje) {
        this.mensaje = mensaje;
    }

    public long getCreatedTimestampLong(){
        return (long) mensaje.getCreatedTimestamp();
    }

    public LUsuario getlUser() {
        return lUser;
    }

    public void setlUser(LUsuario lUser) {
        this.lUser = lUser;
    }

    public String dateCreationMessage(){
        Date date = new Date(getCreatedTimestampLong());
        PrettyTime prettyTime = new PrettyTime(new Date(), Locale.getDefault());
        return prettyTime.format(date);

    }

}
