package com.example.plantify;

import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.applandeo.materialcalendarview.EventDay;

import com.example.plantify.menuContents.Profile;
import com.example.plantify.objects.Event;
import com.example.plantify.objects.infodialog;
import com.example.plantify.objects.users;
import com.example.plantify.services.backgroundService;
import com.example.plantify.services.undefinedItemsService;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Evemts extends AppCompatActivity  {
    private static final String ROOT_FRAGMENT_TAG = "fragmetnt";
    DrawerLayout drawerLayout;
    TextView startDate;
    TextView endDate;
    EditText startTime;
    EditText endTime;
    View view;
    DatePickerDialog picker;
    TimePickerDialog timepicker;
    Button add;
    Button cancel;
    SQL sql;
    EditText title;
    infodialog dialog;
    EditText desc;
    users user;
    Spinner spinner;
    Switch powiadomieniaSwitch;

    NavigationView navigationView;
    Toolbar toolbar;

    Calendar calendar;
    List<Event> list=new ArrayList<>();

    List<EventDay> eventArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evemts);
        Intent intent = getIntent();
         user = intent.getParcelableExtra("user");
         list = intent.getParcelableArrayListExtra("events");
        SimpleDateFormat formatter2=new SimpleDateFormat("dd-MM-yyyy");





        ArrayAdapter ad = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,user.courses);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);


        spinner =findViewById(R.id.reminderSpinner2);
        spinner.setAdapter(ad);
        powiadomieniaSwitch = findViewById(R.id.remindSwitch);
        ImageView icon = findViewById(R.id.note);
        icon.setImageDrawable(getResources().getDrawable(R.drawable.eventicon2));
        startDate = findViewById(R.id.startDateAdd);
        endDate = findViewById(R.id.endDate);
        startTime = findViewById(R.id.startHour);
        endTime = findViewById(R.id.endHour);
        add=findViewById(R.id.add);
        cancel=findViewById(R.id.cancel);
        title=findViewById(R.id.EventTitle);
        desc=findViewById(R.id.desc);
        Date currentTime = Calendar.getInstance().getTime();
        String date = new Date().getDate()+"-"+(new Date().getMonth()+1)+"-"+(new Date().getYear()+1900);
        startDate.setText(date);
        endDate.setText(date);

        startTime.setText(String.valueOf(currentTime.getHours()+":"+currentTime.getMinutes()));
        endTime.setText(String.valueOf(currentTime.getHours()+":"+currentTime.getMinutes()));
        com.applandeo.materialcalendarview.CalendarView calendar2 = findViewById(R.id.menuCalendar);
         list.forEach(new Consumer<Event>() {
             @Override
             public void accept(Event event) {
                 calendar = Calendar.getInstance();
                 Date date2= null;
                 try {
                     date2 = formatter2.parse(event.getStartDate());


                 calendar.set(date2.getYear()+1900,date2.getMonth(), date2.getDate());



                 eventArray.add(new EventDay(calendar, R.drawable.event, Color.parseColor("#228B22")));


                 } catch (ParseException e) {
                     throw new RuntimeException(e);
                 }
             }
         });


        drawerLayout=findViewById(R.id.drawLayout);
        navigationView=findViewById(R.id.navigation_view);
        View view  = (View) navigationView.getHeaderView(0);
        ImageView menuIcon = view.findViewById(R.id.note);
        menuIcon.setImageDrawable(getResources().getDrawable(R.drawable.navigationicon));
        toolbar=findViewById(R.id.toolbar);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(Evemts.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                startDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(Evemts.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                endDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // date picker dialog
                timepicker = new TimePickerDialog(Evemts.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                startTime.setText(hourOfDay + ":" + minute );
                            }
                        },hour,minutes,true);
                timepicker.show();
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timer;
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // date picker dialog
                timepicker = new TimePickerDialog(Evemts.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                startTime.setText(hourOfDay + ":" + minute );
                            }
                        },hour,minutes,true);
                timepicker.show();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timer;
                Boolean isValid=true;
                SimpleDateFormat formatter =new SimpleDateFormat("dd-MM-yyyy");
                Date start = null;
                Date end = null;
                Date today = null;
                try {
                    start = formatter.parse(startDate.getText().toString());
                    end = formatter.parse(endDate.getText().toString());
                    today =formatter.parse(new Date().getDate()+"-"+(new Date().getMonth()+1)+"-"+(new Date().getYear()+1900));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                if(title.getText().toString().equals("")){
                    title.setError("Enter the title");
                    isValid=false;
                }
                if((start.getTime()-today.getTime())<0)
                {
                    startDate.setError("Date is wrong");
                    isValid=false;
                }
                if((end.getTime()-today.getTime())<0)
                {
                    endDate.setError("Date is wrong");
                    isValid=false;
                }
                if(!powiadomieniaSwitch.isChecked()){
                    timer="";
                }else{
                    timer= spinner.getSelectedItem().toString();
                }
                if(isValid) {

                    try {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    user.addEventSQL(new Event(
                                            0,
                                            user.getId(),
                                            title.getText().toString(),
                                            startDate.getText().toString(),
                                            endDate.getText().toString(),
                                            startTime.getText().toString(),
                                            endTime.getText().toString(),
                                            desc.getText().toString(),
                                            timer



                                    ), user.getAccessToken(), user.getId());


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                        thread.start();


                    } finally {
                        Intent intent = new Intent(getApplicationContext(), Menu.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        powiadomieniaSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    spinner.setVisibility(View.VISIBLE);
                }else{
                    spinner.setVisibility(View.GONE);
                }
            }
        });



        setSupportActionBar(toolbar);






        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.OpenDrawer,R.string.CloseDrawer);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id==R.id.profile) {
                    loadBarContent(new Profile(),0,"");
                }
                if(id==R.id.logOut){

                    try {
                        if(isMyServiceRunning(new Intent(getApplicationContext(), backgroundService.class).getClass())){
                            stopService(new Intent(getApplicationContext(), backgroundService.class));
                        }
                        if(isMyServiceRunning(new Intent(getApplicationContext(), undefinedItemsService.class).getClass())){
                            stopService(new Intent(getApplicationContext(), undefinedItemsService.class));
                        }


                        user.logOut(user.getAccessToken()).subscribeOn(Schedulers.io())
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
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.putExtra("logout", true);
                                        finish();

                                        startActivity(intent);

                                    }
                                });

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

    private boolean isMyServiceRunning(Class<? extends Intent> aClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (aClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;

    };
    public void loadBarContent(Fragment fragment, int flag,String value){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Bundle bundle=new Bundle();
        bundle.putString("date",value);
        bundle.putParcelable("user",user);
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


