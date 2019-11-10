package com.example.assignment_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class recorder extends AppCompatActivity {
    Button rec_start,rec_stop,rec_pl,pl_stop;
    String path = "";
    MediaRecorder mdr;
    MediaPlayer mdp;
    final int REQ_PER_CODE = 1000;
    Context c= this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);
        if (!checkPermissionFromDevice()){
            reqestPermission();
        }


        rec_pl = (Button) findViewById(R.id.Start_Pl);
        rec_start = (Button) findViewById(R.id.Start_rec);
        rec_stop=(Button) findViewById(R.id.stop_rec);
        pl_stop=(Button) findViewById(R.id.stop_Pl);


         rec_start.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (checkPermissionFromDevice()) {

                     path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + UUID.randomUUID().toString() + "_audio_record.3gp";
                     setupMediaRecorder();
                     try {
                         mdr.prepare();
                         mdr.start();
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                     rec_stop.setEnabled(true);
                     rec_pl.setEnabled(false);
                     pl_stop.setEnabled(false);
                     Toast.makeText(recorder.this, "Recording ...", Toast.LENGTH_SHORT).show();
                 } else {
                     reqestPermission();
                 }
             }
         });
         rec_stop.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 mdr.stop();
                 rec_stop.setEnabled(false);
                 rec_pl.setEnabled(true);
                 pl_stop.setEnabled(false);
                 rec_start.setEnabled(true);
             }
         });
         rec_pl.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 pl_stop.setEnabled(true);
                 rec_stop.setEnabled(false);
                 rec_start.setEnabled(false);
                 mdp = new MediaPlayer();
                 try{
                     mdp.setDataSource(path);
                     mdp.prepare();
                 }
                 catch(IOException e){
                     e.printStackTrace();
                 }
                 mdp.start();
                 Toast.makeText(recorder.this, "Playing.....", Toast.LENGTH_SHORT).show();
             }
         });
         pl_stop.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 rec_stop.setEnabled(false);
                 rec_start.setEnabled(true);
                 pl_stop.setEnabled(false);
                 rec_pl.setEnabled(true);
                 if(mdp != null){
                     mdp.stop();
                     mdp.release();
                     setupMediaRecorder();

                 }
             }
         });

    }
    private void setupMediaRecorder(){
        mdr = new MediaRecorder();
        mdr.setAudioSource(MediaRecorder.AudioSource.MIC);
        mdr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mdr.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mdr.setOutputFile(path);
    }
    private void reqestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.CHANGE_WIFI_STATE}, REQ_PER_CODE);

    }
    public void onRequestPermissionsResult(int req_code, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch(req_code){
            case REQ_PER_CODE:
            {
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private boolean checkPermissionFromDevice() {
        int write_ext_st_res = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int rec_aud_res= ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_ext_st_res == PackageManager.PERMISSION_GRANTED && rec_aud_res == PackageManager.PERMISSION_GRANTED;
    }
}
