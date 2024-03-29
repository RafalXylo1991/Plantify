package com.example.plantify.login;

import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.example.plantify.MainActivity;
import com.example.plantify.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class google extends MainActivity {
    ImageView image;
    MainActivity mainActivity;
    FirebaseAuth auth;
    GoogleSignInClient googleSignInClient;

    public FirebaseAuth getAuth() {
        return auth;
    }



    public GoogleSignInClient getGoogleSignInClient() {
        return googleSignInClient;
    }







    public google(MainActivity mainActivity, ImageView imageView) {
        this.mainActivity=mainActivity;
        this.image=imageView;
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mainActivity.getResources().getString(R.string.clientId))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(mainActivity, options);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            Glide.with(mainActivity).load(Objects.requireNonNull(auth.getCurrentUser()).getPhotoUrl()).into(imageView);
            Log.i("user", auth.getCurrentUser().getDisplayName().toString());

        }


    }
}
