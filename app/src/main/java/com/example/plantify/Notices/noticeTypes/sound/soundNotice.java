package com.example.plantify.Notices.noticeTypes.sound;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import static com.example.plantify.Notices.noticeTypes.picture.File.getRandomString;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plantify.Helpers.Fragment;
import com.example.plantify.MainActivity;
import com.example.plantify.R;
import com.google.firebase.auth.ActionCodeUrl;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;


public class soundNotice extends Fragment {


    public static final int RequestPermissionCode = 1;
    Runnable run;
    String path =null;
    MediaPlayer mediaPlayer ;
    MediaRecorder mediaRecorder = new MediaRecorder();
    TextView time;
    java.io.File mypath;
    File file;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sound_notice, container, false);
          time = view.findViewById(R.id.time);

        ImageView startRec= view.findViewById(R.id.startRec);
        ImageView stopRec= view.findViewById(R.id.stopRec);
        ImageView startPlay= view.findViewById(R.id.startPlay);
        ImageView stopPlay= view.findViewById(R.id.stopPlay);
        TextView title= view.findViewById(R.id.soundNoticeTitle);
        Calendar calendar = Calendar.getInstance();

        Date date = new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        final Handler h = new Handler();

        stopRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaRecorder.stop();
                h.removeCallbacks(run);
                stopRec.setVisibility(View.GONE);
                stopPlay.setVisibility(View.VISIBLE);
                startPlay.setVisibility(View.VISIBLE);

                Toast.makeText(getActivity(), "Recording Completed",
                        Toast.LENGTH_LONG).show();
            }
        });
        startPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException,
                    SecurityException, IllegalStateException {



                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(mypath.getPath());
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                System.out.println("cipeczka");
                mediaPlayer.start();
                Toast.makeText(getActivity(), "Recording Playing",
                        Toast.LENGTH_LONG).show();
            }
        });

        stopPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    System.out.println(mypath.getPath());
                    System.out.println("cipeczka");
                    MediaRecorderReady(mypath.getPath());
                }
            }
        });

        startRec.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         run = new Runnable() {
                             @Override
                            public void run() {
                                // do stuff then
                                // can call h again after work!
                                date.setSeconds(date.getSeconds() + 1);

                                String hours;
                                if (date.getMinutes() <= 9) {
                                    hours = "0" + String.valueOf(date.getHours());
                                } else {
                                    hours = String.valueOf(date.getHours());
                                }

                                String min;
                                if (date.getMinutes() <= 9) {
                                    min = "0" + String.valueOf(date.getMinutes());
                                } else {
                                    min = String.valueOf(date.getMinutes());
                                }

                                String sec;
                                if (date.getSeconds() <= 9) {
                                    sec = "0" + String.valueOf(date.getSeconds());
                                } else {
                                    sec = String.valueOf(date.getSeconds());
                                }
                                time.setText(hours + ":" + min + ":" + sec);

                                h.postDelayed(this, 1000);
                            }
                        };

                         if(!title.getText().toString().equals("")){

                            stopRec.setVisibility(View.VISIBLE);
                            h.postDelayed(run, 0);
                            if(checkPermission()) {

                                ContextWrapper cw = new ContextWrapper(getActivity());
                                java.io.File directory = cw.getDir(String.valueOf(getUser().getId())+"_audio", getActivity().MODE_PRIVATE);
                                mypath=new java.io.File(directory,title.getText().toString()+".3gp");
                                MediaRecorderReady(mypath.getPath());

                                try {
                                    mediaRecorder.prepare();
                                    mediaRecorder.start();
                                } catch (IllegalStateException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }



                                Toast.makeText(getActivity(), "Rozpoczęto nagrywanie",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                requestPermission();
                            }
                        }

            }
        });



        return view;
    }

    public void MediaRecorderReady(String path){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(path);
    }



    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(getActivity(), "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(),"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {

        int result = ContextCompat.checkSelfPermission(getActivity(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getActivity(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }
}