package com.example.mainactivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mainactivity.notActivities.ReadWriteTotalTime;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {
    private Button mStartStopButton;
    private Chronometer mPomodoroTimer;
    private TextView mTimeTotal;//todo: visible: 5-10 sek every 25 minutes
    private TextView mFileContents;

    private static final String ON_SAVE_INSTANCE_TIME_KEY = "total time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ReadWriteTotalTime timeToFile = new ReadWriteTotalTime(MainActivity.this);

        mTimeTotal = findViewById(R.id.tv_time_total);
        mFileContents = findViewById(R.id.tv_file_contents);

        mStartStopButton = findViewById(R.id.bt_stat_stop_pomodoro);
        mPomodoroTimer = findViewById(R.id.chrono_pomodoro_timer);


        mStartStopButton.setOnClickListener(new View.OnClickListener() {
            boolean chronometerRunning;
            long pauseTime;

            @Override
            public void onClick(View v) {

            //todo: count down from 25 minutes if(00) start countdown from 5 minutes then repeat until stopped
                startStopChronometer();

                mPomodoroTimer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        showTotalTimeSpent(timeToFile); 
                    }
                });//todo: change min api to 24 or make calculations
        }

            private void startStopChronometer() {//todo: maybe move outside onClickListener inner class
                if(!chronometerRunning){ // TODO: 26.03.2019 extract methods, make it more readable
                    mPomodoroTimer.setBase(SystemClock.elapsedRealtime() - pauseTime); //todo: save the base on rotate
                    mPomodoroTimer.start();
                    chronometerRunning = true;
                    mStartStopButton.setText(getString(R.string.stop_button));
                }
                else { // stop timer and save time value to file
                    mPomodoroTimer.stop();
                    chronometerRunning = false;
                    pauseTime = SystemClock.elapsedRealtime() - mPomodoroTimer.getBase();
                    mStartStopButton.setText(getString(R.string.start_button));
                    //todo: should writing to file be here ?
                    timeToFile.writeTotalTimeToFile(mPomodoroTimer, MainActivity.this);
                }
            }
        });

        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(ON_SAVE_INSTANCE_TIME_KEY)){
                String retrievedText = savedInstanceState.getString(ON_SAVE_INSTANCE_TIME_KEY);

                //todo: think what i need to save
                mPomodoroTimer.setText(retrievedText);//set baseline + retrieve text

            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //todo: SWITCH
        int itemId = item.getItemId();
        if(R.id.map_action == itemId){
            openMap();
            return true;
        }
        else if(R.id.browser_action == itemId){
            openBrowser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openBrowser() {//todo: think if i need to pen browser for anything
        String webPageAddress = "https://google.com";//todo:open URL to google sheets(database)
        Uri webPageUri = Uri.parse(webPageAddress);

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, webPageUri);

        if(browserIntent.resolveActivity(getPackageManager()) != null){
            startActivity(browserIntent);
        }
    }

    private void openMap(){
        String locationAddress = "1600 Amphitheatre Parkway, CA";
        Uri geoLocation = Uri.parse("geo:0, 0?q=" + locationAddress);

        Intent openLocation = new Intent(Intent.ACTION_VIEW);
        openLocation.setData(geoLocation);

        if(openLocation.resolveActivity(getPackageManager()) != null){
            startActivity(openLocation);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        String totalTime = mPomodoroTimer.getText().toString();

        outState.putString(ON_SAVE_INSTANCE_TIME_KEY, totalTime);

        super.onSaveInstanceState(outState);//call after putting in data that u want to save
    }

    //show total time for 5 sek, then make it invisible
    private void showTotalTimeSpent(ReadWriteTotalTime timeToFile){// TODO: 26.03.2019 remove parameter, refactor
        mTimeTotal.setVisibility(View.INVISIBLE);
        //todo: think of a better condition than comparing Strings
        if(mPomodoroTimer.getText().toString().substring(0, 2).equals("01")){
            String time = mPomodoroTimer.getText().toString();//todo: refactor
            mTimeTotal.setText(time);

            mTimeTotal.setVisibility(View.VISIBLE);
        }
        // TODO: 26.03.2019 this won't be here
        mFileContents.setText(timeToFile.readTotalTimeFromFile());
    }
}
