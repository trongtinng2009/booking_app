package com.example.bookingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class User implements Parcelable {
    private String id,phone,fullname,email,role
            ,password;
    private Timestamp add_date;

    public User() {
    }

    public User(String id, String phone, String fullname, String email, String role, String password,Timestamp add_date) {
        this.id = id;
        this.phone = phone;
        this.fullname = fullname;
        this.email = email;
        this.role = role;
        this.password = password;
        this.add_date = add_date;
    }

    public User(String phone, String fullname, String email, String role, String password, Timestamp add_date) {
        this.phone = phone;
        this.fullname = fullname;
        this.email = email;
        this.role = role;
        this.password = password;
        this.add_date = add_date;
    }
    public static final Parcelable.Creator<User> CREATOR =
            new Creator<User>() {
                @Override
                public User createFromParcel(Parcel source) {
                    return new User(source);
                }

                @Override
                public User[] newArray(int size) {
                    return new User[size];
                }
            };
    User(Parcel in)
    {
        this.id = in.readString();
        this.phone = in.readString();
        this.email = in.readString();
        this.role = in.readString();
        this.password = in.readString();
        this.fullname = in.readString();
        this.add_date =  in.readParcelable(Timestamp.class.getClassLoader());

    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(role);
        dest.writeString(password);
        dest.writeString(fullname);
        dest.writeParcelable(add_date,flags);
    }

    public Timestamp getAdd_date() {
        return add_date;
    }

    public void setAdd_date(Timestamp add_date) {
        this.add_date = add_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
