package com.example.plantify.Notices.noticeTypes.text;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.plantify.R;
import com.example.plantify.Models.PictureNotice.Notice;
import com.example.plantify.Models.PictureNotice.users;
import com.example.plantify.objects.infodialog;

import org.json.JSONException;

import java.io.IOException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TextNotice#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TextNotice extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String message;

    private String mParam1;
    private String mParam2;
    users user;

    public TextNotice() {
        // Required empty public constructor
    }


    public static TextNotice newInstance(String param1, String param2) {
        TextNotice fragment = new TextNotice();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


             user = getArguments().getParcelable("user");


            View view =  inflater.inflate(R.layout.fragment_text_notice, container, false);
            EditText desc = view.findViewById(R.id.description);
            EditText title = view.findViewById(R.id.title);
            EditText sub = view.findViewById(R.id.sub);
            Button button = view.findViewById(R.id.addTextNotice);
            Switch important = view.findViewById(R.id.isImportant);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isValidate=true;

                if(title.getText().toString().equals("")){
                    isValidate=false;
                    title.setError("Enter the title");
                }
                if(sub.getText().toString().equals(""))
                {
                    isValidate=false;
                    sub.setError("Enter the subject");
                }
                if(desc.getText().toString().equals(""))
                {
                    isValidate=false;
                    desc.setError("Enter the note");
                }
                if(isValidate){
                    try {
                        user.addNotice(
                                        new Notice(
                                                title.getText().toString(),
                                                sub.getText().toString(),
                                                desc.getText().toString(),
                                                important.isChecked()
                                        ), user.getAccessToken(), user.getId()

                                ).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {

                                    }

                                    @Override
                                    public void onNext(@NonNull String s) {
                                        infodialog info = new infodialog(s, getContext(), "Info");
                                        info.getDialog().show();
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {

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


              return  view;
    }
}