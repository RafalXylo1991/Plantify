package com.example.plantify.Notices.noticeTypes.sound;

import android.content.Context;
import android.content.ContextWrapper;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.plantify.Notices.noticeTypes.picture.GalleryAdapter;
import com.example.plantify.R;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link noticesList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class noticesList extends com.example.plantify.Helpers.Fragment {



    public noticesList() {
        // Required empty public constructor
    }


    public static noticesList newInstance(String param1, String param2) {
        noticesList fragment = new noticesList();
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
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_notices_list, container, false);
        ImageView soundIcon = view.findViewById(R.id.note);
        soundIcon.setImageDrawable(getActivity().getDrawable(R.drawable.soundnoticeicon));
        ContextWrapper cw = new ContextWrapper(getActivity());
        ListView galery = view.findViewById(R.id.SoundGallery);
        java.io.File directory = cw.getDir(String.valueOf(getUser().getId()+"_audio"), Context.MODE_PRIVATE);

        soundListAdapter adapter = new soundListAdapter(getActivity(), Arrays.asList(directory.listFiles()), getActivity(), getUser());
        galery.setAdapter(adapter);
        return view;
    }
}