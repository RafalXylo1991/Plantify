package com.example.plantify.noticeTypes.picture;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.example.plantify.Helpers.Fragment;
import com.example.plantify.Menu;
import com.example.plantify.R;
import com.example.plantify.objects.users;

import java.util.Arrays;
import java.util.List;


public class pictureNotice extends Fragment {

    private static final int pic_id = 123;
    // Define the button and imageview type variable
    Button camera_open_id;
    String ROOT_FRAGMENT_TAG="root_fragment";
    GalleryAdapter customGalleryAdapter;
    GridView gallery;

    public static pictureNotice newInstance(String param1, String param2) {
        pictureNotice fragment = new pictureNotice();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_picture_notice, container, false);
        camera_open_id = view.findViewById(R.id.camera_button);



        ((Menu) getActivity()).disableSwipe();
        gallery=view.findViewById(R.id.NoticeGallery);




        List<java.io.File> pictures= Arrays.asList(File.getImages(getActivity().getApplicationContext(), getUser()));
        for (java.io.File file: pictures
             ) {
            System.out.println(file.getPath());
        }
         customGalleryAdapter = new GalleryAdapter(getActivity(), pictures, getActivity(), getUser());
        gallery.setAdapter(customGalleryAdapter);
        camera_open_id.setOnClickListener(v -> {

            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            startActivityForResult(camera_intent, pic_id);
        });


        return view;
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            File.saveToInternalStorage(photo, getActivity().getApplicationContext(), getUser());
            loadBarContent(new pictureNotice(),1, getActivity(),  "PictureNotice");
        }
    }


}


