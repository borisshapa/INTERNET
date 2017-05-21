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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{

    Button back,clear;
    Double serviceTimeinDouble;
    int inService;
    TextView myStatementNow, totalAllText, useForDay,s1,s2,s3;
    SharedPreferences sharedPreferences;
    String isStarted, myCondition, allSocials, serviceHours, serviceMinutes, serviceSeconds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        totalAllText = (TextView) findViewById(R.id.total);
        myStatementNow = (TextView) findViewById(R.id.state);
        useForDay = (TextView) findViewById(R.id.textView6);
        s1 = (TextView) findViewById(R.id.textView22);
        s2 = (TextView) findViewById(R.id.textView4);
        s3 = (TextView) findViewById(R.id.textView1);

        back = (Button) findViewById(R.id.back);
        clear=(Button)findViewById(R.id.clear);
        LoadPreferences();
        back.setOnClickListener(this);
        clear.setOnClickListener(this);
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
            myStatementNow.setTextColor(Color.parseColor("#ffffff"));
            totalAllText.setTextColor(Color.parseColor("#ffffff"));
            useForDay.setTextColor(Color.parseColor("#ffffff"));
            s1.setTextColor(Color.parseColor("#ffffff"));
            s2.setTextColor(Color.parseColor("#ffffff"));
            s3.setTextColor(Color.parseColor("#ffffff"));
            RelativeLayout view = (RelativeLayout) findViewById(R.id.relativeLayout3);
            view.setBackgroundColor(Color.parseColor("#2e7d32"));
        } else if (myCondition.equals("Average")) {
            myStatementNow.setText("Ненормальный");
            myStatementNow.setTextColor(Color.parseColor("#ffffff"));
            totalAllText.setTextColor(Color.parseColor("#ffffff"));
            useForDay.setTextColor(Color.parseColor("#ffffff"));
            s1.setTextColor(Color.parseColor("#ffffff"));
            s2.setTextColor(Color.parseColor("#ffffff"));
            s3.setTextColor(Color.parseColor("#ffffff"));
            RelativeLayout view = (RelativeLayout) findViewById(R.id.relativeLayout3);
            view.setBackgroundColor(Color.parseColor("#6c6f00"));
        } else if (myCondition.equals("attention")) {
            myStatementNow.setText("Внимание");
            myStatementNow.setTextColor(Color.parseColor("#000000"));
            totalAllText.setTextColor(Color.parseColor("#000000"));
            useForDay.setTextColor(Color.parseColor("#000000"));
            s1.setTextColor(Color.parseColor("#000000"));
            s2.setTextColor(Color.parseColor("#000000"));
            s3.setTextColor(Color.parseColor("#000000"));
            RelativeLayout view = (RelativeLayout) findViewById(R.id.relativeLayout3);
            view.setBackgroundColor(Color.parseColor("#ff833a"));
        } else if (myCondition.equals("Addicted")) {
            myStatementNow.setText("Зависимость");
            myStatementNow.setTextColor(Color.parseColor("#000000"));
            totalAllText.setTextColor(Color.parseColor("#000000"));
            useForDay.setTextColor(Color.parseColor("#000000"));
            s1.setTextColor(Color.parseColor("#000000"));
            s2.setTextColor(Color.parseColor("#000000"));
            s3.setTextColor(Color.parseColor("#000000"));
            RelativeLayout view = (RelativeLayout) findViewById(R.id.relativeLayout3);
            view.setBackgroundColor(Color.parseColor("#e65100"));
        } else if (myCondition.equals("DANGER")) {
            myStatementNow.setText("Опасность");
            myStatementNow.setTextColor(Color.parseColor("#ffffff"));
            totalAllText.setTextColor(Color.parseColor("#ffffff"));
            useForDay.setTextColor(Color.parseColor("#ffffff"));
            s1.setTextColor(Color.parseColor("#ffffff"));
            s2.setTextColor(Color.parseColor("#ffffff"));
            s3.setTextColor(Color.parseColor("#ffffff"));
            RelativeLayout view = (RelativeLayout) findViewById(R.id.relativeLayout3);
            view.setBackgroundColor(Color.parseColor("#dd2c00"));
        } else {
            myStatementNow.setText("Стандартный");
            myStatementNow.setTextColor(Color.parseColor("#ffffff"));
            totalAllText.setTextColor(Color.parseColor("#ffffff"));
            useForDay.setTextColor(Color.parseColor("#ffffff"));
            s1.setTextColor(Color.parseColor("#ffffff"));
            s2.setTextColor(Color.parseColor("#ffffff"));
            s3.setTextColor(Color.parseColor("#ffffff"));
            RelativeLayout view = (RelativeLayout) findViewById(R.id.relativeLayout3);
            view.setBackgroundColor(Color.parseColor("#2e7d32"));
        }

        if (allSocials.isEmpty()) {
            SavePreferences("allSocials", "0 Часов");
        } else {
            DecimalFormat doubleHelper = new DecimalFormat("#.##");
             totalAllText.setText(doubleHelper.format(Double.parseDouble(allSocials)) + "Часов");

            if (inService > 24) {
                useForDay.setText(doubleHelper.format((Double.parseDouble(allSocials) / Double.parseDouble(doubleHelper.format((double) inService / 24.0))))
                        + "Часов/День");
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
            case R.id.clear:
                SavePreferences("faceSeconds", "00");
                SavePreferences("faceMinutes", "00");
                SavePreferences("faceHours", "00");

                SavePreferences("twitSeconds", "00");
                SavePreferences("twitMinutes", "00");
                SavePreferences("twitHours", "00");

                SavePreferences("instaSeconds", "00");
                SavePreferences("instaMinutes", "00");
                SavePreferences("instaHours", "00");

                SavePreferences("vkSeconds", "00");
                SavePreferences("vkMinutes", "00");
                SavePreferences("vkHours", "00");

                SavePreferences("servicesec", "00");
                SavePreferences("servicemin", "00");
                SavePreferences("servicehour", "00");

                SavePreferences("myStatementNow", "low");
                SavePreferences("allSocials", "0");
                myStatementNow.setText("Низкий");
                myStatementNow.setTextColor(Color.parseColor("#32CD32"));
                totalAllText.setText("0" + "Часов");
                useForDay.setText(" ... 24ч");
                break;
             default:
                break;
        }
    }
}