package com.example.mainactivity;

import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.mainactivity.notActivities.ReadWriteTotalTime;

public class MainActivity extends AppCompatActivity { // TODO: 28.03.2019 MAKE EVERYTHING LOCAL
    private static final String TAG = MainActivity.class.getSimpleName();// TODO: 28.03.2019 what should I LOG
    private static final String ON_SAVE_INSTANCE_TIME_KEY = "total time";

    private Button mStartStopButton;
    private Chronometer mPomodoroTimer;
    private TextView mTimeTotal;//todo: visible: 5-10 sek every 25 minutes
    private ReadWriteTotalTime mTimeToFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {// TODO: create initViews method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();

        // TODO: 27.03.2019 this object is never destroyed because it has reference the whole time
        /*final ReadWriteTotalTime */mTimeToFile = new ReadWriteTotalTime(MainActivity.this);

        // TODO: 28.03.2019 show total time after activity restarts
        mTimeToFile.showTotalTimeEveryMinute(mPomodoroTimer, mTimeTotal);


        mStartStopButton.setOnClickListener(new View.OnClickListener() {
            boolean chronometerRunning;
            long pauseTime;

            @Override
            public void onClick(View v) {

            //todo: count down from 25 minutes if(00) start countdown from 5 minutes then repeat until stopped
                startStopChronometer();

                /*mPomodoroTimer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {*/
                        mTimeToFile.saveTotalTimeEveryMinute(mPomodoroTimer);
                        mTimeToFile.showTotalTimeEveryMinute(mPomodoroTimer, mTimeTotal);
                /*    }
                });*/
            }

            private void startStopChronometer() {//todo: maybe move outside onClickListener inner class
                if(!chronometerRunning){
                    startTimer();
                }
                else { // stop timer and save time value to file
                    stopTimer();
                    //save when user stops timer
                    mTimeToFile.writeTotalTimeToFile(mPomodoroTimer);
                }
            }

            private void stopTimer() {
                mPomodoroTimer.stop();
                chronometerRunning = false;
                pauseTime = SystemClock.elapsedRealtime() - mPomodoroTimer.getBase();
                mStartStopButton.setText(getString(R.string.resume_button));
            }

            private void startTimer() {
                mPomodoroTimer.setBase(SystemClock.elapsedRealtime() - pauseTime); //todo: save the base on rotate
                mPomodoroTimer.start();
                chronometerRunning = true;
                mStartStopButton.setText(getString(R.string.stop_button));
            }
        });

        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(ON_SAVE_INSTANCE_TIME_KEY)){
                String retrievedText = savedInstanceState.getString(ON_SAVE_INSTANCE_TIME_KEY);

                // TODO: 28.03.2019 on rotate save time to file and then retrieve it
                mPomodoroTimer.setText(retrievedText);//set baseline + retrieve text

            }
        }

    }

    private void initializeViews() {
        mTimeTotal = findViewById(R.id.tv_time_total);

        mStartStopButton = findViewById(R.id.bt_stat_stop_pomodoro);
        mPomodoroTimer = findViewById(R.id.chrono_pomodoro_timer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch(itemId){
            case R.id.reset_action:
                resetTime(mTimeToFile);
                return true;
            case R.id.browser_action:
                openBrowser();
                return true;
            case R.id.details_action:
                // TODO: 28.03.2019 open new Activity with all the details(total time, week time, day time, charts to compare weeks months, days ect.)
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // TODO: 27.03.2019 move openBrowser and resetTime to another class
    private void openBrowser() {
        String webPageAddress = "https://google.com";//todo:open URL to google sheets(database)
        Uri webPageUri = Uri.parse(webPageAddress);

        Intent openBrowser = new Intent(Intent.ACTION_VIEW, webPageUri);

        if(openBrowser.resolveActivity(getPackageManager()) != null){
            startActivity(openBrowser);
        }
    }

    private void resetTime(ReadWriteTotalTime timeToFile){
        if(timeToFile != null){
            timeToFile.resetTotalTime();
        }// TODO: 05.04.2019 else show error message

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        String totalTime = mPomodoroTimer.getText().toString();

        outState.putString(ON_SAVE_INSTANCE_TIME_KEY, totalTime);

        super.onSaveInstanceState(outState);//call after putting in data that u want to save
    }

    public TextView getPomodoroTextView(){
        return mPomodoroTimer;
    }

}
