package com.example.plantify;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class mainSplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_splash_screen);
        ImageView logo = findViewById(R.id.splashScreenIcon);
        TextView text = findViewById(R.id.splashScreenText);
        AnimatorSet animation = new AnimatorSet();
        animation.playTogether(
                ObjectAnimator.ofFloat(logo, "rotation", 0, 360),
                ObjectAnimator.ofFloat(logo, "scaleX", 0, 1),
                ObjectAnimator.ofFloat(logo, "scaleY", 0, 1),
                ObjectAnimator.ofFloat(text, "alpha", 0, 1)
        );
        animation.setDuration(1000);
        animation.start();
        AnimatorSet animation2 = new AnimatorSet();
        animation2.play(

                ObjectAnimator.ofFloat(text, "alpha", 0, 1)
        );
        animation2.setDuration(2000);
        animation2.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                 startActivity(new Intent(mainSplashScreen.this,MainActivity.class));
            }
        },2000);
    }
}