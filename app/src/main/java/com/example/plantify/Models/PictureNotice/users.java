package com.example.plantify.Models.PictureNotice;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;


import com.example.plantify.SQL;

import java.util.ArrayList;
import java.util.List;

public class users extends SQL implements Parcelable {
    public int unDoneLists;
    public int unImportantNotices;
    public int lastEvents;



    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }
   public String[] courses = { "1h", "4h",
            "12h", "1D",
            "7D", "10D" };
  public  boolean isLogged=false;
    public  String accessToken;
    public String refreshToken;
    public List<Notice> noticeList;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    public  int id;
    public   String name;
    public    String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    String email;






    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    public Notice notice;






    public users(int id, String name, String password, String email){
        noticeList=new ArrayList<>();
        this.id=id;
        this.name = name;

        this.password = password;

        this.email = email;



    }

    public users() {

    }
    protected users(Parcel in) {
        isLogged = in.readByte() != 0;
        accessToken = in.readString();
        refreshToken = in.readString();
        id=in.readInt();
        name = in.readString();
        password = in.readString();

        email = in.readString();



    }

    public static final Creator<users> CREATOR = new Creator<users>() {
        @Override
        public users createFromParcel(Parcel in) {
            return new users(in);
        }

        @Override
        public users[] newArray(int size) {
            return new users[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }








    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeByte((byte) (isLogged ? 1 : 0));
        dest.writeString(accessToken);
        dest.writeString(refreshToken);
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(password);

        dest.writeString(email);






    }

}

