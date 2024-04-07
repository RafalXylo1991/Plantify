package com.example.plantify.Notices.noticeTypes.sound;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.plantify.Helpers.Fragment;
import com.example.plantify.PrintNoticePicture;
import com.example.plantify.R;
import com.example.plantify.objects.users;

import java.io.File;
import java.util.List;


public class soundListAdapter extends BaseAdapter {
    Object[] arr;
    private final users user;
    FragmentActivity activity;
    Context c;
    Fragment fragment = new Fragment();
    List<File> items;



    public soundListAdapter(Context c, List<File> arr, FragmentActivity activity, users user) {
        this.c = c;
        items = arr;

        this.activity = activity;
        this.user=user;

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




        return view;
    }
}