package com.example.plantify;

import androidx.appcompat.app.AppCompatActivity;

import com.example.plantify.Helpers.Fragment;
import com.example.plantify.Helpers.Time;
import com.example.plantify.Helpers.eventHandlers;
import com.example.plantify.Models.PictureNotice.ToDoList;
import com.example.plantify.Models.PictureNotice.users;

import java.util.ArrayList;
import java.util.List;

public class ExtendClass extends AppCompatActivity {
    private static Time time = new Time();
    protected static String serverType = "external";

    private static eventHandlers events = new eventHandlers();
    private static users user = new users();

    public static eventHandlers getEvents() {
        return events;
    }

    private static Fragment fragment = new Fragment();
    public static List<ToDoList> toDoLists = new ArrayList<>();

    public static List<ToDoList> getToDoLists() {
        return toDoLists;
    }

    public static void setToDoLists(List<ToDoList> toDoLists) {
        ExtendClass.toDoLists = toDoLists;
    }

    public static users getUser() {
        return user;
    }

    public static void setUser(users user) {
        ExtendClass.user = user;
    }

    public static Fragment getFragment() {
        return fragment;
    }

    public Time getTime() {
        return time;
    }
}
