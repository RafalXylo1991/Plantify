package com.example.plantify.Notices.noticeTypes.picture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.plantify.Helpers.Time;
import com.example.plantify.R;
import com.example.plantify.objects.infodialog;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrintNoticePicture#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrintNoticePicture extends com.example.plantify.Helpers.Fragment {
    private ScaleGestureDetector scaleGestureDetector;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PrintNoticePicture() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrintNoticePicture.
     */
    // TODO: Rename and change types and number of parameters
    public static PrintNoticePicture newInstance(String param1, String param2) {
        PrintNoticePicture fragment = new PrintNoticePicture();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        String path = getArguments().getString("picture");

        View view = inflater.inflate(R.layout.fragment_print_notice_picture, container, false);

        ImageView delete = view.findViewById(R.id.printPictureDelete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infodialog info = new infodialog("Czy chcesz usunąć tą notatke?", getActivity(),"Confirm" );
                info.getYes().setBackgroundColor(getActivity().getResources().getColor(R.color.delete));
                info.getDialog().show();
                info.getYes().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        com.example.plantify.Notices.noticeTypes.picture.File.deleteFromInternalStorage(path,getActivity());
                        info.getDialog().cancel();
                        loadBarContent(new pictureNotice(),1, getActivity(),"dsfdsf");
                    }
                });

            }
        });

        TextView pictureDate = view.findViewById(R.id.dateOfPicture);
        pictureDate.setText("Notatke zrobiono "+ Time.simpleDateFormat.format(new Date(getFile().getFileData(path))));

        Bitmap myBitmap = BitmapFactory.decodeFile(path);

        ImageView image = view.findViewById(R.id.myZoomageView);
        image.setImageBitmap(myBitmap);


        registerForContextMenu(image);

        return view;
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Choose a color");
        // add menu items
        menu.add(0, v.getId(), 0, "Yellow");
        menu.add(0, v.getId(), 0, "Gray");
        menu.add(0, v.getId(), 0, "Cyan");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return super.onContextItemSelected(item);
        
    }
}