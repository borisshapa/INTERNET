package com.example.socadd;


import java.text.DecimalFormat;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity {
        //TODO разобраться почему когда мы выключаем сервис приложение умирает

    TextView facebook1, twitter1,  instagram1, facebook2,
            twitter2,  instagram2, facebook3, twitter3,
            instagram3, state, totalhour, usage;

    String facebooksec, twittersec, instagramsec,
            facebookmin, twittermin,  instagrammin,
            facebookhour, twitterhour, instagramhour,
            starti, states, total, ser1, ser2, ser3, yy, bootchk, firstboot;


    SharedPreferences spf;
    CompoundButton start;
    Double x, y, y1, usaget;
    int z;

    int currentapiVersion = android.os.Build.VERSION.SDK_INT;

    android.support.v7.app.ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);
        //TODO убрать это?
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xff01579b));
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        //TODO вставить название
        actionBar
                .setTitle(Html
                        .fromHtml("<font color='#ffffff'> <b> мой проект </b> </font>"));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            start = (Switch) findViewById(R.id.start);
        } else {
            start = (CheckBox) findViewById(R.id.start);
        }

        // Text views, many text views
        totalhour = (TextView) findViewById(R.id.total);
        facebook1 = (TextView) findViewById(R.id.facebook1);
        facebook2 = (TextView) findViewById(R.id.facebook2);
        facebook3 = (TextView) findViewById(R.id.facebook3);

        twitter1 = (TextView) findViewById(R.id.twitter1);
        twitter2 = (TextView) findViewById(R.id.twitter2);
        twitter3 = (TextView) findViewById(R.id.twitter3);

        instagram1 = (TextView) findViewById(R.id.instagram1);
        instagram2 = (TextView) findViewById(R.id.instagram2);
        instagram3 = (TextView) findViewById(R.id.instagram3);


        state = (TextView) findViewById(R.id.state);
        usage = (TextView) findViewById(R.id.textView6);


        load();


        start.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

                if (arg1 == true) {

                    save("start", "true");



                        startService(new Intent(MainActivity.this,
                                ServiceSocial.class));


                    //TODO добавить название
                    Toast.makeText(getApplicationContext(),
                            "мой проект started", Toast.LENGTH_SHORT)
                            .show();


                } else {

                    save("start", "false");

                        stopService(new Intent(MainActivity.this,
                                ServiceSocial.class));
                }

            }
        });

    }

    /*
     *
     * Преобразует spf в tring
     */
    public void save(String key, String value) {

        spf = PreferenceManager.getDefaultSharedPreferences(this);
        Editor edit = spf.edit();
        edit.putString(key, value);
        edit.commit();

    }

    public void load() {

        spf = PreferenceManager.getDefaultSharedPreferences(this);




        String fbcheck = spf.getString("facebook", "true");
        String twcheck = spf.getString("twitter", "true");
        String instacheck = spf.getString("instagram", "true");


        starti = spf.getString("start", "false");
        states = spf.getString("state", "low");
        total = spf.getString("total", "0");
        bootchk = spf.getString("boot", "true");
        firstboot = spf.getString("first", "true");

        ser1 = spf.getString("servicehour", "0");
        ser2 = spf.getString("servicemin", "0");
        ser3 = spf.getString("servicesec", "0");

        if (firstboot.isEmpty() || firstboot.equals("true")) {
            save("first", "false");
        }



        if (ser1.isEmpty()) {
            save("servicehour", "0");
        } else {
            z = Integer.parseInt(ser1);
        }

        if (ser2.isEmpty()) {
            save("servicemin", "0");
        }

        if (ser3.isEmpty()) {
            save("servicesec", "0");
        }

        if (fbcheck.isEmpty()) {
            save("facebook", "true");
        }

        if (twcheck.isEmpty()) {
            save("twitter", "true");
        }

        if (instacheck.isEmpty()) {
            save("instagram", "true");
        }

        if (bootchk.isEmpty()) {
            save("boot", "true");
        }

        if (total.isEmpty()) {
            save("total", "0 Hours");
        } else {
            DecimalFormat df = new DecimalFormat("#.##");

            x = Double.parseDouble(total);

            totalhour.setText(df.format(x) + " Hours");

            if (z > 24) {

                y = ((double) z / 24.0);

                yy = df.format(y);

                y1 = Double.parseDouble(yy);

                usaget = (x / y1);

                usage.setText(df.format(usaget) + " Hour/Day");

            } else {

                usage.setText("0 Hour/Day");
            }

        }

        if (states.isEmpty()) {
            save("state", "low");
        }

        states = spf.getString("state", "low");

        if (states.equals("low")) {
            state.setText("Low");
            state.setTextColor(Color.parseColor("#32CD32"));
        }

        else if (states.equals("Average")) {
            state.setText("Average");
            state.setTextColor(Color.parseColor("#32CD32"));
        }
        else if (states.equals("attention")) {
            state.setText("ATTENTION");
            state.setTextColor(Color.parseColor("#fcce1c"));
        }
        else if (states.equals("Addicted")) {
            state.setText("Addcited");
            state.setTextColor(Color.parseColor("#FF9933"));
        }
        else if (states.equals("DANGER")) {
            state.setText("Danger");
            state.setTextColor(Color.parseColor("#CC0000"));
        } else {
            state.setText("Low");
            state.setTextColor(Color.parseColor("#32CD32"));
        }

        if (starti.equals("true") && isMyServiceRunning(ServiceSocial.class)
                && starti.equals("true")) {
            start.setChecked(true);
        } else {
            start.setChecked(false);
        }

        facebooksec = spf.getString("facebooksec", "00");
        if (facebooksec.isEmpty()) {
            save("facebooksec", "00");
        } else {
            facebook3.setText(facebooksec);
        }

        facebookmin = spf.getString("facebookmin", "00");
        if (facebookmin.isEmpty()) {
            save("facebookmin", "00");
        } else {
            facebook2.setText(facebookmin);
        }

        facebookhour = spf.getString("facebookhour", "00");
        if (facebookhour.isEmpty()) {
            save("facebookhour", "00");
        } else {
            facebook1.setText(facebookhour);
        }

        twittersec = spf.getString("twittersec", "00");
        if (twittersec.isEmpty()) {
            save("twittersec", "00");
        } else {
            twitter3.setText(twittersec);
        }

        twittermin = spf.getString("twittermin", "00");
        if (twittermin.isEmpty()) {
            save("twittermin", "00");
        } else {
            twitter2.setText(twittermin);
        }

        twitterhour = spf.getString("twitterhour", "00");
        if (twitterhour.isEmpty()) {
            save("twitterhour", "00");
        } else {
            twitter1.setText(twitterhour);
        }



        instagramsec = spf.getString("instagramsec", "00");
        if (instagramsec.isEmpty()) {
            save("instagramsec", "00");
        } else {
            instagram3.setText(instagramsec);
        }

        instagrammin = spf.getString("instagrammin", "00");
        if (instagrammin.isEmpty()) {
            save("instagrammin", "00");
        } else {
            instagram2.setText(instagrammin);
        }

        instagramhour = spf.getString("instagramhour", "00");
        if (instagramhour.isEmpty()) {
            save("instagramhour", "00");
        } else {
            instagram1.setText(instagramhour);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    //TODO убрать это?


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
        load();
    }

    //TODO убрать это?
    @Override
    protected void onPause() {
        super.onPause();
    }

}