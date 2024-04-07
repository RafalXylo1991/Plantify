package com.example.plantify.Notices;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class listViewAdapter extends ArrayAdapter<Notice> {
    Boolean important;
    users user;
    FragmentManager manage;
    String ROOT_FRAGMENT_TAG="root_fragment";
    public listViewAdapter(@NonNull Context context, List<Notice> arrayList, FrameLayout container, FragmentManager manager, users user, Boolean important) {

        super(context, 0, arrayList);
        this.manage=manager;
        this.user=user;
        this.important=important;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View currentItemView = convertView;


        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.listview_item, parent, false);
        }


        Notice currentNumberPosition = getItem(position);


        LinearLayout element = currentItemView.findViewById(R.id.linearLayout);
        element.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            loadBarContent(new printNotice(),0,currentNumberPosition);
            }
        });
        ImageView importantImage = currentItemView.findViewById(R.id.important);
        if(important){

            importantImage.setVisibility( View.VISIBLE);
        }else{
            importantImage.setVisibility( View.GONE);
        }


        TextView title = currentItemView.findViewById(R.id.title);
        title.setText(currentNumberPosition.getTitle().replace("\"", ""));

        TextView subject = currentItemView.findViewById(R.id.subject);
        subject.setText( currentNumberPosition.getSubject().replace("\"", ""));

        TextView text = currentItemView.findViewById(R.id.noticeText);
        text.setText(currentNumberPosition.getText().replace("\"", ""));

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
