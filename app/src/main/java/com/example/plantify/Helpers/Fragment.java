package com.example.plantify.Helpers;



import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.plantify.R;
import com.example.plantify.objects.infodialog;
import com.example.plantify.objects.users;

public  class Fragment extends androidx.fragment.app.Fragment {

    private static users user = new users();
    private static File File = new File();



    public static users getUser() {
        return user;
    }

    public static void setUser(users user) {
        Fragment.user = user;
    }

    public static com.example.plantify.Helpers.File getFile() {
        return File;
    }

    public  void loadBarContent(androidx.fragment.app.Fragment fragment, int flag, FragmentActivity context,  String tag){
        FragmentManager manager = context.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();


        Bundle bundle=new Bundle();
        bundle.putParcelable("user",getUser());
        fragment.setArguments(bundle);

        fragment.setArguments(bundle);


            transaction.replace(R.id.noticeTypeContainer,fragment);
            if(flag==0){
                manager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                transaction.addToBackStack(tag);
            }



        Fragment frag= (Fragment) context.getSupportFragmentManager().findFragmentByTag(tag);
        if (frag != null && frag.isVisible()) {

            // if it is fragment one we are displaying a toast message.
            Toast.makeText(context, "Fragment One is visibile", Toast.LENGTH_SHORT).show();
        }

        transaction.commit();
    }

    public  void loadPrintPictureNoticeFragment(androidx.fragment.app.Fragment fragment, int flag, FragmentActivity context, users user, String tag, String imagePath){
        FragmentManager manager = context.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();


        Bundle bundle=new Bundle();
        bundle.putParcelable("user",user);
        bundle.putString("picture",imagePath);
        fragment.setArguments(bundle);

        fragment.setArguments(bundle);


        transaction.replace(R.id.noticeTypeContainer,fragment);
        if(flag==0){
            manager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            transaction.addToBackStack(tag);
        }



        Fragment frag= (Fragment) context.getSupportFragmentManager().findFragmentByTag(tag);
        if (frag != null && frag.isVisible()) {

            // if it is fragment one we are displaying a toast message.
            Toast.makeText(context, "Fragment One is visibile", Toast.LENGTH_SHORT).show();
        }

        transaction.commit();
    }
}
