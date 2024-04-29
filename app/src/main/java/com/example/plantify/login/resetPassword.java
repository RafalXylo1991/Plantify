package com.example.plantify.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.example.plantify.ExtendClass;
import com.example.plantify.R;
import com.example.plantify.Models.PictureNotice.users;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class resetPassword extends ExtendClass {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        TextView password = findViewById(R.id.passwordFieldReset);
        TextView reset = findViewById(R.id.resetButton);


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if(password.getText().toString().matches(emailPattern)) {
                    try {
                        new users().resetPassword(password.getText().toString(), getBaseContext() ).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<String>() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {

                                    }

                                    @Override
                                    public void onNext(@NonNull String s) {
                                        try {

                                            if(s.equals("Nie znaleziono użytkownika"))
                                            {
                                                Toast.makeText(getApplicationContext(), "Nie ma użytkownika z podanym adresem email", Toast.LENGTH_LONG).show();
                                            }else{
                                                JSONObject result = new JSONObject(s);
                                                if(result.get("message").equals("account founded")) {
                                                    Intent intent = new Intent(resetPassword.this, enterResetKey.class);
                                                    intent.putExtra("email", password.getText().toString());
                                                    startActivity(intent);
                                                }
                                            }
                                        } catch (JSONException e) {
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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
                }else{
                    Toast.makeText(getApplicationContext(), "Format jest nie poprawny", Toast.LENGTH_LONG).show();;
                }

            }
        });
    }
}