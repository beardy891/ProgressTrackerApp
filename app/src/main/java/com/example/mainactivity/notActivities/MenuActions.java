package com.example.mainactivity.notActivities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class MenuActions {
    public static void aboutPomodoro(Context context) { // TODO: 11.04.2019 is it good idea to have class just for handling menu actions, static ?????
        String webPageAddress = "https://en.wikipedia.org/wiki/Pomodoro_Technique";
        Uri webPageUri = Uri.parse(webPageAddress);

        Intent openBrowser = new Intent(Intent.ACTION_VIEW, webPageUri);

        if(openBrowser.resolveActivity(context.getPackageManager()) != null){
            context.startActivity(openBrowser);
        }
    }

    public static void resetTime(ReadWriteTotalTime timeToFile, String TAG, Context context){
        if(timeToFile != null){
            timeToFile.resetTotalTime();
        } else {
            String errorMessage = "System could not reset the time";
            Log.e(TAG, errorMessage);
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();// TODO: 11.04.2019 think of different ways to display error messages
        }// TODO: 05.04.2019 else show error message

    }
}
