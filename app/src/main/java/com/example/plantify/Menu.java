package com.example.plantify;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import com.example.plantify.Adapters.DropDownViewAdapter;
import com.example.plantify.Notices.Notices;
import com.example.plantify.Notices.createNotice;
import com.example.plantify.Notices.noticeTypes.sound.noticesList;
import com.example.plantify.events.showEvent;
import com.example.plantify.menuContents.Profile;
import com.example.plantify.notifications.CreateChannel;
import com.example.plantify.objects.Event;
import com.example.plantify.Models.PictureNotice.Notice;
import com.example.plantify.objects.ToDoList;
import com.example.plantify.objects.users;
import com.example.plantify.services.backgroundService;
import com.example.plantify.services.undefinedItemsService;
import com.example.plantify.todolist.todolist;
import com.example.plantify.todolist.todolist_print;
import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.google.android.material.navigation.NavigationView;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;
import com.tomergoldst.tooltips.ToolTipsManager;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class Menu extends ExtendClass implements SwipeRefreshLayout.OnRefreshListener{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SwipeRefreshLayout swipe;
    Calendar calendar;



    CalendarView calendar2;
    List<EventDay> eventArray = new ArrayList<>();

    List<Event> eventss = new ArrayList<>();
    List<ToDoList> toDoLists = new ArrayList<>();
    NotificationManager manager;

    ConstraintLayout content;
    String ROOT_FRAGMENT_TAG="root_fragment";
    CreateChannel create;
    ImageView addEvent;
    ImageView addNoitce;
    ImageView addList;
    Intent serviceIntent;
    AutoCompleteTextView eventsDropDown;
    AutoCompleteTextView noticesDropDown;
    AutoCompleteTextView listsDropDown;

    ProgressBar listBar;
    TextView listTextBar;
    ToolTipsManager toolTipsManager;
    ProgressBar noticeBar;
    ProgressBar eventBar;
    ImageView earth;
    CallbackManager callbackManager;

    TextView noticeProgressText;
    ShareLinkContent shareLinkContent;
    TextView eventProgressText;
String tokenm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        setUser(intent.getParcelableExtra("user"));

        manager = getSystemService(NotificationManager.class);
        create=new CreateChannel(manager,"cipeczka");

        drawerLayout=(DrawerLayout) findViewById(R.id.drawLayoutMenu);
        navigationView=(NavigationView) findViewById(R.id.navigation_viewMenu);
        View view  = (View) navigationView.getHeaderView(0);
        ImageView icon = view.findViewById(R.id.note);
        icon.setImageDrawable(getResources().getDrawable(R.drawable.navigationicon));




        toolbar=(Toolbar) findViewById(R.id.toolbar);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swiperefreshMenu);
        addEvent =(ImageView) findViewById(R.id.addEvent);
        addNoitce =(ImageView) findViewById(R.id.addNotice);
        addList = (ImageView)findViewById(R.id.addList);
        earth = findViewById(R.id.imageView2);


        callbackManager = CallbackManager.Factory.create();





        earth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        final List<String>[] holidays = new List[]{new ArrayList<>()};
        String [] lista = getResources().getStringArray(R.array.months);
        ListView day = findViewById(R.id.internationalDay);
        Date date = new Date();
        try {
            getUser().getInternationalDay(date.getDate(), lista[date.getMonth()]).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<String>>() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<String> strings) {
                            if(strings.size()==0){

                                holidays[0].add("There isn't special holidays today");
                            }else{
                               holidays[0] = strings;
                            }

                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                            DropDownViewAdapter days = new DropDownViewAdapter(getApplicationContext(),   holidays[0],null, getUser(),"InternationalDay");
                            day.setAdapter(days);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }



        calendar2 = (CalendarView) findViewById(R.id.menuCalendarMenu);
        eventsDropDown=(AutoCompleteTextView) findViewById(R.id.eventsDropDown);
        eventsDropDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent load = new Intent(getApplicationContext(), showEvent.class);
                load.putExtra("user", getUser());
                load.putExtra("event",eventss.stream().filter(event->event.getTitle().equals(parent.getItemAtPosition(position).toString())).findFirst().get());
                startActivity(load);
            }
        });

        listBar =(ProgressBar)  findViewById(R.id.listProgressBar);
        listBar.setOnClickListener(v -> displayToolTip("Unfinished To-Do-List "+ getUser().unDoneLists+"/"+toDoLists.size(),listBar,R.color.noticecolor));
        listTextBar = (TextView) findViewById(R.id.list_progress_text);

        eventBar =(ProgressBar)  findViewById(R.id.eventProgressBar);
        eventBar.setOnClickListener(v -> displayToolTip("Current events "+ getUser().lastEvents+"/"+eventss.size(),eventBar, R.color.mainLogin));
        eventProgressText =  (TextView) findViewById(R.id.event_progress_text);

        noticeBar=(ProgressBar) findViewById(R.id.noticeProgressBar);
        noticeProgressText= (TextView) findViewById(R.id.noticeProgress_text);
        noticeBar.setOnClickListener(v-> displayToolTip("Important Notices",noticeBar, R.color.todocolor));

        noticesDropDown=(AutoCompleteTextView) findViewById(R.id.NoticesDropDown);
        Intent noticesIntent = new Intent(getApplicationContext(), Notices.class);

        listsDropDown=(AutoCompleteTextView) findViewById(R.id.ListsDropDown);

        noticesDropDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch ( parent.getItemAtPosition(position).toString()){
                    case "Show all notices":

                        noticesIntent.putExtra("user", getUser());
                        noticesIntent.putExtra("important",false);
                        startActivity(noticesIntent);
                        break;
                    case "Show only important":

                        noticesIntent.putExtra("user", getUser());
                        noticesIntent.putExtra("important",true);
                        startActivity(noticesIntent);
                        break;
                    case "Pokaż notatki głosowe":

                        loadBarContent(new noticesList(), 0, "dfdsf" );
                        break;
                }



            }
        });
        listsDropDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                Intent intent= new Intent(getApplicationContext(), todolist_print.class);
               ToDoList list = (ToDoList) toDoLists.stream().filter(element->element.getTitle().equals(parent.getItemAtPosition(position).toString())).findFirst().get();



                intent.putExtra("user", getUser());
                intent.putExtra("list",list);

                intent.putExtra("tasks",list.getTasks().toString());
                intent.putExtra("important",true);
                listsDropDown.clearListSelection();
               startActivity(intent);
            }
        });






        long time_one=System.currentTimeMillis();
        long time_two=1000*5;
        intent.setAction("BackgroundProcess");



        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MediaPlayer mp = MediaPlayer.create(getApplicationContext(),R.raw.delete);
                mp.start();
                Intent intent = new Intent(getApplicationContext(), Evemts.class);
                intent.putExtra("user", getUser());
                intent.putParcelableArrayListExtra("events", (ArrayList<? extends Parcelable>) eventss);
                startActivity(intent);
            }
        });
        addNoitce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadBarContent(new createNotice(),0,"");
            }
        });
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), todolist.class);
                intent.putExtra("user", getUser());
                startActivity(intent);
            }
        });

       swipe.setOnRefreshListener(this::onRefresh);

        eventss.clear();;
        try {
            loadData( getUser());
        } catch (InterruptedException | JSONException | IOException e) {
            throw new RuntimeException(e);
        }









        calendar2.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {

                Calendar clickedDayCalendar = eventDay.getCalendar();
                 List<String> eventslist= new ArrayList<>();
                 List<String> listList= new ArrayList<>();


                String date = getTime().getSimpleDateFormat().format(clickedDayCalendar.getTime());



                List<ToDoList> resultList = toDoLists.stream().filter(p -> p.getDate().equals(date)).collect(Collectors.toList());
                List<Event> resultListEvent = eventss.stream().filter(p -> p.getStartDate().equals(date)).collect(Collectors.toList());

                toDoLists.forEach(new Consumer<ToDoList>() {
                    @Override
                    public void accept(ToDoList toDoList) {
                        if(toDoList.getDate().equals(date)){
                            listList.add(toDoList.getTitle());



                        }
                    }
                });


                if(resultList.size()>0){
                    listsDropDown.setText(resultList.size()+" List",false);
                }else{
                    listsDropDown.setText(resultList.size()+" Lists",false);
                }


                DropDownViewAdapter eventAdapter = new DropDownViewAdapter(getApplicationContext(),eventss.stream().filter(event -> event.getStartDate().equals(date)).map(Event::getTitle).collect(Collectors.toList()),null, getUser(),"Event");
                eventsDropDown.setAdapter(eventAdapter);


                DropDownViewAdapter listAdapter = new DropDownViewAdapter(getApplicationContext(),toDoLists.stream().filter(toDoList -> toDoList.getDate().equals(date)).map(ToDoList::getTitle).collect(Collectors.toList()),null, getUser(),"List");
                listsDropDown.setAdapter(listAdapter);


                if(resultListEvent.size()>1){
                    eventsDropDown.setText(resultListEvent.size()+" List",false);
                }else{
                    eventsDropDown.setText(resultListEvent.size()+" Lists",false);
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

                if(id==R.id.profile){
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


                        getUser().logOut( getUser().getAccessToken()).subscribeOn(Schedulers.io())
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
    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    private void check() {
        Log.i("check","check");
        SimpleDateFormat formatter2;
        ArrayList<Event> toNotify = new ArrayList<>();
        final Date[] eventDate = new Date[1];
        final long[] time = {0};
        eventss.forEach(event -> {



            Date today = new Date();
            Long d = new Date().getTime();
            SimpleDateFormat formatter21 =new SimpleDateFormat("dd-MM-yyyy");

            try {
                eventDate[0] = formatter21.parse(event.getStartDate());

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            switch (event.getRemindTime()){
                case "1h":
                    time[0] = 60*60*1000;
                    break;
                case "4h":
                    time[0] = 4*60*60*1000;
                    break;
                case "12h":
                    time[0] = 12*60*60*1000;
                    break;
                case "1D":
                    time[0] = 24*60*60*1000;
                    break;
                case "7D":
                    time[0] = 7*24*60*60*1000;
                    break;
                case "10D":
                    time[0] = 10*24*60*60*1000;
                    break;
            }

            Date first = new Date(new Date(eventDate[0].getTime()- time[0]).getYear()+1900,new Date(eventDate[0].getTime()- time[0]).getMonth()+1,new Date(eventDate[0].getTime()- time[0]).getDate());
            Date second = new Date(today.getYear()+1900,today.getMonth()+1,today.getDate());

             if(first.equals(second)){
                 toNotify.add(event);

             }


        });
        if(toNotify.size()!=0){
            Date date;
            formatter2=new SimpleDateFormat("dd-MM-yyyy");

            try {
                date=formatter2.parse(toNotify.get(0).getStartDate());

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            serviceIntent = new Intent(getApplicationContext(), backgroundService.class);



            serviceIntent.putParcelableArrayListExtra("event",(ArrayList<? extends Parcelable>) toNotify);
            Log.i("sdfdsf", String.valueOf(toNotify.size()));
            serviceIntent.putExtra("date",new Date(date.getTime()- time[0]));
            serviceIntent.putExtra("user", getUser());

                startService(serviceIntent);
                Log.i("service","started");



        }


    }

    public void loadData(users user) throws InterruptedException, JSONException, IOException {
        ArrayList<String> not = new ArrayList<>();

        not.add("Show all notices");
        not.add("Show only important");
        not.add("Pokaż notatki głosowe");
        DropDownViewAdapter notices = new DropDownViewAdapter(getApplicationContext(),not,null,user,"Notice");

        user.noticeList=new ArrayList<>();

            user.getNotices(user.getAccessToken(),user.getId()).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Notice>() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull Notice notice) {

                            user.noticeList.add(notice);

                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                           e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {

                            System.out.println(user.noticeList);
                        }
                    });

        noticesDropDown.setAdapter(notices);
        eventArray.clear();


                    SimpleDateFormat formatter2=new SimpleDateFormat("dd-MM-yyyy");







                    user.getEventSQL(user.accessToken,user.getId()).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Observer<Event>() {
                                                @Override
                                                public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                                                }

                                                @Override
                                                public void onNext(@io.reactivex.rxjava3.annotations.NonNull Event event) {
                                                         eventss.add(event);
                                                }

                                                @Override
                                                public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                                                }

                                                @Override
                                                public void onComplete() {
                                                    try {
                                                        user.getList(user.getAccessToken(),user.getId()).subscribeOn(Schedulers.io())
                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                .subscribe(new Observer<ToDoList>() {
                                                                    @Override
                                                                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                                                                    }

                                                                    @Override
                                                                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull ToDoList toDoList) {
                                                                        String date =toDoList.getDate();
                                                                        Date date2= null;
                                                                        try {
                                                                            date2 = formatter2.parse(date);
                                                                        } catch (ParseException e) {
                                                                            throw new RuntimeException(e);
                                                                        }
                                                                        calendar = Calendar.getInstance();


                                                                        calendar.set(date2.getYear()+1900,date2.getMonth(), date2.getDate());
                                                                        List<Event> resultList = eventss.stream().filter(p -> p.getStartDate().equals(date)).collect(Collectors.toList());
                                                                        if(resultList.size()==0){
                                                                            if(!toDoList.isDone()){
                                                                                eventArray.add( new EventDay(calendar, R.drawable.unfinished, getResources().getColor(R.color.noticecolor)));
                                                                            }else{
                                                                                eventArray.add( new EventDay(calendar, R.drawable.todolist, getResources().getColor(R.color.noticecolor)));
                                                                            }

                                                                            if(calendar2!=null){
                                                                                calendar2.setEvents(eventArray);
                                                                            }

                                                                        }



                                                                        toDoLists.add(toDoList);

                                                                    }

                                                                    @Override
                                                                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                                                                    }

                                                                    @Override
                                                                    public void onComplete() {
                                                                        System.out.println("end");

                                                                        if(!isMyServiceRunning(undefinedItemsService.class)) {
                                                                            if(eventss.size()!=0&&toDoLists.size()!=0&&user.noticeList.size()!=0){

                                                                                Intent leftthings= new Intent(getBaseContext(), undefinedItemsService.class);
                                                                                leftthings.putExtra("events",eventss.size());
                                                                                leftthings.putExtra("lists",toDoLists.stream().filter(element->element.isDone()==false).collect(Collectors.toList()).size());
                                                                                leftthings.putExtra("notices",user.noticeList.stream().filter(element-> element.getImportant()==true).collect(Collectors.toList()).size());
                                                                                startService(leftthings);
                                                                            }

                                                                        }
                                                                        Log.i("service","started");
                                                                        eventss.forEach(new Consumer<Event>() {
                                                                            @Override
                                                                            public void accept(Event event) {
                                                                                boolean is=false;
                                                                                String date =event.getStartDate();
                                                                                Date date2= null;
                                                                                try {
                                                                                    date2 = formatter2.parse(date);
                                                                                } catch (ParseException e) {
                                                                                    throw new RuntimeException(e);
                                                                                }


                                                                                calendar = Calendar.getInstance();



                                                                                calendar.set(date2.getYear()+1900,date2.getMonth(), date2.getDate());

                                                                                EventDay[] day = new EventDay[1];

                                                                                ToDoList ilist=   toDoLists.stream().filter(list->event.getStartDate().equals(list.getDate())).findAny().orElse(null);
                                                                                if(ilist!=null){
                                                                                    day[0] = new EventDay(calendar, R.drawable.multiple_calendar_icon, getResources().getColor(R.color.noticecolor));
                                                                                }else{
                                                                                    day[0] = new EventDay(calendar, R.drawable.event, getResources().getColor(R.color.delete));
                                                                                }



                                                                                eventArray.add(day[0]);
                                                                                calendar2.setEvents(eventArray);

                                                                            }
                                                                        });

                                                                      if(toDoLists.size()!=0)  setProgres(listBar,listTextBar,"List");
                                                                      if(user.noticeList.size()!=0)   setProgres(noticeBar,noticeProgressText,"Notice");
                                                                      if(eventss.size()!=0)  {setProgres(eventBar,eventProgressText,"Event");};

                                                                        check();
                                                                    }

                                                                });
                                                    } catch (IOException e) {
                                                        throw new RuntimeException(e);
                                                    } catch (JSONException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                            });
















    }


    private void setProgres(ProgressBar bar, TextView text,String type) {
        int procent = 0;
        switch (type){
            case "List":
                System.out.println("list");
                getUser().unDoneLists=(toDoLists.stream().filter(check->check.isDone()==true).collect(Collectors.toList()).size());
                procent = (toDoLists.stream().filter(check->check.isDone()==true).collect(Collectors.toList()).size()*100)/toDoLists.size();
                break;
            case "Notice":
             int size=    getUser().noticeList.stream().filter(check->check.getImportant()==true).collect(Collectors.toList()).size();
                procent = (size*100)/ getUser().noticeList.size();
                bar.setProgress(procent);
                text.setText(String.valueOf(procent));
                break;
            case "Event":
                final int[] i = {0};
                eventss.forEach(new Consumer<Event>() {
                    @Override
                    public void accept(Event event) {

                        SimpleDateFormat formatter2=new SimpleDateFormat("dd-MM-yyyy");

                        try {
                           Date d =formatter2.parse(event.getEndDate());
                           long cycki = (d.getTime())-(new Date().getTime());
                        if(cycki>0){
                            i[0]++;
                        }
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

                int count =eventss.size();
                getUser().lastEvents=count;
                if(count==0){
                    procent=(0);
                }else{
                    procent=(i[0]*100)/count;
                }
                bar.setProgress(  (i[0]*100)/count);
                text.setText(String.valueOf(procent));
                break;
        }

    }

    @Override
    public void onBackPressed() {
        createNotice notice=null;
        Profile profile=null;
        PrintNoticePicture PrintPictureNotice=null;

        if( getSupportFragmentManager().findFragmentById(R.id.flFragmentPlaceHolder) instanceof createNotice){
            notice = (createNotice) getSupportFragmentManager().findFragmentById(R.id.flFragmentPlaceHolder);
        }
        if(getSupportFragmentManager().findFragmentById(R.id.flFragmentPlaceHolder) instanceof Profile){
            profile = (Profile) getSupportFragmentManager().findFragmentById(R.id.flFragmentPlaceHolder);
        }

        if(getSupportFragmentManager().findFragmentById(R.id.flFragmentPlaceHolder) instanceof PrintNoticePicture){


            getSupportFragmentManager().findFragmentById(R.id.flFragmentPlaceHolder).getActivity().finish();
            PrintPictureNotice = (PrintNoticePicture) getSupportFragmentManager().findFragmentById(R.id.flFragmentPlaceHolder);
        }



        if(!(notice != null && notice.isVisible())&&!(profile != null && profile.isVisible())&&PrintPictureNotice == null) { System.out.println("cycki");
            try {
                getUser().logOut( getUser().getAccessToken()).subscribeOn(Schedulers.io())
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


        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);


        } else {




            super.onBackPressed();


        }
    }


    @Override
    public void onRefresh() {
        final Semaphore[] mutex = new Semaphore[1];
       runOnUiThread(new Runnable() {
           @Override
           public void run() {
                mutex[0] = new Semaphore(0);
                finish();
               Intent intent =new Intent(Menu.this, Menu.class);
               intent.putExtra("user", getUser());

               startActivity( intent);
               mutex[0].release();
               String message;
           }
       });
        try {
            mutex[0].acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        swipe.setRefreshing(false);

    }


    public void loadBarContent(Fragment fragment, int flag,String value){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Bundle bundle=new Bundle();
        bundle.putString("date",value);
        bundle.putParcelable("user", getUser());

        fragment.setArguments(bundle);
        if(flag==1) {

            transaction.add(R.id.MenuFragmentPlaceHolder,fragment);
            manager.popBackStack(ROOT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            transaction.addToBackStack(ROOT_FRAGMENT_TAG);
        }else if(flag==0){
            transaction.replace(R.id.MenuFragmentPlaceHolder,fragment);
            manager.popBackStack(ROOT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            transaction.addToBackStack(ROOT_FRAGMENT_TAG);

        }else{
            transaction.replace(R.id.MenuFragmentPlaceHolder,fragment);
            transaction.addToBackStack(null);

        }

        transaction.commit();
    }


    private void displayToolTip(String text, ProgressBar bar, int noticecolor) {
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
    private void displayToolTip2(String text, TextView bar, int noticecolor) {
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //un-register BroadcastReceiver


    }
    public void disableSwipe(){
        swipe.setEnabled(false);
        ScrollView scrollView = findViewById(R.id.scrollbar);
        scrollView.setEnabled(false);
    }





}

