package com.example.pettracker.Model;

import com.google.firebase.database.ServerValue;

public class Message {

    private String message;
    private String urlPicture;
    private boolean hasPicture;
    private String emisorKey;
    private Object createdTimestamp;

    public Message() {
        createdTimestamp = ServerValue.TIMESTAMP;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public boolean isHasPicture() {
        return hasPicture;
    }

    public void setHasPicture(boolean hasPicture) {
        this.hasPicture = hasPicture;
    }

    public String getEmisorKey() {
        return emisorKey;
    }

    public void setEmisorKey(String emisorKey) {
        this.emisorKey = emisorKey;
    }

    public Object getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Object createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }
}
