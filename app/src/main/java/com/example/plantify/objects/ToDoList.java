package com.example.plantify.objects;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

public class ToDoList implements Parcelable {
    protected ToDoList(Parcel in) {
        id = in.readInt();
        title = in.readString();
        date = in.readString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            isDone = in.readBoolean();
        }
        progress=in.readInt();


    }

    public static final Creator<ToDoList> CREATOR = new Creator<ToDoList>() {
        @Override
        public ToDoList createFromParcel(Parcel in) {
            return new ToDoList(in);
        }

        @Override
        public ToDoList[] newArray(int size) {
            return new ToDoList[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getDate() {
        return date;
    }

    public ToDoList(int id,String title,String date, JsonObject tasks, boolean isDone, int progress) {
        this.id=id;
        this.title = title;
        this.date = date;
        this.tasks = tasks;
        this.isDone=isDone;
        this.progress=progress;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public JsonObject getTasks() {
        return tasks;
    }

    public void setTasks(JsonObject tasks) {
        this.tasks = tasks;
    }

    int id;
    String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String date;
    JsonObject tasks;
    int progress;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public ToDoList getmInfo() {
        return mInfo;
    }

    public void setmInfo(ToDoList mInfo) {
        this.mInfo = mInfo;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    boolean isDone;


    @Override
    public int describeContents() {
        return 0;
    }
    private ToDoList mInfo;
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(date);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dest.writeBoolean(isDone);
        }
        dest.writeInt(progress);

    }
}
