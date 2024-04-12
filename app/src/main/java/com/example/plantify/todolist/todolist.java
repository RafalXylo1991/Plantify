package com.example.plantify.todolist;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.plantify.ExtendClass;
import com.example.plantify.R;
import com.example.plantify.objects.Task;
import com.example.plantify.objects.ToDoList;
import com.example.plantify.objects.users;
import com.google.gson.JsonObject;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.function.Consumer;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class todolist extends ExtendClass {

    List<Task> taskList= new ArrayList<>();
    DatePickerDialog picker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);

        users user = getIntent().getParcelableExtra("user");
        ImageView icon = findViewById(R.id.note);
        icon.setImageDrawable(getResources().getDrawable(R.drawable.todolisticon));
        TextView task = findViewById(R.id.tasktitle);
        TextView title = findViewById(R.id.title);
        ImageView addTask = findViewById(R.id.addTask);
        TextView setDay = findViewById(R.id.startDate);
        Button add = findViewById(R.id.addToDoList);

        ListView noticeList = findViewById(R.id.taskList);
        noticeList.setDivider(getDrawable(R.color.black));
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(task.getText().toString().equals("")){

                    task.setError("Enter the task");
                }else{
                    taskList.add(new Task(taskList.size()+1,task.getText().toString()));
                    FrameLayout container = findViewById(R.id.flFragmentPlaceHolder);
                    FragmentManager manager = getSupportFragmentManager();
                    addTaskAdapter tasks = new addTaskAdapter(getApplicationContext(), taskList,container,manager,null,noticeList);





                    noticeList.setAdapter(tasks);

                    task.setText("");
                }


            }
        });
        setDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(todolist.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                setDay.setText(getTime().DatePicerkFormatToDate(dayOfMonth,monthOfYear,year));

                            }
                        }, year, month, day);
                picker.show();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isValid=true;
                JsonObject tasks = new JsonObject();




                if(title.getText().toString().equals("")){
                    isValid=false;
                    title.setError("Enter the title");
                }
                if(setDay.getText().toString().equals("")){
                    isValid=false;
                    setDay.setError("Set the day");
                }
                if(taskList.size()==0){
                    isValid=false;

                }



                taskList.forEach(new Consumer<Task>() {
                    @Override
                    public void accept(Task task) {
                        JsonObject json = new JsonObject();
                        json.addProperty(task.getTitle(),task.isDone());
                        tasks.add(String.valueOf(task.getId()),json);


                    }
                });

                if(isValid){
                    try {
                        user.addToDoList(new ToDoList(0,title.getText().toString(),setDay.getText().toString(),tasks,false,0), user.getAccessToken(),user.getId()).subscribeOn(Schedulers.io())
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
                                        finish();
                                    }
                                });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }


            }
        });
    }
}