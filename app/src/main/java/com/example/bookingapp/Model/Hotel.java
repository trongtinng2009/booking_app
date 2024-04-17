package com.example.bookingapp.Model;

import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;
import java.util.ArrayList;

public class Hotel implements Serializable {
    private ArrayList<String> imgs;
    private ArrayList<Integer> stars = new ArrayList<Integer>(5);
    private Location location;
    private String address,hotline,id,imgtitle,name,summary;
    private double distance,overallrating,saleoff,price;
    private int view;

    public Hotel() {
    }

    public Hotel(ArrayList<String> imgs, Location location, String address,
                 String hotline, String id, String imgtitle, String name, String summary,double price) {
        this.imgs = imgs;
        this.location = location;
        this.address = address;
        this.hotline = hotline;
        this.id = id;
        this.imgtitle = imgtitle;
        this.name = name;
        this.summary = summary;
        this.price = price;
    }

    public Hotel(ArrayList<String> imgs, ArrayList<Integer> stars, Location location,
                 String address, String hotline, String id, String imgtitle, String name,
                 String summary, double distance, double overallrating, double saleoff, int view) {
        this.imgs = imgs;
        this.stars = stars;
        this.location = location;
        this.address = address;
        this.hotline = hotline;
        this.id = id;
        this.imgtitle = imgtitle;
        this.name = name;
        this.summary = summary;
        this.distance = distance;
        this.overallrating = overallrating;
        this.saleoff = saleoff;
        this.view = view;
    }

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ArrayList<Integer> getStars() {
        return stars;
    }

    public void setStars(ArrayList<Integer> stars) {
        this.stars = stars;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHotline() {
        return hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgtitle() {
        return imgtitle;
    }

    public void setImgtitle(String imgtitle) {
        this.imgtitle = imgtitle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getOverallrating() {
        return overallrating;
    }

    public void setOverallrating(double overallrating) {
        this.overallrating = overallrating;
    }

    public double getSaleoff() {
        return saleoff;
    }

    public void setSaleoff(double saleoff) {
        this.saleoff = saleoff;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }
}
