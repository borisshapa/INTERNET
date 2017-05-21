package com.example.socadd;


import java.text.DecimalFormat;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.view.View.OnClickListener;
@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity implements OnClickListener {


    TextView facebookTextHour, twitterTextHour, instagramTextHour, vkontakteTextHour, facebookTextMin,
            twitterTextMin, instagramTextMin, vkontakteTextMin, facebookTextSec, twitterTextSec,
            instagramTextSec, vkontakteTextSec;

    String faceSeconds, twitSeconds, instaSeconds, vkSeconds,
            faceMinutes, twitMinutes, instaMinutes, vkMinutes,
            faceHours, twitHours, instaHours, vkHours,
            isStarted, myCondition, allSocials, serviceHours, serviceMinutes, serviceSeconds;

    int inService;
    SharedPreferences sharedPreferences;
    CompoundButton compoundButton;

    Button report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        facebookTextHour = (TextView) findViewById(R.id.facebook1);
        facebookTextMin = (TextView) findViewById(R.id.facebook2);
        facebookTextSec = (TextView) findViewById(R.id.facebook3);
        twitterTextHour = (TextView) findViewById(R.id.twitter1);
        twitterTextMin = (TextView) findViewById(R.id.twitter2);
        twitterTextSec = (TextView) findViewById(R.id.twitter3);
        instagramTextHour = (TextView) findViewById(R.id.instagram1);
        instagramTextMin = (TextView) findViewById(R.id.instagram2);
        instagramTextSec = (TextView) findViewById(R.id.instagram3);
        vkontakteTextHour = (TextView) findViewById(R.id.vkontakte1);
        vkontakteTextMin = (TextView) findViewById(R.id.vkontakte2);
        vkontakteTextSec = (TextView) findViewById(R.id.vkontakte3);

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
        report = (Button) findViewById(R.id.report);
        report.setOnClickListener(this);

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

        }

        if (myCondition.isEmpty()) {
            SavePreferences("myStatementNow", "low");
        }
        myCondition = sharedPreferences.getString("myStatementNow", "low");


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

        isEmpty(vkSeconds,vkontakteTextSec,"vkSeconds");
        isEmpty(vkMinutes,vkontakteTextMin,"vkMinutes");
        isEmpty(vkHours,vkontakteTextHour,"vkHour");
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


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.report:
            Intent intent = new Intent(this, Main2Activity.class);
                startActivity(intent);
            break;
            default:
            break;
        }
    }
}