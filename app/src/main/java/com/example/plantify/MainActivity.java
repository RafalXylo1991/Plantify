package com.example.plantify;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.plantify.login.loginView;
import com.example.plantify.login.registerView;
import com.example.plantify.notifications.CreateChannel;
import com.example.plantify.Models.PictureNotice.users;
import com.example.plantify.services.backgroundService;
import com.example.plantify.services.undefinedItemsService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;

import java.io.IOException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    public users logedUser;
    public  users user;
    FirebaseAuth auth;
    GoogleSignInClient googleSignInClient;

    ImageView imageView;
    ImageView loginImage;


    SQL sql;
    String token;
    static IntentFilter s_intentFilter;
    CreateChannel create;
    public static final int VARIABLE_DSFDSF0 = 1024;
    static {
        s_intentFilter = new IntentFilter();
        s_intentFilter.addAction(Intent.ACTION_TIME_TICK);
        s_intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        s_intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sql= new SQL();
        logedUser=null;
        NotificationManager manager = getSystemService(NotificationManager.class);
        create=new CreateChannel(manager,"cycki");

       if(getIntent().getBooleanExtra("logout",false)){
           FirebaseMessaging.getInstance().getToken()
                   .addOnCompleteListener(new OnCompleteListener<String>() {
                       @Override
                       public void onComplete(@NonNull Task<String> task) {
                           FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                               @Override
                               public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                   if (firebaseAuth.getCurrentUser() == null) {
                                       googleSignInClient.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void unused) {
                                           }
                                       });

                                   }
                               }
                           });
                           FirebaseAuth.getInstance().signOut();
                       }
                   });
       }
       FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
        imageView = findViewById(R.id.imageView);
        FirebaseApp.initializeApp(this);

        loginImage = findViewById(R.id.googleicon);

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.clientId))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(MainActivity.this, options);

        auth = FirebaseAuth.getInstance();
         stopService(new Intent(getApplicationContext(), backgroundService.class));
         stopService(new Intent(getApplicationContext(), undefinedItemsService.class));
        if (logedUser != null) {

            Toast.makeText(this, String.valueOf(logedUser.isLogged), Toast.LENGTH_LONG).show();
        }

        TextView regedit = (TextView) findViewById(R.id.regedit);

        regedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, registerView.class));
            }
        });

        loginImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = googleSignInClient.getSignInIntent();
                activityResultLauncher.launch(intent);
            }
        });

        TextView logging = (TextView) findViewById(R.id.textView3);
        logging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, loginView.class);
                startActivityForResult(intent, VARIABLE_DSFDSF0);
            }
        });
    }

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                try {
                    GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
                    AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
                    auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (auth.getCurrentUser() != null) {
                                    user = new users();
                                    user.getToken().subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Observer<String>() {
                                                @Override
                                                public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                                                }

                                                @Override
                                                public void onNext(@io.reactivex.rxjava3.annotations.NonNull String s) {
                                                    token=s;
                                                }

                                                @Override
                                                public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                                                }

                                                @Override
                                                public void onComplete() {
                                                    sql.addUser(auth.getCurrentUser().getDisplayName(),auth.getCurrentUser().getEmail()).subscribeOn(Schedulers.io())
                                                            .observeOn(AndroidSchedulers.mainThread())
                                                            .subscribe(new Observer<Void>() {
                                                                @Override
                                                                public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                                                                }

                                                                @Override
                                                                public void onNext(@io.reactivex.rxjava3.annotations.NonNull Void unused) {

                                                                }

                                                                @Override
                                                                public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                                                                }

                                                                @Override
                                                                public void onComplete() {

                                                                    try {
                                                                        sql.Login(auth.getCurrentUser().getDisplayName(),null,token, getApplicationContext()).subscribeOn(Schedulers.io())
                                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                                .subscribe(new Observer<users>() {
                                                                                    @Override
                                                                                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                                                                                    }

                                                                                    @Override
                                                                                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull users users) {
                                                                                        if(users.getEmail()==null){
                                                                                            logedUser=users;
                                                                                        }else{

                                                                                            logedUser=users;
                                                                                        }


                                                                                    }

                                                                                    @Override
                                                                                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                                                                                    }

                                                                                    @Override
                                                                                    public void onComplete() {
                                                                                        if(logedUser.getEmail()==null){

                                                                                        }else {
                                                                                            Intent intent = new Intent(MainActivity.this, Menu.class);
                                                                                            intent.putExtra("user",logedUser);


                                                                                            startActivity(intent);
                                                                                            finish();

                                                                                        }

                                                                                    }
                                                                                });
                                                                    } catch (IOException e) {
                                                                        throw new RuntimeException(e);
                                                                    } catch (JSONException e) {
                                                                        throw new RuntimeException(e);
                                                                    }


                                                                }
                                                            });
                                                    System.out.println(auth.getCurrentUser().getDisplayName());

                                                }
                                            });






                                }else{

                                }


                            }
                        }
                    });
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            logedUser = data.getParcelableExtra("value");
            System.out.println("You are logged");
        }
    }
        }
