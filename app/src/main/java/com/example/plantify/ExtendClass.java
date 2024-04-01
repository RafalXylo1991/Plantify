package com.example.plantify;

import androidx.appcompat.app.AppCompatActivity;

import com.example.plantify.Helpers.Time;

public class ExtendClass extends AppCompatActivity {
    private static Time time = new Time();

    public Time getTime() {
        return time;
    }
}
