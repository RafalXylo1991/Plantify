package com.example.plantify.todolist;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


import com.example.plantify.R;
import com.example.plantify.objects.Task;
import com.example.plantify.objects.ToDoList;
import com.example.plantify.objects.infodialog;
import com.example.plantify.objects.users;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class todolist_print extends AppCompatActivity  {
    Task task;
    List<Task> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_todolist_print);
        ToDoList lista =  getIntent().getParcelableExtra("list");
        users user   =  getIntent().getParcelableExtra("user");
        ProgressBar progress = findViewById(R.id.progressBar);
        TextView title = findViewById(R.id.titleList);
        TextView progressText = findViewById(R.id.progress_text);
        Button save = findViewById(R.id.save);
        Button delete = findViewById(R.id.delteList);


        title.setText(lista.getTitle());
        list = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(getIntent().getStringExtra("tasks"));
            obj.keys().forEachRemaining(new Consumer<String>() {
                @Override
                public void accept(String s) {
                    try {
                        JSONObject json = (JSONObject) obj.get(s);
                        json.keys().forEachRemaining(new Consumer<String>() {
                            @Override
                            public void accept(String ss) {
                                if(!ss.equals("kind")){
                                    task =new Task(Integer.parseInt(s),ss);
                                }

                                try {
                                    if(ss.equals("kind")){
                                        task.setKind(json.getString("kind"));
                                    }else {
                                        task.setDone(json.getBoolean(ss));
                                    }






                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                                ;
                            }
                        });
                        list.add(task);


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

            });


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    infodialog info = new infodialog("Do you want to delete this list?",todolist_print.this,"Confirm");
                    info.getYes().setBackgroundColor(getResources().getColor(R.color.delete));
                    info.getYes().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                            user.delList(user.getAccessToken(),lista).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<Void>() {
                                        @Override
                                        public void onSubscribe(@NonNull Disposable d) {

                                        }

                                        @Override
                                        public void onNext(@NonNull Void unused) {

                                        }

                                        @Override
                                        public void onError(@NonNull Throwable e) {

                                        }

                                        @Override
                                        public void onComplete() {
                                            info.getDialog().cancel();
                                            finish();
                                        }
                                    });
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        }
                    });
                    info.getDialog().show();

            }
        });

        ImageView icon = findViewById(R.id.note);

        icon.setImageDrawable(getResources().getDrawable(R.drawable.todolisticon));
        FrameLayout container = findViewById(R.id.flFragmentPlaceHolder);
        FragmentManager manager = getSupportFragmentManager();
        ListView noticeList = findViewById(R.id.printtaskList);
        TaskAdapter tasks = new TaskAdapter(getApplicationContext(), list,container,manager,user,noticeList,lista,progress,progressText,save,this);
        noticeList.setAdapter(tasks);
    }
}