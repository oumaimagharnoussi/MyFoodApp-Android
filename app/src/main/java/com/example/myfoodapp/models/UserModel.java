package com.example.myfoodapp.models;

import android.widget.Toast;

import com.example.myfoodapp.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserModel {
    String img_url;
    String number;
    String address;
    String name;
    String mail;
    String pass;
    String role;

    public UserModel() {
    }

    public UserModel(String name, String mail, String pass,String role) {
        this.name = name;
        this.mail = mail;
        this.pass = pass;
        this.role=role;
    }

    public UserModel(String img_url, String number, String address, String name, String mail) {
        this.img_url = img_url;
        this.number = number;
        this.address = address;
        this.name = name;
        this.mail = mail;
    }

    public UserModel(String img_url, String number, String address, String name, String mail, String pass) {
        this.img_url = img_url;
        this.number = number;
        this.address = address;
        this.name = name;
        this.mail = mail;
        this.pass = pass;
    }

    public UserModel(String img_url, String number, String address, String name, String mail, String pass,String role) {
        this.img_url = img_url;
        this.number = number;
        this.address = address;
        this.name = name;
        this.mail = mail;
        this.pass = pass;
        this.role=role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    /*
    public static boolean check_role_admin(){
        FirebaseAuth auth;
        FirebaseFirestore database;

        auth= FirebaseAuth.getInstance();
        database= FirebaseFirestore.getInstance();
        FirebaseUser user= auth.getCurrentUser();
        DocumentReference doc=database.collection("users").document(user.getUid());

        doc.addSnapshotListener((documentSnapshot,e)-> {
            if (documentSnapshot.exists()) {
                String role= documentSnapshot.getString("role");
                if (role.equals("admin")){
                    return true;
                }


            }
        });
        return false;
    }*/
}
