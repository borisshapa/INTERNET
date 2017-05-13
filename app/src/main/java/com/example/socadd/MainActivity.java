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

    TextView facebookTextSec, twitterTextSec, instagramTextSec, facebookTextMin,
            twitterTextMin,  instagramTextMin, facebookTextHour, twitterTextHour,
            instagramTextHour, myStatementNow, totalhour, usingHour;
    // TODO убрать чеки ибо twittercheck всегда true и вообще все check  в сервисе всегда тру
    String facebooksec, twittersec, instagramsec,
            facebookmin, twittermin,  instagrammin,
            facebookhour, twitterhour, instagramhour,
            starti, states, total, ser1, ser2, ser3, yy, bootchk, firstboot;


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
        totalhour = (TextView) findViewById(R.id.total);
        facebookTextSec = (TextView) findViewById(R.id.facebook1);
        facebookTextMin = (TextView) findViewById(R.id.facebook2);
        facebookTextHour = (TextView) findViewById(R.id.facebook3);

        twitterTextSec = (TextView) findViewById(R.id.twitter1);
        twitterTextMin = (TextView) findViewById(R.id.twitter2);
        twitterTextHour = (TextView) findViewById(R.id.twitter3);

        instagramTextSec = (TextView) findViewById(R.id.instagram1);
        instagramTextMin = (TextView) findViewById(R.id.instagram2);
        instagramTextHour = (TextView) findViewById(R.id.instagram3);


        myStatementNow = (TextView) findViewById(R.id.state);
        usingHour = (TextView) findViewById(R.id.textView6);


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

        starti = sharedPreferences.getString("start", "false");
        states = sharedPreferences.getString("myStatementNow", "low");
        total = sharedPreferences.getString("total", "0");
        bootchk = sharedPreferences.getString("boot", "true");
        firstboot = sharedPreferences.getString("first", "true");

        ser1 = sharedPreferences.getString("servicehour", "0");
        ser2 = sharedPreferences.getString("servicemin", "0");
        ser3 = sharedPreferences.getString("servicesec", "0");

        if (firstboot.isEmpty() || firstboot.equals("true")) {
            SavePreferences("first", "false");
        }



        if (ser1.isEmpty()) {
            SavePreferences("servicehour", "0");
        } else {
            z = Integer.parseInt(ser1);
        }

        if (ser2.isEmpty()) {
            SavePreferences("servicemin", "0");
        }

        if (ser3.isEmpty()) {
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

        if (bootchk.isEmpty()) {
            SavePreferences("boot", "true");
        }

        if (total.isEmpty()) {
            SavePreferences("total", "0 Hours");
        } else {
            DecimalFormat df = new DecimalFormat("#.##");

            x = Double.parseDouble(total);

            totalhour.setText(df.format(x) + " Hours");

            if (z > 24) {

                y = ((double) z / 24.0);

                yy = df.format(y);

                y1 = Double.parseDouble(yy);

                usaget = (x / y1);

                usingHour.setText(df.format(usaget) + " Hour/Day");

            } else {

                usingHour.setText("0 Hour/Day");
            }

        }

        if (states.isEmpty()) {
            SavePreferences("myStatementNow", "low");
        }

        states = sharedPreferences.getString("myStatementNow", "low");

        if (states.equals("low")) {
            myStatementNow.setText("Low");
            myStatementNow.setTextColor(Color.parseColor("#32CD32"));
        }

        else if (states.equals("Average")) {
            myStatementNow.setText("Average");
            myStatementNow.setTextColor(Color.parseColor("#32CD32"));
        }
        else if (states.equals("attention")) {
            myStatementNow.setText("ATTENTION");
            myStatementNow.setTextColor(Color.parseColor("#fcce1c"));
        }
        else if (states.equals("Addicted")) {
            myStatementNow.setText("Addcited");
            myStatementNow.setTextColor(Color.parseColor("#FF9933"));
        }
        else if (states.equals("DANGER")) {
            myStatementNow.setText("Danger");
            myStatementNow.setTextColor(Color.parseColor("#CC0000"));
        } else {
            myStatementNow.setText("Low");
            myStatementNow.setTextColor(Color.parseColor("#32CD32"));
        }

        if (starti.equals("true") && isMyServiceRunning(MyService.class)
                && starti.equals("true")) {
            start.setChecked(true);
        } else {
            start.setChecked(false);
        }

        facebooksec = sharedPreferences.getString("facebooksec", "00");
        if (facebooksec.isEmpty()) {
            SavePreferences("facebooksec", "00");
        } else {
            facebookTextHour.setText(facebooksec);
        }

        facebookmin = sharedPreferences.getString("facebookmin", "00");
        if (facebookmin.isEmpty()) {
            SavePreferences("facebookmin", "00");
        } else {
            facebookTextMin.setText(facebookmin);
        }

        facebookhour = sharedPreferences.getString("facebookhour", "00");
        if (facebookhour.isEmpty()) {
            SavePreferences("facebookhour", "00");
        } else {
            facebookTextSec.setText(facebookhour);
        }

        twittersec = sharedPreferences.getString("twittersec", "00");
        if (twittersec.isEmpty()) {
            SavePreferences("twittersec", "00");
        } else {
            twitterTextHour.setText(twittersec);
        }

        twittermin = sharedPreferences.getString("twittermin", "00");
        if (twittermin.isEmpty()) {
            SavePreferences("twittermin", "00");
        } else {
            twitterTextMin.setText(twittermin);
        }

        twitterhour = sharedPreferences.getString("twitterhour", "00");
        if (twitterhour.isEmpty()) {
            SavePreferences("twitterhour", "00");
        } else {
            twitterTextSec.setText(twitterhour);
        }



        instagramsec = sharedPreferences.getString("instagramsec", "00");
        if (instagramsec.isEmpty()) {
            SavePreferences("instagramsec", "00");
        } else {
            instagramTextHour.setText(instagramsec);
        }

        instagrammin = sharedPreferences.getString("instagrammin", "00");
        if (instagrammin.isEmpty()) {
            SavePreferences("instagrammin", "00");
        } else {
            instagramTextMin.setText(instagrammin);
        }

        instagramhour = sharedPreferences.getString("instagramhour", "00");
        if (instagramhour.isEmpty()) {
            SavePreferences("instagramhour", "00");
        } else {
            instagramTextSec.setText(instagramhour);
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