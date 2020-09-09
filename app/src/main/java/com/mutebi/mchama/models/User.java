package com.mutebi.mchama.models;

public class User {

    private String email;
    private int id;
    private String name;
    private String phone;
    private int wallet;
    private int rotation;
    private String token;

    public User(int id, String name, String email, String phone, int wallet, int rotation, String token) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.wallet = wallet;
        this.rotation = rotation;
        this.token = token;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



}
