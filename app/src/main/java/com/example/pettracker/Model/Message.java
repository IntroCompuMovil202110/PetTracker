package com.example.pettracker.Model;

public class Message {

    private String message;
    private String name;
    private String picture;
    private String urlImage;
    private String type_message;

    public Message() {
    }

    public Message(String message, String name, String picture, String type_message) {
        this.message = message;
        this.name = name;
        this.picture = picture;
        this.type_message = type_message;
    }

    public Message(String message, String name, String picture, String urlImage, String type_message) {
        this.message = message;
        this.name = name;
        this.picture = picture;
        this.urlImage = urlImage;
        this.type_message = type_message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getType_message() {
        return type_message;
    }

    public void setType_message(String type_message) {
        this.type_message = type_message;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
