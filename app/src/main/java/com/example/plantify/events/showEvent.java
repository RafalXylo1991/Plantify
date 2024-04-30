package com.example.plantify.events;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;


import com.example.plantify.ExtendClass;
import com.example.plantify.Menu;
import com.example.plantify.R;
import com.example.plantify.Models.PictureNotice.Event;
import com.example.plantify.objects.infodialog;
import com.example.plantify.Models.PictureNotice.users;
import com.example.plantify.todolist.todolist_print;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class showEvent extends ExtendClass {

    Event event;
    String message;
    DatePickerDialog picker;
    TimePickerDialog timepicker;
    users user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);
        event=getIntent().getParcelableExtra("event");
        user=getIntent().getParcelableExtra("user");
        EditText title=   findViewById(R.id.EventTitle);
        EditText startDate=  findViewById(R.id.startPrintDate);
        EditText endDate=   findViewById(R.id.endDate);
        EditText startTime=   findViewById(R.id.startHour);
        EditText endTime= findViewById(R.id.endHour);
        EditText desc=   findViewById(R.id.desc);
        Spinner timer=  findViewById(R.id.reminderSpinner);
        Switch powiadomieniaSwitch=findViewById(R.id.remindSwitch);
        Button update = findViewById(R.id.update);
        Button delete = findViewById(R.id.delete);

        ImageView icon = findViewById(R.id.note);
        icon.setImageDrawable(getResources().getDrawable(R.drawable.eventicon2));

        title.setText(event.getTitle());
        startDate.setText(event.getStartDate());
        endDate.setText(event.getEndDate());
        startTime.setText(event.getStartTime());
        endTime.setText(event.getEndTime());
        desc.setText(event.getDesc());

        desc.setText(event.getDesc());

        powiadomieniaSwitch.setChecked(true);
        timer.setVisibility(View.VISIBLE);
        timer.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,user.courses));

        try {
            Date date = getTime().getSimpleDateFormat().parse(event.getEndDate());
            long days_difference = TimeUnit.MILLISECONDS.toDays(date.getTime()-new Date().getTime()) % 365;
            if(days_difference<0){
                infodialog info = new infodialog("The event is out of date. Do you want delete it?",showEvent.this,"Confirm");
                info.getYes().setBackgroundColor(getResources().getColor(R.color.delete));
                info.getYes().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {

                            user.delEventSQL(user.getAccessToken(),event.getId()).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<String>() {
                                        @Override
                                        public void onSubscribe(@NonNull Disposable d) {

                                        }

                                        @Override
                                        public void onNext(@NonNull String msg) {
                                            message=msg;
                                        }

                                        @Override
                                        public void onError(@NonNull Throwable e) {

                                        }

                                        @Override
                                        public void onComplete() {
                                            info.getDialog().cancel();
                                            infodialog deleted = new infodialog(message.replace("\"", ""),showEvent.this,"Info");
                                            deleted.getDialog().show();
                                            deleted.getNo().setOnClickListener(v1 -> {
                                                deleted.getDialog().cancel();
                                                finish();
                                            });
                                        }
                                    });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
               info.getDialog().show();
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        powiadomieniaSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    timer.setVisibility(View.VISIBLE);
                }else{
                    timer.setVisibility(View.GONE);
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    infodialog info = new infodialog("Czy napewno chcesz usunąć to wydarzenie?", showEvent.this,"Confirm");
                    info.getYes().setBackgroundColor(getResources().getColor(R.color.delete));
                    info.getYes().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                user.delEventSQL(user.getAccessToken(),event.getId()).subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Observer<String>() {
                                            @Override
                                            public void onSubscribe(@NonNull Disposable d) {

                                            }

                                            @Override
                                            public void onNext(@NonNull String msg) {
                                                message=msg;
                                            }

                                            @Override
                                            public void onError(@NonNull Throwable e) {

                                            }

                                            @Override
                                            public void onComplete() {


                                                infodialog deleted = new infodialog("Usunięto wydarzenie",showEvent.this,"Info");
                                                deleted.getDialog().show();
                                                deleted.getNo().setOnClickListener(v1 -> {
                                                    deleted.getDialog().cancel();
                                                    finish();
                                                });

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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        powiadomieniaSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    timer.setVisibility(View.VISIBLE);
                }else{
                    timer.setVisibility(View.GONE);
                }
            }
        });
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(showEvent.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                startDate.setText(getTime().DatePicerkFormatToDate(dayOfMonth,monthOfYear,year));
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
                picker = new DatePickerDialog(showEvent.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                endDate.setText(getTime().DatePicerkFormatToDate(dayOfMonth,monthOfYear,year));
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
                timepicker = new TimePickerDialog(showEvent.this,
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
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // date picker dialog
                timepicker = new TimePickerDialog(showEvent.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                endTime.setText(hourOfDay + ":" + minute );
                            }
                        },hour,minutes,true);
                timepicker.show();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {


                        try {
                            user.updateEventSQL(new Event(
                                    event.getId(),
                                    user.getId(),
                                    title.getText().toString(),
                                    startDate.getText().toString(),
                                    endDate.getText().toString(),
                                    startTime.getText().toString(),
                                    endTime.getText().toString(),
                                    desc.getText().toString(),
                                    timer.getSelectedItem().toString()

                            ), user.getAccessToken(), event.getId());

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                    }
                });
                try {
                    thread.start();
                    thread.join(2000);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {

                }


            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        if(user.isLogged){
            Intent intent = new Intent(getBaseContext(), Menu.class);
            intent.putExtra("user",user);
            startActivity(intent);
        }
        super.onBackPressed();
    }
    private void displayToolTip(String text, EditText bar, int noticecolor) {
        // get message from edit text
        Balloon balloon = new Balloon.Builder(getApplicationContext())
                .setWidthRatio(0.5f)
                .setHeight(BalloonSizeSpec.WRAP)
                .setText(text)
                .setTextColorResource(noticecolor)
                .setTextSize(15f)

                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowSize(10)
                .setArrowPosition(0.5f)
                .setPadding(12)
                .setCornerRadius(8f)
                .setBackgroundColor(getResources().getColor(R.color.white))
                .setBalloonAnimation(BalloonAnimation.ELASTIC)

                .build();
        balloon.showAlignTop(bar);
    }
}