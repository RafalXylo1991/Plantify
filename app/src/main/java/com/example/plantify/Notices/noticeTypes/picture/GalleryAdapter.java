package com.example.plantify.Notices.noticeTypes.picture;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import androidx.fragment.app.FragmentActivity;


import com.example.plantify.Helpers.Fragment;
import com.example.plantify.R;
import com.example.plantify.Models.PictureNotice.users;

import java.io.File;
import java.util.List;



public class GalleryAdapter extends BaseAdapter {
    Object[] arr;
    private final users user;
    FragmentActivity activity;
    Context c;
    Fragment fragment = new Fragment();
    List<File> items;



    public GalleryAdapter(Context c, List<File> arr, FragmentActivity activity, users user) {
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
            view = inflater.inflate(R.layout.imagellayout, null);
        }





        ImageView imageView = view.findViewById(R.id.imageView);
        Bitmap myBitmap = BitmapFactory.decodeFile(items.get(i).getAbsolutePath());
        imageView.setImageBitmap(myBitmap);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arr = new Object[1];
                arr[0]=myBitmap;
                fragment.loadPrintPictureNoticeFragment(new PrintNoticePicture(), 0,  activity, user,"PrintNoticePicture", items.get(i).getAbsolutePath());
            }
        });
        return view;
    }
}