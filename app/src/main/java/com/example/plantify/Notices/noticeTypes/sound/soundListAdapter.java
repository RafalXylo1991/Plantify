package com.example.plantify.Notices.noticeTypes.sound;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.plantify.Helpers.Fragment;
import com.example.plantify.Notices.noticeTypes.picture.pictureNotice;
import com.example.plantify.R;
import com.example.plantify.objects.infodialog;
import com.example.plantify.objects.users;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;


public class soundListAdapter extends BaseAdapter {
    Object[] arr;
    private final users user;
    FragmentActivity activity;
    Context c;
    Fragment fragment = new Fragment();
    List<File> items;
    MediaPlayer mediaPlayer;
    noticesList noticesList;



    public soundListAdapter(Context c, List<File> arr, FragmentActivity activity, users user, MediaPlayer mediaPlayer, noticesList noticesList) {
        this.c = c;
        items = arr;

        this.activity = activity;
        this.user=user;
        this.mediaPlayer=mediaPlayer;
        this.noticesList = noticesList;

    }

    @Override public int getCount() { return items.size(); }

    @Override public Object getItem(int i) { return null; }

    @Override public long getItemId(int i) { return 0; }


    @Override
    public View getView(int i, View view,ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.sound_item, null);
        }


        TextView title = view.findViewById(R.id.soundTitle);
        title.setText(items.get(i).getName());
        ImageView startPlay = view.findViewById(R.id.startPlay);
        ImageView stopPlay = view.findViewById(R.id.stopPlay);
        ImageView delete = view.findViewById(R.id.deleteSound);


        startPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException,
                    SecurityException, IllegalStateException {

                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    System.out.println("isplaying");
                    try {
                        mediaPlayer.setDataSource( items.get(i).getPath());
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mediaPlayer.start();
                }else {

                    System.out.println(" not isplaying");
                }



            }
        });
        stopPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infodialog info = new infodialog("Czy chcesz usunąć tą notatke?", c,"Confirm" );
                info.getYes().setBackgroundColor(c.getResources().getColor(R.color.delete));
                info.getDialog().show();
                info.getYes().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new java.io.File(items.get(i).getPath()).delete();
                        info.getDialog().cancel();
                        FragmentManager manager = activity.getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.MenuFragmentPlaceHolder,new noticesList());
                        manager.popBackStack("soundFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        transaction.addToBackStack("soundFragment");
                        transaction.commit();
                    }
                });

               }
        });
        return view;
    }
}