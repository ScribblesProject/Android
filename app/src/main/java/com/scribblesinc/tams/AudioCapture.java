package com.scribblesinc.tams;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.os.Environment;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import java.io.IOException;
import android.Manifest;


public class AudioCapture extends AppCompatActivity{
    //View buttons
    Button play, stop, record, replay;
    private MediaRecorder appAudioRecorder;
    private MediaPlayer appPlayer;
    //data storage as
    private String outputFile = null;
    //Flag for to check if SD present
    Boolean isSDPresent;
    private static final int REQUEST_MIC = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //run parents method by extending the existing class or run this
        //class in addition to parent's class
        super.onCreate(savedInstanceState);
        //activity class creates window
        setContentView(R.layout.activity_audio);

        if(ContextCompat.checkSelfPermission(AudioCapture.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED){
            //if Permission is not granted
            ActivityCompat.requestPermissions(AudioCapture.this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_MIC);

        }else{
            ActivityCompat.requestPermissions(AudioCapture.this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_MIC);

        }

        //binding buttons to GUI component
        play = (Button)findViewById(R.id.play_sound_);
        stop = (Button) findViewById(R.id.stop_sound);
        record = (Button)findViewById(R.id.record_sound);

        //Initially stop button and play button are enabled
        stop.setEnabled(false);
        play.setEnabled(false);

                /*Where shoud storing of recording go? SD or device
        if SD is present store there else store on device*/
        isSDPresent = isExternalStorageWritable();
        if(isSDPresent){
            //SD is present and declare a path there for storing voice rec
            outputFile = Environment.getExternalStorageDirectory().getAbsolutePath()+"/voice_recording.3gp";
        }else{
            //no SD-Card
            outputFile = getFilesDir().getAbsolutePath()+"/voice_recordig.3gp";
        }
        //Instantiating the toolbar of adding asset activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }



    }

    @Override
    protected void onStart(){
        super.onStart();
        //works but crashes when after play and stop
        //working on it.

        /*
        record.setOnClickListener((new OnClickListener() {
            @Override
            public void onClick(View view) {
                record(view);
            }
        }));
        //OnClick listeners to call functions to take care of buttons
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play(view);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop(view);
            }
        });
        */

    }//endofonStart()


    public void play(View view){
        try{
            appPlayer= new MediaPlayer();
            appPlayer.setDataSource(outputFile);
            appPlayer.prepare();
            appPlayer.start();

            play.setEnabled(false);
            stop.setEnabled(true);
            Toast.makeText(getApplicationContext(),"Playing the recording...",Toast.LENGTH_LONG).show();
        } catch(Exception e){
            //play is called before record
            e.printStackTrace();
        }
    }
    public void stop(View view){
        try{
            appAudioRecorder.stop();
            appAudioRecorder.release();
            appAudioRecorder = null;

            stop.setEnabled(false);//disabled stop button
            play.setEnabled(true);//enabled play button
            Toast.makeText(getApplicationContext(),"Audio stopped recording",Toast.LENGTH_LONG).show();
        }catch(IllegalStateException e){
            e.printStackTrace();
        }
    }
    public void record(View view){
        try{
            appAudioRecorder.prepare();
            appAudioRecorder.start();//start recording

            record.setEnabled(false);//disable record button
            stop.setEnabled(true);//enabled stop button
            Toast.makeText(getApplicationContext(),"Recording Started",Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    /*check if external storage is available for read and write */
    public boolean isExternalStorageWritable(){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true; //SD Present
        }
        return false;
    }
    //handle the permissions request response
    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull  int[] grantResults){

           //start audio recording
            if(requestCode == REQUEST_MIC) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "App has microphone capturing permission", Toast.LENGTH_LONG).show();

                    //instantiate the MediaRecording to be use
                    appAudioRecorder = new MediaRecorder();
                     /*Define output format*/
                    appAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    appAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    appAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                    appAudioRecorder.setOutputFile(outputFile);
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(AudioCapture.this, Manifest.permission.RECORD_AUDIO)) {
                        //explain user need of permission
                        finish();
                        Toast.makeText(getApplicationContext(),"Audio Capturing Permission Required", Toast.LENGTH_LONG).show();
                    } else {
                        //Don't ask again for permission, handle rest of app without this permisson
                        finish();
                        Toast.makeText(getApplicationContext(), "App has no audio capturing permission", Toast.LENGTH_LONG).show();
                    }
                }
            }

    }//end of onRequestPermissoinResult

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_audio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //if the back button is pressed
        if(item.getItemId() == android.R.id.home){
            finish(); //goes back to the previous activity
        }

        return super.onOptionsItemSelected(item);
    }


}
