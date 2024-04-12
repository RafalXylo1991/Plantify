package com.example.plantify.todolist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;


import com.example.plantify.R;
import com.example.plantify.objects.Task;
import com.example.plantify.objects.ToDoList;
import com.example.plantify.objects.users;
import com.google.gson.JsonObject;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class TaskAdapter extends ArrayAdapter<Task> {
    todolist_print activity;
    Context context;
    TextView progressText;
    ProgressBar progress;
    ToDoList lista;
    ListView noticeList;
    users user;
    FragmentManager manage;
    TextView title;
    TaskAdapter adapter;

    List<Task> arrayList;
    CheckBox check;


    Button save;




    public TaskAdapter(@NonNull Context context, List<Task> arrayList, FrameLayout container, FragmentManager manager, users user, ListView noticeList, ToDoList lista, ProgressBar progress, TextView progressText, Button save, todolist_print todolistPrint) {


        super(context, 0, arrayList);
        this.manage=manager;
        this.progress=progress;
        this.context=context;
        this.user=user;
        this.activity=todolistPrint;
        this.noticeList=noticeList;
        adapter=this;

        this.arrayList=arrayList;
        this.lista=lista;
        this.save=save;
        this.progressText=progressText;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View currentItemView = convertView;







        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.print_task_list_item, parent, false);
        }


        Task currentNumberPosition = getItem(position);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lista.setDone(true);
                JsonObject jsonObject = new JsonObject();
                arrayList.forEach(new Consumer<Task>() {
                    @Override
                    public void accept(Task task) {
                        JsonObject json = new JsonObject();
                        json.addProperty(task.getTitle(), task.isDone());
                        jsonObject.add(String.valueOf(task.getId()), json);


                    }
                });
                Log.i("json", String.valueOf(jsonObject));
                lista.setTasks(jsonObject);


                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(setProgresss()==100){
                            lista.setDone(true);
                        }else{
                            lista.setDone(false);
                        }

                    }
                });
                if(arrayList.stream().filter(element->element.isDone()==true).collect(Collectors.toList()).size()<arrayList.size()){
                    lista.setDone(false);
                }
                try {
                    user.updateList(lista, user.getAccessToken(), lista.getId()).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<Void>() {
                                @Override
                                public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                                }

                                @Override
                                public void onNext(@io.reactivex.rxjava3.annotations.NonNull Void unused) {

                                }

                                @Override
                                public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                                }

                                @Override
                                public void onComplete() {
                                    activity.finish();
                                }
                            });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }});
        title = currentItemView.findViewById(R.id.askTitle);

        title.setText(currentNumberPosition.getTitle());

        check = currentItemView.findViewById(R.id.checkbox);
        check.setChecked(currentNumberPosition.isDone());

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                View view= (View) buttonView.getParent();
                TextView cipka = view.findViewById(R.id.askTitle);
                if(!currentNumberPosition.isDone()){
                    if(isChecked&&cipka.getText().toString().equals(currentNumberPosition.getTitle())){
                        arrayList.get(position).setDone(true);
                        cipka.setText("Done");
                        progress.setProgress(setProgresss());
                    }else{
                        arrayList.get(position).setDone(false);
                        progress.setProgress(setProgresss());
                    }
                }
                else{
                    if(!isChecked&&cipka.getText().toString().equals(currentNumberPosition.getTitle())){
                        arrayList.get(position).setDone(false);
                        cipka.setText(currentNumberPosition.getTitle());
                        progress.setProgress(setProgresss());
                    }
                }





                boolean is= arrayList.stream().allMatch(check->check.isDone()==true);






            }
        });


        progress.setProgress(setProgresss());





        return currentItemView;
    }

    private int setProgresss() {
        int procent = (arrayList.stream().filter(check->check.isDone()==true).collect(Collectors.toList()).size()*100)/arrayList.size();
        progressText.setText(procent+"%");
        lista.setProgress(procent);
        if(procent==100){
            progressText.setTextColor(context.getResources().getColor(R.color.checked));
        }else{
            progressText.setTextColor(context.getResources().getColor(R.color.black));
        }
        return procent;
    }

}
