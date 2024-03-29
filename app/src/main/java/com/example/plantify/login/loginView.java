package com.example.plantify.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.plantify.Menu;
import com.example.plantify.R;
import com.example.plantify.SQL;
import com.example.plantify.objects.users;

import org.json.JSONException;

import java.io.IOException;
import java.util.stream.StreamSupport;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class loginView extends AppCompatActivity {

    private users userr;
    String token;
    private users userr2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_view);
        TextView name = findViewById(R.id.userName);
        TextView password = findViewById(R.id.userPassword);

        TextView resetPassword = findViewById(R.id.resetPassword);
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginView.this,resetPassword.class));
            }
        });

        Button login = findViewById(R.id.log);
        login.setBackgroundColor(getResources().getColor(R.color.mainLogin));
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                      Boolean isValid=true;


                      if(password.getText().toString().equals("")){
                          isValid=false;
                          password.setError(Html.fromHtml("<font color='red'>Enter the password</font>"));

                      }
                      if(isValid){
                          SQL sql=new SQL();

                             userr2=new users();
                              userr2.getToken().subscribeOn(Schedulers.io())
                                              .observeOn(AndroidSchedulers.mainThread())
                                                      .subscribe(new Observer<String>() {
                                                          @Override
                                                          public void onSubscribe(@NonNull Disposable d) {

                                                          }

                                                          @Override
                                                          public void onNext(@NonNull String s) {
                                                            token=s;
                                                          }

                                                          @Override
                                                          public void onError(@NonNull Throwable e) {

                                                          }

                                                          @Override
                                                          public void onComplete() {
                                                              try {
                                                              sql.Login(name.getText().toString(),password.getText().toString(),token).subscribeOn(Schedulers.io())
                                                                      .observeOn(AndroidSchedulers.mainThread())
                                                                      .subscribe(new Observer<users>() {
                                                                          @Override
                                                                          public void onSubscribe(@NonNull Disposable d) {

                                                                          }

                                                                          @Override
                                                                          public void onNext(@NonNull users users) {
                                                                              userr=users;
                                                                              System.out.println(users+"cipeczka");
                                                                          }

                                                                          @Override
                                                                          public void onError(@NonNull Throwable e) {

                                                                              Toast toast = Toast.makeText(getApplicationContext(), "Nie ma takiego użytkownika lub dane nie są poprawne", Toast.LENGTH_SHORT);
                                                                              toast.show();
                                                                          }

                                                                          @Override
                                                                          public void onComplete() {
                                                                              Intent intent = new Intent(loginView.this, Menu.class);
                                                                              intent.putExtra("user", userr);

                                                                              finish();
                                                                              startActivity(intent);
                                                                          }
                                                                      });
                                                          } catch (IOException e) {
                                                              throw new RuntimeException(e);
                                                          } catch (JSONException e) {
                                                              throw new RuntimeException(e);
                                                          }
                                                          }
                                                      });

                      }





            }
        });

    }
}