package com.example.socadd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{

    Button back;
    Double serviceTimeinDouble;
    int inService;
    TextView myStatementNow, totalAllText, useForDay;
    SharedPreferences sharedPreferences;
    String faceSeconds, twitSeconds, instaSeconds, vkSeconds,
            faceMinutes, twitMinutes, instaMinutes, vkMinutes,
            faceHours, twitHours, instaHours, vkHours,
            isStarted, myCondition, allSocials, serviceHours, serviceMinutes, serviceSeconds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        totalAllText = (TextView) findViewById(R.id.total);
        myStatementNow = (TextView) findViewById(R.id.state);
        useForDay = (TextView) findViewById(R.id.textView6);
        back = (Button) findViewById(R.id.back);
        LoadPreferences();
        back.setOnClickListener(this);
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

        if (myCondition.isEmpty()) {
            SavePreferences("myStatementNow", "low");
        }
        myCondition = sharedPreferences.getString("myStatementNow", "low");

        if (myCondition.equals("low")) {
            myStatementNow.setText("Низкий");
            myStatementNow.setTextColor(Color.parseColor("#32CD32"));
        } else if (myCondition.equals("Average")) {
            myStatementNow.setText("Ненормальный");
            myStatementNow.setTextColor(Color.parseColor("#32CD32"));
        } else if (myCondition.equals("attention")) {
            myStatementNow.setText("Внимание");
            myStatementNow.setTextColor(Color.parseColor("#fcce1c"));
        } else if (myCondition.equals("Addicted")) {
            myStatementNow.setText("Зависимость");
            myStatementNow.setTextColor(Color.parseColor("#FF9933"));
        } else if (myCondition.equals("DANGER")) {
            myStatementNow.setText("Опасность");
            myStatementNow.setTextColor(Color.parseColor("#CC0000"));
        } else {
            myStatementNow.setText("Стандартный");
            myStatementNow.setTextColor(Color.parseColor("#32CD32"));
        }

        if (allSocials.isEmpty()) {
            SavePreferences("allSocials", "0 Hours");
        } else {
            DecimalFormat doubleHelper = new DecimalFormat("#.##");
             totalAllText.setText(doubleHelper.format(Double.parseDouble(allSocials)) + "Часов");

            if (inService > 24) {
                useForDay.setText(doubleHelper.format((Double.parseDouble(allSocials) / Double.parseDouble(doubleHelper.format((double) inService / 24.0))))
                        + "Часов/День");
            } else {
                useForDay.setText(" ... 24ч");
            }
        }



    }
    public void SavePreferences(String stringName, String stringValue) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(stringName, stringValue);
        edit.commit();
    }


    @Override
    public void onClick(View v2) {
        switch (v2.getId()) {
            case R.id.back:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}