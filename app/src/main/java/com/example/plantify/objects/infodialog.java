package com.example.plantify.objects;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.plantify.R;


public class infodialog {

    public Dialog getDialog() {
        return dialog;
    }

    Dialog dialog;

    public Button getYes() {
        return yes;
    }

    public Button getNo() {
        return no;
    }

    Button yes;
    Button no;
    TextView dialogtitle;

    public infodialog(String title, Context context,String type){
        dialog=new Dialog(context);
        switch (type){
            case "Confirm":
                dialog.setContentView(R.layout.dialog);
                break;

            case "Info":
                dialog.setContentView(R.layout.infodialog);
                break;




        }


        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogtitle =dialog.findViewById(R.id.dialogTitle);


        no =dialog.findViewById(R.id.no);
        yes =dialog.findViewById(R.id.yes);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialogtitle.setText(title);


    }

}
