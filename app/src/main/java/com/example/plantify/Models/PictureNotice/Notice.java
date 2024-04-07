package com.example.plantify.Models.PictureNotice;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Notice implements Parcelable {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    String title;
    String subject;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id;

    public Boolean getImportant() {
        return isImportant;
    }

    public void setImportant(Boolean important) {
        isImportant = important;
    }

    Boolean isImportant;
    String text;

    public Notice(String title, String subject, String text, Boolean isImportant) {

        this.title = title;
        this.subject = subject;
        this.text = text;
        this.isImportant=isImportant;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    protected Notice(Parcel in) {

        title = in.readString();
        subject = in.readString();
        text=in.readString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            isImportant=in.readBoolean();
        }
        id=in.readInt();

    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
       dest.writeString(title);
       dest.writeString(subject);
       dest.writeString(text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dest.writeBoolean(isImportant);
        }
        dest.writeInt(id);

    }
}
