package com.example.plantify;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.plantify.objects.users;

public class firstLoginSplashScreen extends AppCompatActivity {
  users user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login_splash_screen);
        user = getIntent().getParcelableExtra("user");
        LinearLayout events= findViewById(R.id.events);
        LinearLayout lists= findViewById(R.id.lists);
        LinearLayout notes= findViewById(R.id.notes);
        TextView text = findViewById(R.id.firstLoginText);
        AnimatorSet animation2 = new AnimatorSet();
        animation2.play(

                ObjectAnimator.ofFloat(text, "alpha", 0, 1)
        );
        animation2.setDuration(3000);
        animation2.start();;




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                events.setVisibility(View.VISIBLE);
                lists.setVisibility(View.VISIBLE);
                notes.setVisibility(View.VISIBLE);
                AnimatorSet animation = new AnimatorSet();
                animation.playTogether(

                        ObjectAnimator.ofFloat(events, "alpha", 0, 1),

                        ObjectAnimator.ofFloat(lists, "alpha", 0, 1),

                        ObjectAnimator.ofFloat(notes, "alpha", 0, 1)
                );
                animation.setDuration(1000);
                animation.start();
            }
        },2000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                 Intent intent = new Intent(firstLoginSplashScreen.this,Menu.class);
                 intent.putExtra("user",user);
               startActivity(intent);
            }
        },3000);
    }
}