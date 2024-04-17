package com.example.bookingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class FavoriteRoom implements Parcelable {
    String id;
    User user;
    Room room;

    public FavoriteRoom(String id, User user, Room room) {
        this.id = id;
        this.user = user;
        this.room = room;
    }

    public FavoriteRoom() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeParcelable(user,flags);
        dest.writeSerializable(room);
    }
    public static final Parcelable.Creator<FavoriteRoom> CREATOR =
            new Creator<FavoriteRoom>() {
                @Override
                public FavoriteRoom createFromParcel(Parcel source) {
                    return new FavoriteRoom(source);
                }

                @Override
                public FavoriteRoom[] newArray(int size) {
                    return new FavoriteRoom[size];
                }
            };
    FavoriteRoom(Parcel in)
    {
        this.id = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.room = (Room) in.readSerializable();
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
