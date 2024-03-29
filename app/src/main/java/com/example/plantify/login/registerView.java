package com.example.plantify.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.plantify.R;
import com.example.plantify.firstLoginSplashScreen;
import com.example.plantify.objects.users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class registerView extends AppCompatActivity {
    boolean isValidate=true;
    users logged;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_view);

        TextView name = findViewById(R.id.name);

        TextView password = findViewById(R.id.password);
        TextView repeatPassword = findViewById(R.id.repeatPassword);
        TextView email = findViewById(R.id.email);








        Button button = (Button) findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean isValis=true;
                JSONArray array = new JSONArray();

                JSONObject currency = new JSONObject();
                try {
                    currency.put("PLN", 0);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                array.put(currency);


                if (name.getText().toString().length() < 2 || name.getText().toString().length() == 0) {
                    name.setError("The name shoud not be  less than 2 characters long");
                    isValis=false;

                }
                if (password.getText().toString().length() == 0 || repeatPassword.getText().toString().length() == 0){
                    password.setError("<font color='red'>The password shoud not be  less than 2 characters long");
                    repeatPassword.setError("<font color='red'>The password shoud not be  less than 2 characters long");

                    isValis=false;

                }

                if(!password.getText().toString().equals(repeatPassword.getText().toString())){
                    repeatPassword.setError("<font color='red'>Password has to be the same");

                    isValis=false;
                }


                if(email.getText().toString().length()==0||!email.getText().toString().contains("@")){
                    email.setError("<font color='red'>Incorrect email");
                    isValis=false;
                }



                if(isValis

                ){

                    JSONObject newUser = new JSONObject();
                    try {
                     users   user = new users();
                        newUser.put("name", name.getText().toString());
                        newUser.put("password", password.getText().toString());

                        newUser.put("email", email.getText().toString());



                   user.register(newUser).subscribeOn(Schedulers.io())
                           .observeOn(AndroidSchedulers.mainThread())
                           .subscribe(new Observer<String>() {
                               @Override
                               public void onSubscribe(@NonNull Disposable d) {

                               }

                               @Override
                               public void onNext(@NonNull String s) {
                                   System.out.println("cipeczka");
                               }

                               @Override
                               public void onError(@NonNull Throwable e) {

                               }

                               @Override
                               public void onComplete() {
                                  users user = new users();
                                   try {
                                       user.Login(name.getText().toString(),password.getText().toString(),"").subscribeOn(Schedulers.io())
                                               .observeOn(AndroidSchedulers.mainThread())
                                               .subscribe(new Observer<users>() {
                                                   @Override
                                                   public void onSubscribe(@NonNull Disposable d) {

                                                   }

                                                   @Override
                                                   public void onNext(@NonNull users users) {
                                                           logged=users;
                                                       Intent intent = new Intent(registerView.this, firstLoginSplashScreen.class);
                                                       intent.putExtra("user",logged);
                                                       startActivity(intent);
                                                   }

                                                   @Override
                                                   public void onError(@NonNull Throwable e) {

                                                   }

                                                   @Override
                                                   public void onComplete() {

                                                   }
                                               });
                                   } catch (IOException e) {
                                       throw new RuntimeException(e);
                                   } catch (JSONException e) {
                                       throw new RuntimeException(e);
                                   }
                               }
                           });





                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    isValidate=false;
                }
                }















        });
    }
}