package com.example.pettracker.Model;

import com.example.pettracker.Model.Firebase.LUsuario;

import java.io.Serializable;

public class Product implements Serializable {
    String title;
    String image;
    String details;
    String price;
    String type;
    String SpeciesClassification;
    String key;
    LUsuario publisher;

    public Product() {

    }

    public Product(String title, String image, String details, String price, String type, String SpeciesClassification, LUsuario publisher, String key) {
        this.title = title;
        this.image = image;
        this.details = details;
        this.price = price;
        this.type = type;
        this.SpeciesClassification = SpeciesClassification;
        this.key = key;
        this.publisher = publisher;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setkey(String key) {
        this.key = key;
    }

    public String getSpeciesClassification() {
        return SpeciesClassification;
    }

    public void setSpeciesClassification(String speciesClassification) {
        SpeciesClassification = speciesClassification;
    }

    public LUsuario getPublisher() {
        return publisher;
    }

    public void setPublisher(LUsuario publisher) {
        this.publisher = publisher;
    }
}
