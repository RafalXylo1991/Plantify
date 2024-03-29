package com.example.plantify.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.example.plantify.R;
import com.example.plantify.SQL;

import org.json.JSONException;

import java.io.IOException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class createNewPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_password);

        EditText newPassword = findViewById(R.id.newPassword);
        EditText repeat = findViewById(R.id.repeatNewPassword);

        Button button = findViewById(R.id.resetButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isValideate = true;
                if(newPassword.getText().toString()==null) {
                    isValideate=false;
                    newPassword.setError("Enter new password");
                }
                if(repeat.getText().toString()==null) {
                    isValideate=false;
                    repeat.setError("Enter new password again");
                }
                if(!repeat.getText().toString().equals(newPassword.getText().toString())){
                    isValideate=false;
                    repeat.setError("Passwords have to be the same");
                }
                if(isValideate){
                    try {
                        new SQL().setNewPassword(repeat.getText().toString(),getIntent().getStringExtra("email")).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<String>() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {

                                    }

                                    @Override
                                    public void onNext(@NonNull String s) {
                                           if(s.equals("Password changed")){
                                               startActivity(new Intent(createNewPassword.this, loginView.class));
                                           }
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

            }
        });


    }
}