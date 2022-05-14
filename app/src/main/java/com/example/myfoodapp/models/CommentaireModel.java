package com.example.myfoodapp.models;

public class CommentaireModel {
    String username;
    String user_img;
    String current_date;
    String curent_time;
    String commentaire;

    public CommentaireModel() {
    }

    public CommentaireModel(String username, String user_img, String current_date, String curent_time, String commentaire) {
        this.username = username;
        this.user_img = user_img;
        this.current_date = current_date;
        this.curent_time = curent_time;
        this.commentaire = commentaire;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date(String current_date) {
        this.current_date = current_date;
    }

    public String getCurent_time() {
        return curent_time;
    }

    public void setCurent_time(String curent_time) {
        this.curent_time = curent_time;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}
