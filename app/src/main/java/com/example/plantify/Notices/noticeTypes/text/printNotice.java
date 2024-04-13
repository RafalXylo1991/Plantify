package com.example.plantify.Notices.noticeTypes.text;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


import com.example.plantify.R;
import com.example.plantify.Models.PictureNotice.Notice;
import com.example.plantify.objects.infodialog;
import com.example.plantify.Models.PictureNotice.users;

import org.json.JSONException;

import java.io.IOException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class printNotice extends Fragment {



    public printNotice() {
        // Required empty public constructor
    }



    public static printNotice newInstance(String param1, String param2) {
        printNotice fragment = new printNotice();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_print_notice, container, false);

        Notice notice = getArguments().getParcelable("notice");
        users user = getArguments().getParcelable("user");
        TextView title = view.findViewById(R.id.printTitle);
        TextView subject = view.findViewById(R.id.printSubject);
        EditText text = view.findViewById(R.id.printText);
        title.setText(notice.getTitle().replace("\"", ""));
        subject.setText(notice.getSubject().replace("\"", ""));
        text.setText(notice.getText().replace("\"", ""));

        ImageView delete = view.findViewById(R.id.deleteNotice);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            user.deleteNotice(user.getAccessToken(),notice);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getActivity().onBackPressed();


                                }
                            });

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                });

                thread.start();


            }
        });
        ImageView saveButton = view.findViewById(R.id.saveNotice);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                            notice.setText(text.getText().toString());
                try {
                    user.updateNotice(notice,user.getAccessToken()).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Void>() {
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


                                    infodialog info =new infodialog("Notice has updated",getContext(),"Info");
                                    info.getDialog().show();

                                            getActivity().onBackPressed();



                                }
                            });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        });
        return view;
    }
}