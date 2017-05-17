package com.example.socadd;

/*
 * Created by S_P_ 10.05.2017.
 */
 import java.text.SimpleDateFormat;
 import java.util.Calendar;
 import android.annotation.SuppressLint;
 import android.app.Activity;
 import android.app.ActivityManager;
 import android.app.Notification;
 import android.app.NotificationManager;
 import android.app.PendingIntent;
 import android.app.Service;
 import android.content.Context;
 import android.content.Intent;
 import android.content.SharedPreferences;
 import android.content.SharedPreferences.Editor;
 import android.os.Handler;
 import android.os.IBinder;
 import android.os.Message;
 import android.preference.PreferenceManager;
 import android.support.v4.app.NotificationCompat;
 import android.support.v4.app.TaskStackBuilder;
 import android.util.Log;

public class MyService extends Service {

    public static int faceDHours, faceDMinutes, faceDSeconds,
            twitDHours, twitDMinutes, twitDSeconds,
            instDHours, instDMinutes, instDSeconds,
            serviceDHours, serviceDMinutes, serviceDSeconds;

    public static String isStarted, startedApp, reBoot;

    private Handler serviceControler;

    int isFirstFacebook, isFirstTwitter, isFirstInstagram;
    double condition;

    private Handler faceControl = new Handler();
    private Handler twitControl = new Handler();
    private Handler instControl = new Handler();

    Boolean facebookB, twitterB, instagramB;

    NotificationManager nm;

    SharedPreferences sharedPreferences;

    @Override
    public IBinder onBind(Intent arg0) {

        return null;
    }

    public void load() {

        isStarted = sharedPreferences.getString("compoundButton", "false");

        faceDHours = Integer.parseInt(sharedPreferences.getString("faceHours", "0"));
        faceDMinutes = Integer.parseInt(sharedPreferences.getString("faceMinutes", "0"));
        faceDSeconds = Integer.parseInt(sharedPreferences.getString("faceSeconds", "0"));

        twitDHours = Integer.parseInt(sharedPreferences.getString("twitHours", "0"));
        twitDMinutes = Integer.parseInt(sharedPreferences.getString("twitMinutes", "0"));
        twitDSeconds = Integer.parseInt(sharedPreferences.getString("twitSeconds", "0"));

        instDHours = Integer.parseInt(sharedPreferences.getString("instaHours", "0"));
        instDMinutes = Integer.parseInt(sharedPreferences.getString("instaMinutes", "0"));
        instDSeconds = Integer.parseInt(sharedPreferences.getString("instaSeconds", "0"));

        serviceDSeconds = Integer.parseInt(sharedPreferences.getString("servicesec", "0"));
        serviceDMinutes = Integer.parseInt(sharedPreferences.getString("servicemin", "0"));
        serviceDHours = Integer.parseInt(sharedPreferences.getString("servicehour", "0"));

    }

    @Override
    public void onCreate() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        reBoot = sharedPreferences.getString("reBoot", "false");
        if (reBoot.isEmpty()) {
            SavePreferences("reBoot", "false");
        }
        showNotification("m");
    }

    @SuppressLint({"HandlerLeak", "SimpleDateFormat"})
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        serviceControler = new Handler() {
            @SuppressWarnings("deprecation")
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    if (reBoot.equals("true")) {
                        //TODO запилиить очищение , которое не чекает время
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        String strDate = sdf.format(c.getTime());

                        if (strDate.equals("00:00:00")) {

                            SavePreferences("faceSeconds", "00");
                            SavePreferences("faceMinutes", "00");
                            SavePreferences("faceHours", "00");

                            SavePreferences("twitSeconds", "00");
                            SavePreferences("twitMinutes", "00");
                            SavePreferences("twitHours", "00");

                            SavePreferences("instaSeconds", "00");
                            SavePreferences("instaMinutes", "00");
                            SavePreferences("instaHours", "00");

                            SavePreferences("servicesec", "00");
                            SavePreferences("servicemin", "00");
                            SavePreferences("servicehour", "00");

                            SavePreferences("myStatementNow", "low");
                            SavePreferences("allSocials", "0");

                        }

                    }
                    load();
                    SavePreferences("allSocials", String.valueOf((faceDHours + twitDHours + instDHours +
                             ( (double) (faceDMinutes + twitDMinutes + instDMinutes) / 60) +
                                     ( (double) (faceDSeconds + twitDSeconds + instDSeconds) / 3600))));
                    serviceDSeconds++;

                    if (serviceDSeconds >= 60) {
                        serviceDSeconds = 00;
                        serviceDMinutes += 1;

                    }

                    if (serviceDMinutes >= 60) {
                        serviceDMinutes = 00;
                        serviceDHours += 1;

                    }

                    SavePreferences("servicesec", String.valueOf(serviceDSeconds));
                    SavePreferences("servicemin", String.valueOf(serviceDMinutes));
                    SavePreferences("servicehour", String.valueOf(serviceDHours));
                    condition = ( (faceDHours + twitDHours + instDHours +
                            ( (double) (faceDMinutes + twitDMinutes + instDMinutes) / 60) +
                            ( (double) (faceDSeconds + twitDSeconds + instDSeconds) / 3600)) / (double) serviceDHours);

                } catch (NullPointerException nullPointerException) {

                }
                //TODO возможно переделать систему определения твоего состояния / зависимости
                if (serviceDHours > 4) {

                    if (condition < 0.1) {

                        SavePreferences("myStatementNow", "low");

                        showNotification("State : Low");

                    } else {

                        if (condition < 0.2 && condition > 0.1) {

                            SavePreferences("myStatementNow", "Average");

                            showNotification("State : Average");

                        } else {

                            if (condition < 0.3 && condition > 0.2) {

                                SavePreferences("myStatementNow", "attention");

                                showNotification("State : Attention");

                            } else {

                                if (condition < 0.5 && condition > 0.4) {

                                    SavePreferences("myStatementNow", "Addicted");

                                    showNotification("State : Addicted");

                                } else {
                                    //TODO пофиксить баг для значения 0.55
                                    if (condition > 0.6) {

                                        SavePreferences("myStatementNow", "DANGER");

                                        showNotification("State : DANGER");

                                    }
                                }
                            }
                        }
                    }
                }

                ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Activity.ACTIVITY_SERVICE);
                startedApp = am.getRunningTasks(1).get(0).topActivity.getPackageName();
                //TODO убрать в самом конце
                Log.d("MyLogs", startedApp);

                if (isStarted.equals("true")) {
                        //TODO убрать calendar
                        //TODO проверить такой ли packageName у всех нужных приложений
                        if (startedApp.equals("com.facebook.katana") || startedApp.equals("com.android.calendar")) {
                            if (isFirstFacebook == 0) {
                                isFirstFacebook = 1;
                                facebookB = true;
                                faceControl.postDelayed(timerFacebook, 0);
                            }
                        } else {
                            if (isFirstFacebook == 1) {
                                facebookB = false;
                                faceControl.removeCallbacks(timerFacebook);
                                isFirstFacebook = 0;
                            }
                        }

                        if (startedApp.equals("com.twitter.android")) {
                            if (isFirstTwitter == 0) {
                                isFirstTwitter = 1;
                                twitterB = true;
                                twitControl.postDelayed(timerTwitter, 0);
                            }
                        } else {
                            if (isFirstTwitter == 1) {
                                twitterB = false;
                                twitControl.removeCallbacks(timerTwitter);
                                isFirstTwitter = 0;
                            }
                        }
                        if (startedApp.equals("com.instagram.android")) {
                            if (isFirstInstagram == 0) {
                                isFirstInstagram = 1;
                                instagramB = true;
                                instControl.postDelayed(timerInstagram, 0);
                            }
                        } else {
                            if (isFirstInstagram == 1) {
                                instagramB = false;
                                instControl.removeCallbacks(timerInstagram);
                                isFirstInstagram = 0;
                            }
                        }
                }

            }

        };

        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        serviceControler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        }).start();

        return START_STICKY;
    }

    @SuppressLint("InlinedApi")
    private void showNotification(String m) {

        load();

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("InternetTime")
                        .setContentText("Сервис запущен");

        Intent targetIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(0, builder.build());

    }


    public void onDestroy() {
        super.onDestroy();

        String close = sharedPreferences.getString("InTheEnd", "no");

        if (isStarted.equals("true") && close.equals("no")) {

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancelAll();

            startService(new Intent(MyService.this, MyService.class));

        } else if (isStarted.equals("true") && close.equals("yes")) {

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancelAll();

        } else {

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancelAll();

        }

    }

    public void SavePreferences(String key, String value) {
        Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.commit();

    }

    private Runnable timerFacebook = new Runnable() {

        public void run() {

            try {

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (facebookB = true) {

                setChangeTime(faceDSeconds,faceDMinutes,faceDHours,"faceSeconds","faceMinutes","faceHours");
                faceControl.postDelayed(this, 0);

            }
        }

    };

    private Runnable timerTwitter = new Runnable() {

        public void run() {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (twitterB = true) {

                setChangeTime(twitDSeconds,twitDMinutes,twitDHours,"instaSeconds","instaMinutes","instaHours");
                twitControl.postDelayed(this, 0);

            }
        }

    };


    private Runnable timerInstagram = new Runnable() {
        public void run() {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (instagramB = true) {
                setChangeTime(instDSeconds,instDMinutes,instDHours,"instaSeconds","instaMinutes","instaHours");
                instControl.postDelayed(this, 0);

            }
        }

    };

    private  void setChangeTime(int sec,int min,int hou,String nameS,String nameM,String nameH){
        sec++;
        if(sec>=60){
            sec=0;
            min++;
        }
        if(min>=60){
            min=0;
            hou++;
        }
        if (sec < 10) {
            SavePreferences(nameS, "0"+sec);
        }

        if (min < 10) {
            SavePreferences(nameM, "0"+min);
        }

        if (hou < 10) {
            SavePreferences(nameH, "0"+hou);
        }
        if(sec>=10)
        {
            SavePreferences(nameS, ""+sec);
        }
        if (min >= 10) {
            SavePreferences(nameM, ""+min);
        }

        if (hou >= 10) {
            SavePreferences(nameH, ""+hou);
        }
    }
}

