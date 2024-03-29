package com.example.plantify.Notices;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.example.plantify.R;
import com.example.plantify.noticeTypes.TextNotice;
import com.example.plantify.noticeTypes.pictureNotice;
import com.example.plantify.noticeTypes.soundNotice;
import com.example.plantify.objects.Notice;
import com.example.plantify.objects.users;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class newTotice extends Fragment {

    String ROOT_FRAGMENT_TAG="root_fragment";
    String message;
    users user;
    public newTotice() {
        // Required empty public constructor
    }


    public static newTotice newInstance(String param1, String param2) {
        newTotice fragment = new newTotice();
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
         user = getArguments().getParcelable("user");

        View view = inflater.inflate(R.layout.fragment_new_totice, container, false);



        ImageView text = view.findViewById(R.id.textNotice);
        ImageView picture = view.findViewById(R.id.pictureNotice);
        ImageView sound = view.findViewById(R.id.soundNotice);

        text.setOnClickListener(v -> {
           loadType(new TextNotice(),1);
        });
        sound.setOnClickListener(v -> {
            loadType(new soundNotice(),1);
        });
        picture.setOnClickListener(v -> {
            loadType(new pictureNotice(),1);
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
    public void loadType(Fragment fragment, int flag){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Bundle bundle=new Bundle();
        bundle.putParcelable("user",user);
        fragment.setArguments(bundle);

        if(flag==0){
            transaction.replace(R.id.noticeTypeContainer,fragment);
            manager.popBackStack(ROOT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            transaction.addToBackStack(ROOT_FRAGMENT_TAG);

        }else{
            transaction.replace(R.id.noticeTypeContainer,fragment);
            transaction.addToBackStack(null);

        }

        transaction.commit();
    }
}