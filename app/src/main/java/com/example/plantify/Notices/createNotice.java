package com.example.plantify.Notices;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.example.plantify.Helpers.Fragment;
import com.example.plantify.R;
import com.example.plantify.Notices.noticeTypes.text.TextNotice;
import com.example.plantify.Notices.noticeTypes.picture.pictureNotice;
import com.example.plantify.Notices.noticeTypes.sound.soundNotice;
import com.example.plantify.Models.PictureNotice.users;

import java.util.ArrayList;
import java.util.List;


public class createNotice extends Fragment {

    String ROOT_FRAGMENT_TAG="root_fragment";
    String message;
    users user;
    public createNotice() {
        // Required empty public constructor
    }


    public static createNotice newInstance(String param1, String param2) {
        createNotice fragment = new createNotice();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final String[] phone = {""};
        List<String > phones = new ArrayList<>();
        setUser(getArguments().getParcelable("user"));
        View view = inflater.inflate(R.layout.fragment_new_totice, container, false);



        ImageView text = view.findViewById(R.id.textNotice);
        ImageView picture = view.findViewById(R.id.pictureNotice);
        ImageView sound = view.findViewById(R.id.soundNotice);

        text.setOnClickListener(v -> {
            loadBarContent(new TextNotice(),1,  getActivity(),  "TextNotice");
        });
        sound.setOnClickListener(v -> {
            loadBarContent(new soundNotice(),1, getActivity(),  "SoundNotice");
        });
        picture.setOnClickListener(v -> {
            loadBarContent(new pictureNotice(),1, getActivity(),  "PictureNotice");
        });







        return view;
    }
    public String filter(String value){
        String phone = "";
        for(int i =0 ; i<=value.length()-1; i++){
            try{
                int x = Integer.parseInt(String.valueOf(value.charAt(i)));

                phone+=String.valueOf(value.charAt(i));

                if(phone.length()==9){
                    long number = Long.parseLong(phone);


                    Log.i("number",String.valueOf(phone).replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1-$2-$3"));
                    return String.valueOf(phone);

                }
            }catch (NumberFormatException error){
                String newValue = value.substring(1);

                filter(newValue);
            }



        };

        return null;
    }

}