package com.example.bookingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Room implements Serializable {
    private String id,img,name;
    private Hotel hotel;
    private ArrayList<String> imgs;
    private int maxadult,maxkid,quantity,remain_quantity;
    private double size,priceadult,pricekid;

    public Room() {
    }
    public double calPrice(int adult,int kid)
    {
        return calAdultPrice(adult) + calKidPrice(kid);
    }
    public double calKidPrice(int kid)
    {
        return kid * pricekid;
    }
    public double calAdultPrice(int adult)
    {
        return adult * priceadult;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Room(String id, String img, String name, Hotel hotel, ArrayList<String> imgs, int maxadult,
                int maxkid, int quantity, int remain_quantity, double size, double priceadult, double pricekid) {
        this.id = id;
        this.img = img;
        this.name = name;
        this.hotel = hotel;
        this.imgs = imgs;
        this.maxadult = maxadult;
        this.maxkid = maxkid;
        this.quantity = quantity;
        this.remain_quantity = remain_quantity;
        this.size = size;
        this.priceadult = priceadult;
        this.pricekid = pricekid;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxadult() {
        return maxadult;
    }

    public void setMaxadult(int maxadult) {
        this.maxadult = maxadult;
    }

    public int getMaxkid() {
        return maxkid;
    }

    public void setMaxkid(int maxkid) {
        this.maxkid = maxkid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getRemain_quantity() {
        return remain_quantity;
    }

    public void setRemain_quantity(int remain_quantity) {
        this.remain_quantity = remain_quantity;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getPriceadult() {
        return priceadult;
    }

    public void setPriceadult(double priceadult) {
        this.priceadult = priceadult;
    }

    public double getPricekid() {
        return pricekid;
    }

    public void setPricekid(double pricekid) {
        this.pricekid = pricekid;
    }
}
