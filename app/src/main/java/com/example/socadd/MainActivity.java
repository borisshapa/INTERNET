package com.example.socadd;


import java.text.DecimalFormat;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity {
        //TODO разобраться почему когда мы выключаем сервис приложение умирает

    TextView facebookTextHour, twitterTextHour, instagramTextHour, facebookTextMin,
            twitterTextMin,  instagramTextMin, facebookTextSec, twitterTextSec,
            instagramTextSec, myStatementNow, totalAllText, useForDay;
    // TODO убрать чеки ибо twittercheck всегда true и вообще все check  в сервисе всегда тру
    String faceSeconds, twitSeconds, instaSeconds,
            faceMinutes, twitMinutes, instaMinutes,
            faceHours, twitHours, instaHours,
            isStarted, myCondition, allSocials, serviceHours, serviceMinutes, serviceSeconds, yy;


    SharedPreferences sharedPreferences;
    CompoundButton start;
    Double x, y, y1, usaget;
    int z;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            start = (Switch) findViewById(R.id.start);
        } else {
            start = (CheckBox) findViewById(R.id.start);
        }

        // Text views, many text views
        totalAllText = (TextView) findViewById(R.id.total);
        facebookTextHour = (TextView) findViewById(R.id.facebook1);
        facebookTextMin = (TextView) findViewById(R.id.facebook2);
        facebookTextSec = (TextView) findViewById(R.id.facebook3);

        twitterTextHour = (TextView) findViewById(R.id.twitter1);
        twitterTextMin = (TextView) findViewById(R.id.twitter2);
        twitterTextSec = (TextView) findViewById(R.id.twitter3);

        instagramTextHour = (TextView) findViewById(R.id.instagram1);
        instagramTextMin = (TextView) findViewById(R.id.instagram2);
        instagramTextSec = (TextView) findViewById(R.id.instagram3);


        myStatementNow = (TextView) findViewById(R.id.state);
        useForDay = (TextView) findViewById(R.id.textView6);


        LoadPreferences();


        start.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

                if (arg1 == true) {

                    SavePreferences("start", "true");



                        startService(new Intent(MainActivity.this,
                                MyService.class));


                    //TODO добавить название
                    Toast.makeText(getApplicationContext(),
                            "InternetTime запущено", Toast.LENGTH_SHORT)
                            .show();


                } else {

                    SavePreferences("start", "false");

                        stopService(new Intent(MainActivity.this,
                                MyService.class));
                }

            }
        });

    }

    /*
     *
     * Преобразует sharedPreferences в tring
     */
    public void SavePreferences(String key, String value) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.commit();

    }

    public void LoadPreferences() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String fbcheck = sharedPreferences.getString("facebook", "true");
        String twcheck = sharedPreferences.getString("twitter", "true");
        String instacheck = sharedPreferences.getString("instagram", "true");

        isStarted = sharedPreferences.getString("start", "false");
        myCondition = sharedPreferences.getString("myStatementNow", "low");
        allSocials = sharedPreferences.getString("allSocials", "0");


        serviceHours = sharedPreferences.getString("servicehour", "0");
        serviceMinutes = sharedPreferences.getString("servicemin", "0");
        serviceSeconds = sharedPreferences.getString("servicesec", "0");

        if (serviceHours.isEmpty()) {
            SavePreferences("servicehour", "0");
        } else {
            z = Integer.parseInt(serviceHours);
        }

        if (serviceMinutes.isEmpty()) {
            SavePreferences("servicemin", "0");
        }

        if (serviceSeconds.isEmpty()) {
            SavePreferences("servicesec", "0");
        }

        if (fbcheck.isEmpty()) {
            SavePreferences("facebook", "true");
        }

        if (twcheck.isEmpty()) {
            SavePreferences("twitter", "true");
        }

        if (instacheck.isEmpty()) {
            SavePreferences("instagram", "true");
        }


        if (allSocials.isEmpty()) {
            SavePreferences("allSocials", "0 Hours");
        } else {
            DecimalFormat df = new DecimalFormat("#.##");

            x = Double.parseDouble(allSocials);

            totalAllText.setText(df.format(x) + " Hours");

            if (z > 24) {

                y = ((double) z / 24.0);

                yy = df.format(y);

                y1 = Double.parseDouble(yy);

                usaget = (x / y1);

                useForDay.setText(df.format(usaget) + " Hour/Day");

            } else {

                useForDay.setText("0 Hour/Day");
            }

        }

        if (myCondition.isEmpty()) {
            SavePreferences("myStatementNow", "low");
        }

        myCondition = sharedPreferences.getString("myStatementNow", "low");

        if (myCondition.equals("low")) {
            myStatementNow.setText("Low");
            myStatementNow.setTextColor(Color.parseColor("#32CD32"));
        }

        else if (myCondition.equals("Average")) {
            myStatementNow.setText("Average");
            myStatementNow.setTextColor(Color.parseColor("#32CD32"));
        }
        else if (myCondition.equals("attention")) {
            myStatementNow.setText("ATTENTION");
            myStatementNow.setTextColor(Color.parseColor("#fcce1c"));
        }
        else if (myCondition.equals("Addicted")) {
            myStatementNow.setText("Addcited");
            myStatementNow.setTextColor(Color.parseColor("#FF9933"));
        }
        else if (myCondition.equals("DANGER")) {
            myStatementNow.setText("Danger");
            myStatementNow.setTextColor(Color.parseColor("#CC0000"));
        } else {
            myStatementNow.setText("Low");
            myStatementNow.setTextColor(Color.parseColor("#32CD32"));
        }

        if (isStarted.equals("true") && isMyServiceRunning(MyService.class)
                && isStarted.equals("true")) {
            start.setChecked(true);
        } else {
            start.setChecked(false);
        }

        faceSeconds = sharedPreferences.getString("faceSeconds", "00");
        if (faceSeconds.isEmpty()) {
            SavePreferences("faceSeconds", "00");
        } else {
            facebookTextSec.setText(faceSeconds);
        }

        faceMinutes = sharedPreferences.getString("faceMinutes", "00");
        if (faceMinutes.isEmpty()) {
            SavePreferences("faceMinutes", "00");
        } else {
            facebookTextMin.setText(faceMinutes);
        }

        faceHours = sharedPreferences.getString("faceHours", "00");
        if (faceHours.isEmpty()) {
            SavePreferences("faceHours", "00");
        } else {
            facebookTextHour.setText(faceHours);
        }

        twitSeconds = sharedPreferences.getString("twitSeconds", "00");
        if (twitSeconds.isEmpty()) {
            SavePreferences("twitSeconds", "00");
        } else {
            twitterTextSec.setText(twitSeconds);
        }

        twitMinutes = sharedPreferences.getString("twitMinutes", "00");
        if (twitMinutes.isEmpty()) {
            SavePreferences("twitMinutes", "00");
        } else {
            twitterTextMin.setText(twitMinutes);
        }

        twitHours = sharedPreferences.getString("twitHours", "00");
        if (twitHours.isEmpty()) {
            SavePreferences("twitHours", "00");
        } else {
            twitterTextHour.setText(twitHours);
        }



        instaSeconds = sharedPreferences.getString("instaSeconds", "00");
        if (instaSeconds.isEmpty()) {
            SavePreferences("instaSeconds", "00");
        } else {
            instagramTextSec.setText(instaSeconds);
        }

        instaMinutes = sharedPreferences.getString("instaMinutes", "00");
        if (instaMinutes.isEmpty()) {
            SavePreferences("instaMinutes", "00");
        } else {
            instagramTextMin.setText(instaMinutes);
        }

        instaHours = sharedPreferences.getString("instaHours", "00");
        if (instaHours.isEmpty()) {
            SavePreferences("instaHours", "00");
        } else {
            instagramTextHour.setText(instaHours);
        }

    }



    // Проверяет запущен ли сервис чтобы изменить checkbox на случай убийства системой
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadPreferences();
    }

    //TODO убрать это?
    @Override
    protected void onPause() {
        super.onPause();
    }

}