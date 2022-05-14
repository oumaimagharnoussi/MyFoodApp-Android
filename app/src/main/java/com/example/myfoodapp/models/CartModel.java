package com.example.myfoodapp.models;

import java.io.Serializable;

public class CartModel implements Serializable {
    String product_name;
    int Product_price;
    String current_date;
    String current_time;
    int total_price;
    String total_quantity;
    String documentId;

    public CartModel() {
    }

    public CartModel(String porduct_name, int product_price, String current_date, String current_time, int total_price, String total_quantity) {
        this.product_name = porduct_name;
        Product_price = product_price;
        this.current_date = current_date;
        this.current_time = current_time;
        this.total_price = total_price;
        this.total_quantity = total_quantity;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String porduct_name) {
        this.product_name = porduct_name;
    }

    public int getProduct_price() {
        return Product_price;
    }

    public void setProduct_price(int product_price) {
        Product_price = product_price;
    }

    public String getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date(String current_date) {
        this.current_date = current_date;
    }

    public String getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public String getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(String total_quantity) {
        this.total_quantity = total_quantity;
    }
}
