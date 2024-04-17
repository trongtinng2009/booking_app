package com.example.bookingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class FavoriteHotel implements Parcelable
{
    User user;
    Hotel hotel;
    String id;

    public FavoriteHotel() {
    }

    public FavoriteHotel(User user, Hotel hotel, String id) {
        this.user = user;
        this.hotel = hotel;
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static final Parcelable.Creator<FavoriteHotel> CREATOR =
            new Creator<FavoriteHotel>() {
                @Override
                public FavoriteHotel createFromParcel(Parcel source) {
                    return new FavoriteHotel(source);
                }

                @Override
                public FavoriteHotel[] newArray(int size) {
                    return new FavoriteHotel[size];
                }
            };
    FavoriteHotel(Parcel in)
    {
        this.user = in.readParcelable(User.class.getClassLoader());
        this.hotel = (Hotel) in.readSerializable();
        this.id = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable(user,flags);
        dest.writeSerializable(hotel);
        dest.writeString(id);
    }
}
