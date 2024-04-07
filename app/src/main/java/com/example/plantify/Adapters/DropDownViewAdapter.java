package com.example.plantify.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.example.plantify.R;
import com.example.plantify.Models.PictureNotice.Notice;
import com.example.plantify.objects.users;

import java.util.List;



public class DropDownViewAdapter extends ArrayAdapter<String > {

     String type;
    users user;
    FragmentManager manage;
    String ROOT_FRAGMENT_TAG="root_fragment";
    public DropDownViewAdapter(@NonNull Context context, List<String> arrayList, FrameLayout container, users user, String type) {

        super(context, 0, arrayList);
        this.type=type;
        this.user=user;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View currentItemView = convertView;


        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.drop_down_item, parent, false);
        }

        String currentNumberPosition = getItem(position);

        TextView text= currentItemView.findViewById(R.id.item_title);
        ImageView img = currentItemView.findViewById(R.id.image);
        switch (type){
            case "Event":
                img.setImageDrawable(getContext().getResources().getDrawable(R.drawable.event));
                text.setTextColor(getContext().getResources().getColor(R.color.mainLogin));
                break;
            case "List":
                img.setImageDrawable(getContext().getResources().getDrawable(R.drawable.todolist));
                text.setTextColor(getContext().getResources().getColor(R.color.noticecolor));
                break;
            case "Notice":
                img.setImageDrawable(getContext().getResources().getDrawable(R.drawable.notice));
                text.setTextColor(getContext().getResources().getColor(R.color.todocolor));
                break;
            case "InternationalDay":
                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                break;
        }




        text.setText(currentNumberPosition);





        return currentItemView;
    }
    public void loadBarContent(Fragment fragment, int flag, Notice notice){

        FragmentTransaction transaction = manage.beginTransaction();

        Bundle bundle=new Bundle();

        bundle.putParcelable("notice",notice);
        bundle.putParcelable("user",user);

        fragment.setArguments(bundle);

        if(flag==0){
            transaction.replace(R.id.flFragmentPlaceHolder,fragment);
            manage.popBackStack(ROOT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            transaction.addToBackStack(ROOT_FRAGMENT_TAG);

        }else{
            transaction.replace(R.id.flFragmentPlaceHolder,fragment);
            transaction.addToBackStack(null);

        }

        transaction.commit();
    }
}
