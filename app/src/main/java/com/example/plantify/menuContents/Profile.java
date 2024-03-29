package com.example.plantify.menuContents;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


import com.example.plantify.MainActivity;
import com.example.plantify.R;
import com.example.plantify.objects.infodialog;
import com.example.plantify.objects.users;

import org.json.JSONException;

import java.io.IOException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class Profile extends Fragment {


    users user;
    View view;
    public Profile() {
        // Required empty public constructor
    }

    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        user = getArguments().getParcelable("user");

        view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView userName= view.findViewById(R.id.id);
        TextView userNameEdit= view.findViewById(R.id.userNameEdit);
        TextView email= view.findViewById(R.id.email);
        TextView pin= view.findViewById(R.id.pin);
        TextView password= view.findViewById(R.id.password);
        Button update = view.findViewById(R.id.update);
        Button delete = view.findViewById(R.id.delete);
        userName.setText(user.getName().replace("\"", ""));
        userNameEdit.setText(user.getName().replace("\"", ""));
        email.setText(user.getEmail().replace("\"", ""));


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     infodialog dialogs = new infodialog("Are you sure you want to update your  profile?",getContext(),"Confirm");
                     dialogs.getDialog().show();
                     dialogs.getYes().setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {




                                         user.setName(userNameEdit.getText().toString());
                                         user.setEmail(email.getText().toString());
                                         if(password.getText().toString().equals("")){
                                             user.setPassword(pin.getText().toString());
                                         }else {
                                             user.setPassword("");
                                         }


                             try {
                                 user.updateUser(user.getAccessToken(),user).subscribeOn(Schedulers.io())
                                         .observeOn(AndroidSchedulers.mainThread())
                                         .subscribe(new Observer<Void>() {
                                             @Override
                                             public void onSubscribe(@NonNull Disposable d) {

                                             }

                                             @Override
                                             public void onNext(@NonNull Void unused) {

                                             }

                                             @Override
                                             public void onError(@NonNull Throwable e) {

                                             }

                                             @Override
                                             public void onComplete() {
                                                  dialogs.getDialog().cancel();
                                             }
                                         });
                             } catch (IOException e) {
                                 throw new RuntimeException(e);
                             } catch (JSONException e) {
                                 throw new RuntimeException(e);
                             }


                         }
                     });
                     dialogs.getNo().setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             dialogs.getDialog().cancel();
                         }
                     });

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infodialog dialogs = new infodialog("Are you sure you want to delete your  profile?",getContext(),"Confirm");
                dialogs.getDialog().show();
                dialogs.getYes().setBackgroundColor(getResources().getColor(R.color.delete));
                dialogs.getYes().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        try {
                            user.deleteUser(user.getAccessToken(),user).subscribeOn(Schedulers.io())
                                            .subscribeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(new Observer<Void>() {
                                                        @Override
                                                        public void onSubscribe(@NonNull Disposable d) {

                                                        }

                                                        @Override
                                                        public void onNext(@NonNull Void unused) {

                                                        }

                                                        @Override
                                                        public void onError(@NonNull Throwable e) {

                                                        }

                                                        @Override
                                                        public void onComplete() {
                                                            startActivity(new Intent(getContext(), MainActivity.class));
                                                        }
                                                    });
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }



                    }
                });
                dialogs.getNo().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogs.getDialog().cancel();
                    }
                });

            }
        });
        return view;
    }
}