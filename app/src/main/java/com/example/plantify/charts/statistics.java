package com.example.plantify.charts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.plantify.ExtendClass;
import com.example.plantify.Models.PictureNotice.PieEntires;
import com.example.plantify.Models.PictureNotice.Task;
import com.example.plantify.Models.PictureNotice.ToDoList;
import com.example.plantify.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class statistics extends ExtendClass {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        final PieEntires[] entire = new PieEntires[1];
        final JSONObject[] obj = new JSONObject[1];
        final String[] name = {null};
        final Boolean[] isdone = {null};
        List<PieEntires> list = new ArrayList<>();
        List<PieEntry> entries = new ArrayList<PieEntry>();
        toDoLists.forEach(new Consumer<ToDoList>() {
            @Override
            public void accept(ToDoList toDoList) {

                    JsonObject json =  toDoList.getTasks();
                    try {
                        JSONObject json2 = new JSONObject(json.toString());

                        Iterator<String> keys = json2.keys();
                        while(keys.hasNext()) {
                           entire[0] = new PieEntires();
                            String key = keys.next();
                            String value = json2.getString(key);
                            JSONObject value2 = new JSONObject(value);
                            Iterator<String> keys2 = value2.keys();
                            while(keys2.hasNext()) {


                                String title = keys2.next();
                                String  bool = value2.getString(title);
                                if(!title.equals("date")){


                                    entire[0].setTitle(title);
                                    entire[0].setDone(value2.getString(title).equals("true")?true:false);



                                }else{
                                    String  date = value2.getString(title);
                                    entire[0].setEndDate(getTime().getSimpleDateFormat().parse(date));
                                    name[0] =title;
                                    isdone[0] = bool.equals("true")?true:false;

                                }

                            }
                            list.add(entire[0]);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
            }


        });

       list.forEach(new Consumer<PieEntires>() {
           @Override
           public void accept(PieEntires pieEntires) {

           }
       });
        PieData data = new PieData();
        PieDataSet set = new PieDataSet(entries, "cycki"    );
        PieChart chart = findViewById(R.id.chart);
        data.setDataSet(set);
        chart.setData(data);
        chart.invalidate();




    }
}