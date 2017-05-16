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
            twitterTextMin, instagramTextMin, facebookTextSec, twitterTextSec,
            instagramTextSec, myStatementNow, totalAllText, useForDay;
    // TODO убрать чеки ибо twittercheck всегда true и вообще все check  в сервисе всегда тру
    String faceSeconds, twitSeconds, instaSeconds,
            faceMinutes, twitMinutes, instaMinutes,
            faceHours, twitHours, instaHours,
            isStarted, myCondition, allSocials, serviceHours, serviceMinutes, serviceSeconds;


    SharedPreferences sharedPreferences;
    CompoundButton compoundButton;

    Double serviceTimeinDouble;
    int inService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalAllText = (TextView) findViewById(R.id.total);
        myStatementNow = (TextView) findViewById(R.id.state);
        useForDay = (TextView) findViewById(R.id.textView6);

        facebookTextHour = (TextView) findViewById(R.id.facebook1);
        facebookTextMin = (TextView) findViewById(R.id.facebook2);
        facebookTextSec = (TextView) findViewById(R.id.facebook3);
        twitterTextHour = (TextView) findViewById(R.id.twitter1);
        twitterTextMin = (TextView) findViewById(R.id.twitter2);
        twitterTextSec = (TextView) findViewById(R.id.twitter3);
        instagramTextHour = (TextView) findViewById(R.id.instagram1);
        instagramTextMin = (TextView) findViewById(R.id.instagram2);
        instagramTextSec = (TextView) findViewById(R.id.instagram3);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            compoundButton = (Switch) findViewById(R.id.startButton);
        } else {
            compoundButton = (CheckBox) findViewById(R.id.startButton);
        }

        LoadPreferences();

        compoundButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton myButton, boolean isSwitched) {

                if (isSwitched == true) {
                    SavePreferences("compoundButton", "true");
                    startService(new Intent(MainActivity.this, MyService.class));
                    Toast.makeText(getApplicationContext(), "InternetTime запущено", Toast.LENGTH_SHORT).show();
                } else {
                    SavePreferences("compoundButton", "false");
                    stopService(new Intent(MainActivity.this, MyService.class));
                }
            }
        });

    }

    /*
     * Преобразует sharedPreferences в tring
     */

    public void SavePreferences(String stringName, String stringValue) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Editor edit = sharedPreferences.edit();
        edit.putString(stringName, stringValue);
        edit.commit();
    }

    public void LoadPreferences() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        isStarted = sharedPreferences.getString("compoundButton", "false");
        myCondition = sharedPreferences.getString("myStatementNow", "low");
        allSocials = sharedPreferences.getString("allSocials", "0");

        serviceHours = sharedPreferences.getString("servicehour", "0");
        serviceMinutes = sharedPreferences.getString("servicemin", "0");
        serviceSeconds = sharedPreferences.getString("servicesec", "0");

        if (serviceHours.isEmpty()) {
            SavePreferences("servicehour", "0");
        } else {
            inService = Integer.parseInt(serviceHours);
        }

        if (serviceMinutes.isEmpty()) {
            SavePreferences("servicemin", "0");
        }

        if (serviceSeconds.isEmpty()) {
            SavePreferences("servicesec", "0");
        }


        if (allSocials.isEmpty()) {
            SavePreferences("allSocials", "0 Hours");
        } else {
            DecimalFormat doubleHelper = new DecimalFormat("#.##");
            totalAllText.setText(doubleHelper.format(Double.parseDouble(allSocials)) + "Часов");

            if (inService > 24) {
                useForDay.setText(doubleHelper.format((serviceTimeinDouble / Double.parseDouble(doubleHelper.format((double) inService / 24.0))))
                        + "Часов/День");
            } else {
                useForDay.setText("Обслуживание начнется после 24ч");
            }
        }

        if (myCondition.isEmpty()) {
            SavePreferences("myStatementNow", "low");
        }
        myCondition = sharedPreferences.getString("myStatementNow", "low");
        //TODO Боря вот это все удалить и переделать!
        if (myCondition.equals("low")) {
            myStatementNow.setText("Low");
            myStatementNow.setTextColor(Color.parseColor("#32CD32"));
        } else if (myCondition.equals("Average")) {
            myStatementNow.setText("Average");
            myStatementNow.setTextColor(Color.parseColor("#32CD32"));
        } else if (myCondition.equals("attention")) {
            myStatementNow.setText("ATTENTION");
            myStatementNow.setTextColor(Color.parseColor("#fcce1c"));
        } else if (myCondition.equals("Addicted")) {
            myStatementNow.setText("Addcited");
            myStatementNow.setTextColor(Color.parseColor("#FF9933"));
        } else if (myCondition.equals("DANGER")) {
            myStatementNow.setText("Danger");
            myStatementNow.setTextColor(Color.parseColor("#CC0000"));
        } else {
            myStatementNow.setText("Low");
            myStatementNow.setTextColor(Color.parseColor("#32CD32"));
        }
        //TODO Я имею ввиду только до сюда)
        if (isStarted.equals("true") && isMyServiceRunning(MyService.class)
                && isStarted.equals("true")) {
            compoundButton.setChecked(true);
        } else {
            compoundButton.setChecked(false);
        }

        isEmpty(faceSeconds,facebookTextSec,"faceSeconds");
        isEmpty(faceMinutes,facebookTextMin,"faceMinutes");
        isEmpty(faceHours,facebookTextHour,"faceHour");

        isEmpty(twitSeconds,twitterTextSec,"twitSeconds");
        isEmpty(twitMinutes,twitterTextMin,"twitMinutes");
        isEmpty(twitHours,twitterTextHour,"twitHour");

        isEmpty(instaSeconds,instagramTextSec,"instaSeconds");
        isEmpty(instaMinutes,instagramTextMin,"instaMinutes");
        isEmpty(instaHours,instagramTextHour,"instaHour");

    }

    private void isEmpty(String shared, TextView text, String name)
    {
        shared=sharedPreferences.getString(name, "00");
        if(shared.isEmpty()){
            SavePreferences(name,"00");
        }
        else{
            text.setText(shared);
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