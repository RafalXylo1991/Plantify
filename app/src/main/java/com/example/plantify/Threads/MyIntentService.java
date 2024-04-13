package com.example.plantify.Threads;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;


import com.example.plantify.Models.PictureNotice.users;

import java.util.ArrayList;
import java.util.List;

public class MyIntentService extends IntentService {

    public static final String ACTION_MyIntentService = "com.example.androidintentservice.RESPONSE";
    public static final String ACTION_MyUpdate = "com.example.androidintentservice.UPDATE";
    public static final String EXTRA_KEY_IN = "EXTRA_IN";
    public static final String EXTRA_KEY_OUT = "EXTRA_OUT";
    public static final String EXTRA_KEY_UPDATE = "EXTRA_UPDATE";
    String msgFromActivity;
    String extraOut;
    List<Boolean> lista=new ArrayList<>();
    users user;

    public MyIntentService() {
        super("com.example.androidintentservice.MyIntentService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        int cycki = super.onStartCommand(intent, flags, startId);
        System.out.println("oncommand");
        user = intent.getParcelableExtra("user");
        return cycki;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        System.out.println("handled");
        //get input
        msgFromActivity = intent.getStringExtra(EXTRA_KEY_IN);
        extraOut = "Hello: " +  msgFromActivity;



        //return result
        Intent intentResponse = new Intent();
        intentResponse.setAction(ACTION_MyIntentService);
        intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
        intentResponse.putExtra(EXTRA_KEY_OUT, (CharSequence) lista);
        sendBroadcast(intentResponse);
    }

}