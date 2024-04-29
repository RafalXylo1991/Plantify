package com.example.plantify.Helpers;

import android.content.Context;
import android.widget.Toast;

public  class eventHandlers {
    public void setError(Throwable e, Context context){
        switch (e.getMessage().split(":")[0]){
            case "java.net.ConnectException":
                Toast.makeText(context, "Błąd połączenia z serwerem", Toast.LENGTH_LONG).show();
                break;
            case "org.json.JSONException":
                Toast.makeText(context, "Błąd połączenia z serwerem", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();

        }



    }
    public void printDatabaseResponse(String message, Context context){
        Toast.makeText(context, "Błąd połączenia z serwerem", Toast.LENGTH_LONG).show();



    }
    public void printError(String message, Context context){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
