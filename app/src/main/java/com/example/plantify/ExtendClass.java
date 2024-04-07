package com.example.plantify;

import androidx.appcompat.app.AppCompatActivity;

import com.example.plantify.Helpers.Fragment;
import com.example.plantify.Helpers.Time;
import com.example.plantify.objects.users;

public class ExtendClass extends AppCompatActivity {
    private static Time time = new Time();
    private static users user = new users();
    private static Fragment fragment = new Fragment();

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
