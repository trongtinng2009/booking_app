package com.example.bookingapp.Model;

import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;

public class Location implements Serializable {
    private String id,name,img;
    private Country country;
    private int view;


    public Location() {
    }

    public Location(String id, String name, String img, Country country, int view) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.country = country;
        this.view = view;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }
}
