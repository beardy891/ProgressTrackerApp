package com.example.mainactivity.notActivities;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class ReadWriteTotalTime {// TODO: 03.04.2019 mby make it a class that will handle data persistence for each type of statistics (e.g. totalTime, timeToday, whatever i put in g.sheets)
    private static File file;
    private Context context;

    private static final String TAG = ReadWriteTotalTime.class.getSimpleName();
    private static final String FILENAME = "time_value";
    private static String amountOfTimeFromPreviousSession;
    private static String mFileContents;

    private static int amountOfMinutesFromPreviousSession;
    private static int amountOfSecondsFromPreviousSession;

    private static int mMinutesTotal;
    private static int mSecondsTotal;

    private static String[] mMinutesAndSecondsArray;

    public ReadWriteTotalTime(Context context){
        this.context = context;
        file = new File(context.getFilesDir(), FILENAME);//todo: make sure its only one instance of file
        amountOfTimeFromPreviousSession = readTotalTimeFromFile();//called when object is created
        mMinutesAndSecondsArray = amountOfTimeFromPreviousSession.split(":");
        amountOfMinutesFromPreviousSession = Integer.parseInt(mMinutesAndSecondsArray[0].trim());
        amountOfSecondsFromPreviousSession = Integer.parseInt(mMinutesAndSecondsArray[1].trim());
        mFileContents = "";
    }


    public void writeTotalTimeToFile(TextView pomodoroTimer) {
        calculateTotalTime(pomodoroTimer);
        mFileContents = setCorrectStringFormat();

        saveTimeToFile(mFileContents);
    }

    private void saveTimeToFile(String fileContents) {
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
            Toast.makeText(context, file.getParent(), Toast.LENGTH_LONG).show();// TODO: 26.03.2019 remove toast at one point
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void calculateTotalTime(TextView pomodoroTimer) {
        String currentAmountOfTime = pomodoroTimer.getText().toString();
        mMinutesAndSecondsArray = currentAmountOfTime.split(":");

        int minutes = Integer.parseInt(mMinutesAndSecondsArray[0].trim());
        int seconds = Integer.parseInt(mMinutesAndSecondsArray[1].trim());

        mMinutesTotal = amountOfMinutesFromPreviousSession + minutes;
        mSecondsTotal = amountOfSecondsFromPreviousSession + seconds;

        if(mSecondsTotal >= 60){
            mMinutesTotal += mSecondsTotal / 60;
            mSecondsTotal = mSecondsTotal % 60;
            //Log.d(TAG, setCorrectStringFormat(mMinutesTotal, mSecondsTotal));
        }
    }

    public static String setCorrectStringFormat() {
        if(mMinutesTotal < 10 && mSecondsTotal < 10){
            mFileContents = String.format("0%s:0%s", mMinutesTotal, mSecondsTotal);
        } else if(mMinutesTotal < 10 && mSecondsTotal > 10){
            mFileContents = String.format("0%s:%s", mMinutesTotal, mSecondsTotal);
        } else if(mMinutesTotal > 10 && mSecondsTotal > 10){
            mFileContents = String.format("%s:%s", mMinutesTotal, mSecondsTotal);
        } else if(mMinutesTotal > 10 && mSecondsTotal < 10) {
            mFileContents = String.format("%s:0%s", mMinutesTotal, mSecondsTotal);
        }
        return mFileContents;
    }

    private String readTotalTimeFromFile() {
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;

            while((line = bufferedReader.readLine()) != null){
                text.append(line + "\n");
            }
            bufferedReader.close();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
        Log.d(TAG, text.toString());
        return text.toString();
    }

    // TODO: 28.03.2019 extract method to see if it is a minute mark
    public void saveTotalTimeEveryMinute(TextView pomodoro){
        if(isMinuteMark(pomodoro)){
            writeTotalTimeToFile(pomodoro);
        }
    }

    // TODO: 28.03.2019 pomodoro is actually Chronometer type
    public void showTotalTimeEveryMinute(TextView pomodoro, TextView timeTotal){
        if(isMinuteMark(pomodoro)){
            timeTotal.setText(readTotalTimeFromFile());
            timeTotal.setVisibility(View.VISIBLE);
        }
        /*if(isFiveSecondsAfterMinuteMark()){
            timeTotal.setVisibility(View.INVISIBLE);
        }*/// TODO: 29.03.2019 uncomment

    }

    private boolean isMinuteMark(TextView pomodoro) {
        mMinutesAndSecondsArray = pomodoro.getText().toString().split(":");
        return mMinutesAndSecondsArray[1].trim().equals("00");
    }

    private boolean isFiveSecondsAfterMinuteMark() { // TODO: 05.04.2019 might need pomodoro.getText
        return mMinutesAndSecondsArray[1].trim().equals("05");
    }

    public void resetTotalTime(){
        String timeZero = "00:00";
        saveTimeToFile(timeZero);
        readTotalTimeFromFile();
    }

}
