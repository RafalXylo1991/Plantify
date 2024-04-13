package com.example.plantify.todolist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.example.plantify.R;
import com.example.plantify.Models.PictureNotice.Notice;
import com.example.plantify.Models.PictureNotice.Task;
import com.example.plantify.Models.PictureNotice.users;

import java.util.List;

public class addTaskAdapter extends ArrayAdapter<Task> {
    ListView noticeList;
    users user;
    FragmentManager manage;
    EditText title;
    addTaskAdapter adapter;
    String ROOT_FRAGMENT_TAG="root_fragment";
    List<Task> arrayList;
    public addTaskAdapter(@NonNull Context context, List<Task> arrayList, FrameLayout container, FragmentManager manager, users user, ListView noticeList) {


        super(context, 0, arrayList);
        this.manage=manager;
        this.user=user;
        this.noticeList=noticeList;
        adapter=this;
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View currentItemView = convertView;


        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.add_task_list_item, parent, false);
        }


        Task currentNumberPosition = getItem(position);


          ImageView editImage = currentItemView.findViewById(R.id.editTask);
        editImage.setImageDrawable(currentItemView.getResources().getDrawable(R.drawable.save));
        ImageView deleteImage = currentItemView.findViewById(R.id.deleteTask);
        deleteImage.setImageDrawable(currentItemView.getResources().getDrawable(R.drawable.delete3));
         title = currentItemView.findViewById(R.id.askTitle);
        title.setText(currentNumberPosition.getTitle());
        TextView id = currentItemView.findViewById(R.id.taskId);
        id.setText(String.valueOf(currentNumberPosition.getId())+".");

        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.remove(getItem(position));
            }
        });
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getItem(position).setTitle(title.getText().toString());
            }
        });


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
