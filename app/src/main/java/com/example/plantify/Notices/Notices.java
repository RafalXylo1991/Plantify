package com.example.plantify.Notices;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.plantify.MainActivity;
import com.example.plantify.R;
import com.example.plantify.menuContents.Profile;
import com.example.plantify.objects.Notice;
import com.example.plantify.objects.users;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Notices extends AppCompatActivity {
    NavigationView navigationView;
    users user;
    Boolean important;

    SwipeRefreshLayout swipe;

    List<Notice> notices=new ArrayList<>();
    String ROOT_FRAGMENT_TAG="root_fragment";
 DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notices);
        drawerLayout=findViewById(R.id.noticeDrawer);
        navigationView=findViewById(R.id.navigation_view);
        Toolbar  toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        user = getIntent().getParcelableExtra("user");
        important = getIntent().getBooleanExtra("important",false);
       ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.OpenDrawer,R.string.CloseDrawer);

        drawerLayout.addDrawerListener(toggle);
        try {
            loadData();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id==R.id.profile){

                    loadBarContent(new Profile(),0,"",null);


                }
                if(id==R.id.logOut){

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {



                        }});

                    thread.start();
                    try {
                        user.logOut(user.getAccessToken());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
                drawerLayout.closeDrawer(GravityCompat.START);


                return false;
            }
        });

    }

    public void loadData() throws JSONException, IOException {

         user.getNotices(user.getAccessToken(),user.getId()).subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Notice>() {
                     @Override
                     public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                     }

                     @Override
                     public void onNext(@io.reactivex.rxjava3.annotations.NonNull Notice notice) {

                         if(!important){
                             notices.add(notice);
                         }else{
                             if( notice.getImportant()) {

                                 notice.setId( notice.getId());
                                 notices.add(notice);
                             }


                         }
                        
                     }

                     @Override
                     public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                e.printStackTrace();
                     }

                     @Override
                     public void onComplete() {
                         FrameLayout container = findViewById(R.id.flFragmentPlaceHolder);
                         FragmentManager manager = getSupportFragmentManager();
                         listViewAdapter numbersArrayAdapter = new listViewAdapter(getApplicationContext(), notices,container,manager,user,important);

                         ListView noticeList = findViewById(R.id.listView);



                         noticeList.setAdapter(numbersArrayAdapter);
                     }
                 });






    }

    public void loadBarContent(Fragment fragment, int flag, String value,Notice notice){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Bundle bundle=new Bundle();
        bundle.putString("date",value);
        bundle.putParcelable("user",user);
        bundle.putParcelable("notice",notice);

        fragment.setArguments(bundle);

        if(flag==0){
            transaction.replace(R.id.flFragmentPlaceHolder,fragment);
            manager.popBackStack(ROOT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            transaction.addToBackStack(ROOT_FRAGMENT_TAG);

        }else{
            transaction.replace(R.id.flFragmentPlaceHolder,fragment);
            transaction.addToBackStack(null);

        }

        transaction.commit();
    }


}
