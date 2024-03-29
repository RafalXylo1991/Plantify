package com.example.plantify;

import android.util.Log;


import com.example.plantify.objects.Event;
import com.example.plantify.objects.Notice;
import com.example.plantify.objects.ToDoList;
import com.example.plantify.objects.users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;

public class SQL {

    static users user;

    public  users getUser() {
        return user;
    }

    public  void setUser(users user) {
        SQL.user = user;
    }

    public   Observable<users> Login(String name,String password,String token) throws IOException, JSONException {

        return Observable.create(new ObservableOnSubscribe<users>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<users> emitter) throws Throwable {
                String url2 = "http://192.168.1.158:5000/users/login";
                URL url = new URL(url2);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("name", name);
                jsonParam.put("password", password);
                jsonParam.put("token", token);


                System.out.println(jsonParam.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());

                os.writeBytes(jsonParam.toString());


                os.flush();
                os.close();

                System.out.println(String.valueOf(conn.getResponseCode()));
                System.out.println(conn.getResponseMessage());

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();


                JsonObject jsonObject = new JsonParser().parse(response.toString()).getAsJsonObject();

                int error= Integer.valueOf(jsonObject.get("status").toString());
                System.out.println(jsonObject.get("status").toString());
                System.out.println("error");
                if(error==0)
                {
                    emitter.onError(new RuntimeException("Error") );
                }else {
                    user = new users(
                            Integer.parseInt(jsonObject.get("user").getAsJsonObject().get("id").toString()),
                            jsonObject.get("user").getAsJsonObject().get("name").toString(),
                            jsonObject.get("user").getAsJsonObject().get("password").toString(),
                            jsonObject.get("user").getAsJsonObject().get("email").toString()
                          );
                    user.isLogged=true;
                    user.accessToken = jsonObject.get("accessToken").toString();
                    user.refreshToken = jsonObject.get("refreshToken").toString();
                    emitter.onNext(user);
                    emitter.onComplete();;
                }
                conn.disconnect();
            }
        });
        }
    public   Observable<String> register(JSONObject user) throws IOException, JSONException {

        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                try {
                    String url2 = "http://192.168.1.158:4000/users/newUser";
                    URL url = new URL(url2);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);




                    System.out.println(user.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());

                    os.writeBytes(user.toString());


                    os.flush();
                    os.close();

                    System.out.println(String.valueOf(conn.getResponseCode()));
                    System.out.println(conn.getResponseMessage());

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    if(!response.toString().replace("\"", "").equals("Error")){
                        emitter.onComplete();;
                    }else{
                        emitter.onNext(response.toString());
                    }


                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public  Observable<Void> addUser(String displayName, String email){
        return  Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Void> emitter) throws Throwable {
                String url2 = "http://192.168.1.158:4000/users/newUser";
                URL url = new URL(url2);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);


                JSONObject newUser = new JSONObject();


                    newUser.put("name", displayName);
                    newUser.put("password", "dfdsf");

                    newUser.put("email",email);


                    newUser.put("pin",  "0000");



                DataOutputStream os = new DataOutputStream(conn.getOutputStream());

                os.writeBytes(newUser.toString());


                os.flush();
                os.close();
                emitter.onComplete();
                System.out.println(String.valueOf(conn.getResponseCode()));
                System.out.println(conn.getResponseMessage());

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                Gson g = new Gson();

                System.out.println(response.toString());

                conn.disconnect();

            }
        });
    }
    public  Observable<Void> addToDoKind(String kind,String token,int id){
        return  Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Void> emitter) throws Throwable {
                String url2 = "http://192.168.1.158:4000/users/updateToDoKind";
                URL url = new URL(url2);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setRequestProperty("Authorization","Bearer "+token.replace("\"", ""));
                conn.setDoOutput(true);
                conn.setDoInput(true);


                JSONObject newUser = new JSONObject();


                newUser.put("kind", kind);


                newUser.put("id",id);




                DataOutputStream os = new DataOutputStream(conn.getOutputStream());

                os.writeBytes(newUser.toString());


                os.flush();
                os.close();



                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result



                conn.disconnect();

            }
        });
    }
    public  Observable<Void> sendToken(String token, String accessToken){
        return  Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Void> emitter) throws Throwable {
                String url2 = "http://192.168.1.158:4000/users/getNotify";
                URL url = new URL(url2);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setRequestProperty("Authorization","Bearer "+accessToken.replace("\"", ""));
                conn.setDoOutput(true);
                conn.setDoInput(true);


                JSONObject newUser = new JSONObject();


                newUser.put("token", token);




                DataOutputStream os = new DataOutputStream(conn.getOutputStream());

                os.writeBytes(newUser.toString());


                os.flush();
                os.close();

                System.out.println(String.valueOf(conn.getResponseCode()));
                System.out.println(conn.getResponseMessage());

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                Gson g = new Gson();

                System.out.println(response.toString());
                emitter.onComplete();
                conn.disconnect();

            }
        });
    }
    public  Observable<String> getToken(){
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@androidx.annotation.NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Log.w("TAG", "Fetching FCM registration token failed", task.getException());

                                    return;
                                }

                                // Get new FCM registration token
                                String token = task.getResult();
                                // Log and toast
                               System.out.println(token);
                              emitter.onNext(token);
                              emitter.onComplete();

                            }




                        });
            }
        });
    }
    public  void updateEventSQL(Event event, String token, int id) throws IOException, JSONException {
        String url2 = "http://192.168.1.158:5000/users/updateEvent";
        URL url = new URL(url2);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept","application/json");
        conn.setRequestProperty("Authorization","Bearer "+token.replace("\"", ""));

        conn.setDoOutput(true);
        conn.setDoInput(true);
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("id", event.getId());
        jsonParam.put("user_id", event.getUser_id());
        jsonParam.put("title", event.getTitle());
        jsonParam.put("startDate", event.getStartDate());
        jsonParam.put("endDate", event.getEndDate());
        jsonParam.put("startTime", event.getStartTime());
        jsonParam.put("endTime", event.getEndTime());
        jsonParam.put("desc", event.getDesc());
        jsonParam.put("remindTime", event.getRemindTime());



        System.out.println(jsonParam.toString());
        DataOutputStream os = new DataOutputStream(conn.getOutputStream());

        os.writeBytes(jsonParam.toString());


        os.flush();
        os.close();









        System.out.println(String.valueOf(conn.getResponseCode()));
        System.out.println(conn.getResponseMessage());

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();


        JsonObject jsonObject = new JsonParser().parse(response.toString()).getAsJsonObject();

        int error= Integer.valueOf(jsonObject.get("status").toString());
        System.out.println(jsonObject.get("status").toString());
        System.out.println("error");
        if(error==0)
        {


        }

    }

    public  Observable<Void> updateNotice(Notice notice, String token) throws IOException, JSONException {
        return Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Void> emitter) throws Throwable {
                String url2 = "http://192.168.1.158:4000/users/updateNotice";
                URL url = new URL(url2);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setRequestProperty("Authorization","Bearer "+token.replace("\"", ""));

                conn.setDoOutput(true);
                conn.setDoInput(true);
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("id", notice.getId());

                jsonParam.put("text", notice.getText());





                DataOutputStream os = new DataOutputStream(conn.getOutputStream());

                os.writeBytes(jsonParam.toString());


                os.flush();
                os.close();








                emitter.onComplete();








            }
        });


    }
    public  Observable<Void> delList(String accessToken, ToDoList list) throws IOException, JSONException {
        return Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Void> emitter) throws Throwable {
                String url2 = "http://192.168.1.158:4000/users/deleteList";
                URL url = new URL(url2);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("DELETE");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setRequestProperty("Authorization","Bearer "+accessToken.replace("\"", ""));

                conn.setDoOutput(true);
                conn.setDoInput(true);
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("id", list.getId());







                DataOutputStream os = new DataOutputStream(conn.getOutputStream());

                os.writeBytes(jsonParam.toString());


                os.flush();
                os.close();









                System.out.println(String.valueOf(conn.getResponseCode()));
                System.out.println(conn.getResponseMessage());


                emitter.onComplete();
            }
        });

    }
    public  Observable<Void> updateList(ToDoList list, String token,int id) throws IOException, JSONException {
        return Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Void> emitter) throws Throwable {
                String url2 = "http://192.168.1.158:4000/users/updateList";
                URL url = new URL(url2);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setRequestProperty("Authorization","Bearer "+token.replace("\"", ""));

                conn.setDoOutput(true);
                conn.setDoInput(true);
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("id", list.getId());

                jsonParam.put("title", list.getTitle());
                jsonParam.put("tasks", list.getTasks());
                jsonParam.put("isdone", list.isDone());

                jsonParam.put("progress", list.getProgress());





                DataOutputStream os = new DataOutputStream(conn.getOutputStream());

                os.writeBytes(jsonParam.toString());


                os.flush();
                os.close();









                emitter.onComplete();
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();







            }
        });


    }
    public void addEventSQL(Event event, String token,int id) throws IOException, JSONException {
        String url2 = "http://192.168.1.158:5000/users/addEvent";
        URL url = new URL(url2);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept","application/json");
        conn.setRequestProperty("Authorization","Bearer "+token.replace("\"", ""));

        conn.setDoOutput(true);
        conn.setDoInput(true);
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("id", id);
        jsonParam.put("title", event.getTitle());
        jsonParam.put("startDate", event.getStartDate());
        jsonParam.put("endDate", event.getEndDate());
        jsonParam.put("startTime", event.getStartTime());
        jsonParam.put("endTime", event.getEndTime());
        jsonParam.put("desc", event.getDesc());
        jsonParam.put("remindTime", event.getRemindTime());

        System.out.println("cycki");

        System.out.println(jsonParam.toString());
        DataOutputStream os = new DataOutputStream(conn.getOutputStream());

        os.writeBytes(jsonParam.toString());


        os.flush();
        os.close();









        System.out.println(String.valueOf(conn.getResponseCode()));
        System.out.println(conn.getResponseMessage());

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();


        JsonObject jsonObject = new JsonParser().parse(response.toString()).getAsJsonObject();

        int error= Integer.valueOf(jsonObject.get("status").toString());
        System.out.println(jsonObject.get("status").toString());
        System.out.println("error");
        if(error==0)
        {


        }

    }
    public Observable<Void> addToDoList( ToDoList list, String token, int id) throws IOException, JSONException {
        return Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Void> emitter) throws Throwable {
                String url2 = "http://192.168.1.158:5000/users/addList";
                URL url = new URL(url2);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setRequestProperty("Authorization","Bearer "+token.replace("\"", ""));

                conn.setDoOutput(true);
                conn.setDoInput(true);
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("id", id);
                jsonParam.put("title", list.getTitle());
                jsonParam.put("date", list.getDate());
                jsonParam.put("tasks", list.getTasks());
                jsonParam.put("isdone", false);
                jsonParam.put("progress", list.getProgress());




                System.out.println(jsonParam.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());

                os.writeBytes(jsonParam.toString());


                os.flush();
                os.close();

                 emitter.onComplete();







              
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();


                JsonObject jsonObject = new JsonParser().parse(response.toString()).getAsJsonObject();

                int error= Integer.valueOf(jsonObject.get("status").toString());
                System.out.println(jsonObject.get("status").toString());
                System.out.println("error");
                if(error==0)
                {


                }

            }
        });


    }
    public Observable<String> addNotice(Notice notice, String token, int id) throws IOException, JSONException {

        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                String url2 = "http://192.168.1.158:5000/users/addNotice";
                URL url = new URL(url2);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setRequestProperty("Authorization","Bearer "+token.replace("\"", ""));

                conn.setDoOutput(true);
                conn.setDoInput(true);
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("id", id);
                jsonParam.put("title", notice.getTitle());
                jsonParam.put("subject", notice.getSubject());
                jsonParam.put("description", notice.getText());
                jsonParam.put("isimportant", notice.getImportant());




                System.out.println(jsonParam.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());

                os.writeBytes(jsonParam.toString());


                os.flush();
                os.close();









                System.out.println(String.valueOf(conn.getResponseCode()));
                System.out.println(conn.getResponseMessage());
                emitter.onComplete();
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();


                JsonObject jsonObject = new JsonParser().parse(response.toString()).getAsJsonObject();





             emitter.onNext(jsonObject.get("message").toString());

            }
        });

    }
    public Observable<List<String>> getInternationalDay(int day, String month) throws IOException, JSONException {

        return Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<String>> emitter) throws Throwable {
                List<String> list = new ArrayList<>();
                String url = MessageFormat.format("https://www.kalbi.pl/{0}-{1}",day,month);
                Document doc = Jsoup.connect(url).get();

                Element holidays = doc.select("section").get(4);
                holidays.children().forEach(element -> {
                    System.out.println(element.text());
                    if(element.hasAttr("title")){
                        list.add(element.text());
                    }
                });





                emitter.onNext(list);
                emitter.onComplete();
            }
        });

    }
    public Observable<Event> getEventSQL(String token, int id) throws IOException, JSONException {

        return Observable.create(new ObservableOnSubscribe<Event>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Event> emitter) throws Throwable {
                String url2 = "http://192.168.1.158:4000/users/getEvent";
                URL url = new URL(url2);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setRequestProperty("Authorization","Bearer "+token.replace("\"", ""));
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();

                jsonParam.put("id", id);




                System.out.println(jsonParam.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());


                os.writeBytes(jsonParam.toString());


                os.flush();
                os.close();

                System.out.println(String.valueOf(conn.getResponseCode()));
                System.out.println(conn.getResponseMessage());

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();


                JsonObject jsonObject = new JsonParser().parse(response.toString()).getAsJsonObject();

                int error= Integer.valueOf(jsonObject.get("status").toString());
                System.out.println(jsonObject.get("status").toString());
                System.out.println("error");
                if(error==0)
                {


                }else {
                    conn.disconnect();
                    JsonArray json= jsonObject.getAsJsonArray("events");
                    System.out.println(json.size());
                    if(json.size()==0){
                        emitter.onComplete();
                    }
                    Iterator<JsonElement> iterator = json.iterator();
                    Event event;
                    while (iterator.hasNext()) {
                        JsonElement name = iterator.next();
                        event=new Event(
                                name.getAsJsonObject().get("id").getAsInt(),
                                name.getAsJsonObject().get("user_id").getAsInt(),
                                name.getAsJsonObject().get("title").toString().replace("\"", ""),
                                name.getAsJsonObject().get("startdate").toString().replace("\"", ""),
                                name.getAsJsonObject().get("enddate").toString().replace("\"", ""),
                                name.getAsJsonObject().get("starttime").toString().replace("\"", ""),
                                name.getAsJsonObject().get("endtime").toString().replace("\"", ""),
                                name.getAsJsonObject().get("description").toString().replace("\"", ""),
                                name.getAsJsonObject().get("timer").toString().replace("\"", "")
                        );

                        emitter.onNext(event);

                        if (!iterator.hasNext()) {
                            emitter.onComplete();
                        }
                    }






                }



            }
        });

    }
    public Observable<ToDoList> getList(String token, int id) throws IOException, JSONException {


        return  Observable.create(new ObservableOnSubscribe<ToDoList>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ToDoList> emitter) throws Throwable {
                String url2 = "http://192.168.1.158:4000/users/getLists";
                URL url = new URL(url2);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setRequestProperty("Authorization","Bearer "+token.replace("\"", ""));
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();

                jsonParam.put("id", id);




                System.out.println(jsonParam.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());


                os.writeBytes(jsonParam.toString());


                os.flush();
                os.close();

                System.out.println(String.valueOf(conn.getResponseCode()));
                System.out.println(conn.getResponseMessage());

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();


                JsonObject jsonObject = new JsonParser().parse(response.toString()).getAsJsonObject();

                int error= Integer.valueOf(jsonObject.get("status").toString());
                System.out.println(jsonObject.get("status").toString());
                System.out.println("error");
                if(error==0)
                {


                }else {
                    conn.disconnect();
                    JsonArray json= jsonObject.getAsJsonArray("lists");
                    if(json.size()==0){
                        emitter.onComplete();
                    }
                    Iterator<JsonElement> iterator = json.iterator();
                    ToDoList list;
                    while (iterator.hasNext()) {
                        JsonElement name = iterator.next();
                        list=new ToDoList(
                                name.getAsJsonObject().get("id").getAsInt(),
                                name.getAsJsonObject().get("title").getAsString(),
                                name.getAsJsonObject().get("date").getAsString(),
                                name.getAsJsonObject().get("tasks").getAsJsonObject(),
                                name.getAsJsonObject().get("isdone").getAsBoolean(),
                                name.getAsJsonObject().get("progress").getAsInt()
                        );

                        emitter.onNext(list);

                        if (!iterator.hasNext()) {
                            emitter.onComplete();
                        }
                    }





                }



            }
        });

    }
    public Observable<Notice> getNotices(String token, int id) throws IOException, JSONException {


        return  Observable.create(new ObservableOnSubscribe<Notice>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Notice> emitter) throws Throwable {
                String url2 = "http://192.168.1.158:5000/users/getNotices";
                URL url = new URL(url2);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setRequestProperty("Authorization","Bearer "+token.replace("\"", ""));

                conn.setDoOutput(true);
                conn.setDoInput(true);
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("id", id);





                System.out.println(jsonParam.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());

                os.writeBytes(jsonParam.toString());


                os.flush();
                os.close();









                System.out.println(String.valueOf(conn.getResponseCode()));
                System.out.println(conn.getResponseMessage());

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();


                JsonArray jsonObject = new JsonParser().parse(response.toString()).getAsJsonArray();
                Iterator<JsonElement> iterator = jsonObject.iterator();
                Notice notice;
                while (iterator.hasNext()) {
                    JsonElement name = iterator.next();
                    notice=new Notice(
                            name.getAsJsonObject().get("title").getAsString(),
                            name.getAsJsonObject().get("subject").getAsString(),
                            name.getAsJsonObject().get("description").getAsString(),
                            name.getAsJsonObject().get("important").getAsBoolean()

                    );
                    notice.setId( name.getAsJsonObject().get("id").getAsInt());
                    emitter.onNext(notice);

                    if (!iterator.hasNext()) {
                       emitter.onComplete();
                    }
                }




            conn.disconnect();
            }
        });
    }
    public Observable<String> delEventSQL(String token, int id) throws IOException, JSONException {


        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                String url2 = "http://192.168.1.158:4000/users/delEvent";
                URL url = new URL(url2);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setRequestProperty("Authorization","Bearer "+token.replace("\"", ""));
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("id",id);






                System.out.println(jsonParam.toString());

                DataOutputStream os = new DataOutputStream(conn.getOutputStream());


                os.writeBytes(jsonParam.toString());


                os.flush();
                os.close();


                System.out.println(String.valueOf(conn.getResponseCode()));
                System.out.println(conn.getResponseMessage());
                if(Integer.parseInt(String.valueOf(conn.getResponseCode()))==403){
                    Log.i("error","Session out");
                }
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                emitter.onNext(response.toString());
                emitter.onComplete();




            }
        });

    }


    public Observable<Void> logOut(String token) throws IOException, JSONException {


        return Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Void> emitter) throws Throwable {
                String url2 = "http://192.168.1.158:4000/users/logOut";
                URL url = new URL(url2);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("DELETE");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setRequestProperty("Authorization","Bearer "+token.replace("\"", ""));
                conn.setDoOutput(true);
                conn.setDoInput(true);

                emitter.onComplete();
            }
        });









    }
    public Observable<Void> updateUser(String token, users user) throws IOException, JSONException {


        return Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Void> emitter) throws Throwable {
                String url2 = "http://192.168.1.158:4000/users/updateUser";
                URL url = new URL(url2);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + token.replace("\"", ""));
                conn.setDoOutput(true);
                conn.setDoInput(true);


                JSONObject jsonParam = new JSONObject();

                jsonParam.put("id", user.getId());
                jsonParam.put("name", user.getName().replace("\"", ""));
                jsonParam.put("password", user.getPassword());
                jsonParam.put("email", user.getEmail().replace("\"", ""));
                ;


                System.out.println(jsonParam.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());

                os.writeBytes(jsonParam.toString());


                os.flush();
                os.close();
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();


                JsonObject jsonObject = new JsonParser().parse(response.toString()).getAsJsonObject();

                int error= Integer.valueOf(jsonObject.get("status").toString());
                System.out.println(jsonObject.get("status").toString());
                System.out.println("error");
                if(error==0)
                {


                }else {
                    conn.disconnect();






                }
                 emitter.onComplete();
            }

        });




    }


    public Observable<Void> deleteUser(String token, users user) throws IOException, JSONException {

        return Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Void> emitter) throws Throwable {
                String url2 = "http://192.168.1.158:4000/users/deleteUser";
                URL url = new URL(url2);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setRequestProperty("Authorization","Bearer "+token.replace("\"", ""));
                conn.setDoOutput(true);
                conn.setDoInput(true);



                JSONObject jsonParam = new JSONObject();

                jsonParam.put("id", user.getId() );






                System.out.println(jsonParam.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());

                os.writeBytes(jsonParam.toString());


                os.flush();
                os.close();



                System.out.println(String.valueOf(conn.getResponseCode()));
                System.out.println(conn.getResponseMessage());

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();


                JsonObject jsonObject = new JsonParser().parse(response.toString()).getAsJsonObject();

                int error= Integer.valueOf(jsonObject.get("status").toString());
                System.out.println(jsonObject.get("status").toString());
                System.out.println("error");
                if(error==0)
                {


                }else {
                    emitter.onComplete();
                    conn.disconnect();






                }

            }
        });



    }
    public void deleteNotice(String token, Notice notice) throws IOException, JSONException {

        String url2 = "http://192.168.1.158:4000/users/delNotice";
        URL url = new URL(url2);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept","application/json");
        conn.setRequestProperty("Authorization","Bearer "+token.replace("\"", ""));
        conn.setDoOutput(true);
        conn.setDoInput(true);



        JSONObject jsonParam = new JSONObject();

        jsonParam.put("id", notice.getId() );






        System.out.println(jsonParam.toString());
        DataOutputStream os = new DataOutputStream(conn.getOutputStream());

        os.writeBytes(jsonParam.toString());


        os.flush();
        os.close();



        System.out.println(String.valueOf(conn.getResponseCode()));
        System.out.println(conn.getResponseMessage());

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();


        JsonObject jsonObject = new JsonParser().parse(response.toString()).getAsJsonObject();



            conn.disconnect();










    }
    public Observable<String> resetPassword(String email) throws IOException, JSONException {
              return Observable.create(new ObservableOnSubscribe<String>() {
                  @Override
                  public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                      String url2 = "http://192.168.1.158:4000/users/resetPassword";
                      URL url = new URL(url2);
                      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                      conn.setRequestMethod("POST");
                      conn.setRequestProperty("Content-Type", "application/json");
                      conn.setRequestProperty("Accept","application/json");

                      conn.setDoOutput(true);
                      conn.setDoInput(true);



                      JSONObject jsonParam = new JSONObject();

                      jsonParam.put("email", email );






                      System.out.println(jsonParam.toString());
                      DataOutputStream os = new DataOutputStream(conn.getOutputStream());

                      os.writeBytes(jsonParam.toString());


                      os.flush();
                      os.close();



                      System.out.println(String.valueOf(conn.getResponseCode()));
                      System.out.println(conn.getResponseMessage());

                      BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                      String inputLine;
                      StringBuffer response = new StringBuffer();

                      while ((inputLine = in.readLine()) != null) {
                          response.append(inputLine);
                      }
                      in.close();
                     System.out.println(response.toString());

                     emitter.onNext(response.toString());
                     emitter.onComplete();

                      conn.disconnect();


                  }
              });









    }
    public Observable<String> sendKey(String key, String email) throws IOException, JSONException {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                String url2 = "http://192.168.1.158:4000/users/sendResetKey";
                URL url = new URL(url2);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");

                conn.setDoOutput(true);
                conn.setDoInput(true);



                JSONObject jsonParam = new JSONObject();

                jsonParam.put("resetkey", key);
                jsonParam.put("email", email);






                System.out.println(jsonParam.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());

                os.writeBytes(jsonParam.toString());


                os.flush();
                os.close();



                System.out.println(String.valueOf(conn.getResponseCode()));
                System.out.println(conn.getResponseMessage());

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                emitter.onNext(response.toString());
                emitter.onComplete();
                System.out.println(response.toString());

                emitter.onNext(response.toString());
                emitter.onComplete();

                conn.disconnect();


            }
        });









    }
    public Observable<String> setNewPassword(String password, String email) throws IOException, JSONException {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                String url2 = "http://192.168.1.158:4000/users/setPassword";
                URL url = new URL(url2);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");

                conn.setDoOutput(true);
                conn.setDoInput(true);



                JSONObject jsonParam = new JSONObject();

                jsonParam.put("password", password);
                jsonParam.put("email", email);







                System.out.println(jsonParam.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());

                os.writeBytes(jsonParam.toString());


                os.flush();
                os.close();



                System.out.println(String.valueOf(conn.getResponseCode()));
                System.out.println(conn.getResponseMessage());

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println(response.toString());

                emitter.onNext(response.toString());
                emitter.onComplete();

                conn.disconnect();


            }
        });









    }
}