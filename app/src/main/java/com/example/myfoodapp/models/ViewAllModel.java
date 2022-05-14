package com.example.myfoodapp.models;

import androidx.lifecycle.ViewModel;

import java.io.Serializable;

public class ViewAllModel implements Serializable {
    String name;
    String description;
    String rating;
    String img_url;
    String type;
    String categorie;
    int price;
    int promotion;
    int fidelite;
    public ViewAllModel() {

    }

    public ViewAllModel(String name, String description, String img_url, String type,String categorie, int price,int promotion,int fidelite) {
        this.name = name;
        this.description = description;
        this.img_url = img_url;
        this.type = type;
        this.categorie=categorie;
        this.price = price;
        this.promotion=promotion;
        this.fidelite=fidelite;
    }

    public int getPromotion() {
        return promotion;
    }

    public void setPromotion(int promotion) {
        this.promotion = promotion;
    }

    public int getFidelite() {
        return fidelite;
    }

    public void setFidelite(int fidelite) {
        this.fidelite = fidelite;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
