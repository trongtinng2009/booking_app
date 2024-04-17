package com.example.bookingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Review implements Parcelable{
    String id,content;
    Timestamp postdate;
    int rating_point;
    User user;
    Hotel hotel;

    public Review() {
    }
    public static final Parcelable.Creator<Review> CREATOR =
            new Parcelable.Creator<Review>() {
                @Override
                public Review createFromParcel(Parcel source) {
                    return new Review(source);
                }

                @Override
                public Review[] newArray(int size) {
                    return new Review[size];
                }
            };
    Review(Parcel in)
    {
        this.id = in.readString();
        this.content = in.readString();
        this.postdate = in.readParcelable(Timestamp.class.getClassLoader());
        this.rating_point = in.readInt();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.hotel = (Hotel) in.readSerializable();
    }

    public Review(String id, String content, Timestamp postdate, int rating_point, User user, Hotel hotel) {
        this.id = id;
        this.content = content;
        this.postdate = postdate;
        this.rating_point = rating_point;
        this.user = user;
        this.hotel = hotel;
    }
    public float calcRatingPoint()
    {
        float totalpoint = 0;
        int total = 0;
        for(int i = 0 ; i< hotel.getStars().size();i++)
        {
            totalpoint += (i+1) * hotel.getStars().get(i);
            total += hotel.getStars().get(i);
        }
        BigDecimal roundfinalPrice = new BigDecimal(totalpoint/total).setScale(2,BigDecimal.ROUND_HALF_UP);
        return (float) Math.floor(roundfinalPrice.doubleValue());
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(content);
        dest.writeParcelable(postdate,flags);
        dest.writeInt(rating_point);
        dest.writeParcelable(user,flags);
        dest.writeSerializable(hotel);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getPostdate() {
        return postdate;
    }

    public void setPostdate(Timestamp postdate) {
        this.postdate = postdate;
    }

    public int getRating_point() {
        return rating_point;
    }

    public void setRating_point(int rating_point) {
        this.rating_point = rating_point;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
}
