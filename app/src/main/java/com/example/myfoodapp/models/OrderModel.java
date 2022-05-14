package com.example.myfoodapp.models;

import java.sql.Array;
import java.util.Arrays;
import java.util.List;

public class OrderModel {

    float total;
    String payment;
    //List<CartModel> listCart;
    String listCart;
    String user;
    String userId;


    public OrderModel() {
    }

    public OrderModel(float total, String payment, String listCart) {
        this.total = total;
        this.payment = payment;
        this.listCart = listCart;
    }

    public OrderModel(float total, String payment, String listCart, String user,String userId) {
        this.total = total;
        this.payment = payment;
        this.listCart = listCart;
        this.user = user;
        this.userId=userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    /*public List<CartModel> getListCart() {
        return listCart;
    }*/
    public String getListCart() {
        return listCart;
    }

    public void setListCart(String listCart) {
        this.listCart = listCart;
    }
}
