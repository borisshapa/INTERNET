package com.example.socadd;

/*
 * Created by S_P_ 10.05.2017.
 */
 import java.text.SimpleDateFormat;
 import java.util.Calendar;
 import android.annotation.SuppressLint;
 import android.app.Activity;
 import android.app.ActivityManager;
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

    /**
     * Этот сервис определяет зарущенное приложение
     * и отслеживает время его работы
     **/

    private Handler serviceControler;
    private Handler faceControl = new Handler();
    private Handler twitControl = new Handler();
    private Handler instControl = new Handler();

    Boolean facebookB, twitterB, instagramB;

    double total, inSocialSec, inSocialMin, totalall, totalall1, totalall2;

    SharedPreferences sharedPreferences;

    int isFirstFacebook, isFirstTwitter, isFirstInstagram;

    int x = 60;

    int y = 3600;

    double addict;

    public static int facebooktemp1, faceDMinutes, faceDSeconds, twittertemp1,
            twitDMinutes, twitDSeconds, instagramtemp1, instDMinutes,
            instDSeconds, serviceDHours, serviceDMinutes, serviceDSeconds;

    public static String isStarted, fb1, fb2, fb3,
            twittr1, twittr2, twittr3,
            insta1, insta2, insta3, ser1, ser2, ser3, startedApp,
            not, reBoot, rich,totals, start;

    @Override
    public IBinder onBind(Intent arg0) {

        return null;
    }

    /**
     * Этот метод загружает значения времени из sharedPreferences.
     * Значения хранятся в строках и преобразуются в integer потом используются в handler
     **/

    public void load() {

        rich = sharedPreferences.getString("rich", "false");
        totals = sharedPreferences.getString("allSocials", "0.0");

        ser1 = sharedPreferences.getString("servicehour", "0");
        ser2 = sharedPreferences.getString("servicemin", "0");
        ser3 = sharedPreferences.getString("servicesec", "0");

        isStarted = sharedPreferences.getString("compoundButton", "false");

        fb1 = sharedPreferences.getString("faceSeconds", "0");
        fb2 = sharedPreferences.getString("faceMinutes", "0");
        fb3 = sharedPreferences.getString("faceHours", "0");

        twittr1 = sharedPreferences.getString("twitSeconds", "0");
        twittr2 = sharedPreferences.getString("twitMinutes", "0");
        twittr3 = sharedPreferences.getString("twitHours", "0");

        insta1 = sharedPreferences.getString("instaSeconds", "0");
        insta2 = sharedPreferences.getString("instaMinutes", "0");
        insta3 = sharedPreferences.getString("instaHours", "0");

        facebooktemp1 = Integer.parseInt(fb3);
        faceDMinutes = Integer.parseInt(fb2);
        faceDSeconds = Integer.parseInt(fb1);

        twittertemp1 = Integer.parseInt(twittr3);
        twitDMinutes = Integer.parseInt(twittr2);
        twitDSeconds = Integer.parseInt(twittr1);

        instagramtemp1 = Integer.parseInt(insta3);
        instDMinutes = Integer.parseInt(insta2);
        instDSeconds = Integer.parseInt(insta1);

        serviceDSeconds = Integer.parseInt(ser3);
        serviceDMinutes = Integer.parseInt(ser2);
        serviceDHours = Integer.parseInt(ser1);

    }

    @Override
    public void onCreate() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
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
                    SavePreferences("allSocials", String.valueOf((facebooktemp1 + twittertemp1 + instagramtemp1 +
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
                    addict = ( (facebooktemp1 + twittertemp1 + instagramtemp1 +
                            ( (double) (faceDMinutes + twitDMinutes + instDMinutes) / 60) +
                            ( (double) (faceDSeconds + twitDSeconds + instDSeconds) / 3600)) / (double) serviceDHours);

                } catch (NullPointerException nullPointerException) {

                }
                //TODO возможно переделать систему определения твоего состояния / зависимости
                if (serviceDHours > 4) {

                    if (addict < 0.1) {

                        SavePreferences("myStatementNow", "low");

                        showNotification("State : Low");

                    } else {

                        if (addict < 0.2 && addict > 0.1) {

                            SavePreferences("myStatementNow", "Average");

                            showNotification("State : Average");

                        } else {

                            if (addict < 0.3 && addict > 0.2) {

                                SavePreferences("myStatementNow", "attention");

                                showNotification("State : Attention");

                            } else {

                                if (addict < 0.5 && addict > 0.4) {

                                    SavePreferences("myStatementNow", "Addicted");

                                    showNotification("State : Addicted");

                                } else {
                                    //TODO пофиксить баг для значения 0.55
                                    if (addict > 0.6) {

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
                            if (isFirstFacebook == 1) {
                            } else {
                                isFirstFacebook = 1;
                                facebookB = true;
                                faceControl.postDelayed(updateTimerThreadfacebook, 0);
                            }

                        } else {
                            if (isFirstFacebook == 1) {
                                facebookB = false;
                                faceControl.removeCallbacks(updateTimerThreadfacebook);
                                isFirstFacebook = 0;
                            }
                        }

                        if (startedApp.equals("com.twitter.android")) {
                            if (isFirstTwitter == 1) {

                            } else {
                                isFirstTwitter = 1;
                                twitterB = true;
                                twitControl.postDelayed(updateTimerThreadtwitter, 0);
                            }
                        } else {
                            if (isFirstTwitter == 1) {
                                twitterB = false;
                                twitControl.removeCallbacks(updateTimerThreadtwitter);
                                isFirstTwitter = 0;
                            }
                        }
                        if (startedApp.equals("com.instagram.android")) {

                            if (isFirstInstagram == 1) {
                            } else {
                                isFirstInstagram = 1;
                                instagramB = true;
                                instControl.postDelayed(updateTimerThreadinstagram, 0);
                            }
                        } else {
                            if (isFirstInstagram == 1) {
                                instagramB = false;
                                instControl.removeCallbacks(updateTimerThreadinstagram);
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

    public void isRunning(String name,int is,boolean b,Handler handler,Runnable runnable)
    {
        if(startedApp.equals(name))
        {
            if(is==0)
            {
                is=1;
                b=true;
                handler.postDelayed(runnable,0);
            }
        }
        else
        {
            if(is==1)
            {
                is=0;
                b=false;
                handler.removeCallbacks(runnable);
            }
        }
    }
    @SuppressLint("InlinedApi")
    private void showNotification(String m) {

        load();

        Intent deleteIntent = new Intent(this, InTheEnd.class);
        PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(this, 0,
                deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);




        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this);
        //TODO вставить название
        mBuilder.setContentTitle("InternetTime");


        mBuilder.setSmallIcon(R.drawable.ic_launcher);

        //TODO оставить только название и иконку

        mBuilder.setOngoing(true);

        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(2, mBuilder.build());

    }


    public void onDestroy() {
        super.onDestroy();

        String close = sharedPreferences.getString("InTheEnd", "no");

        if (start.equals("true") && close.equals("no")) {

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancelAll();

            startService(new Intent(MyService.this, MyService.class));

        } else if (start.equals("true") && close.equals("yes")) {

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

    private Runnable updateTimerThreadfacebook = new Runnable() {

        String facesec, facemin, facehour;

        public void run() {

            try {

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (facebookB = true) {

                faceDSeconds++;

                if (faceDSeconds >= 60) {
                    faceDSeconds = 0;
                    faceDMinutes += 1;

                }
                if (faceDMinutes >= 60) {
                    faceDMinutes = 0;
                    facebooktemp1 += 1;
                }

                facemin = "" + faceDMinutes;
                facesec = "" + faceDSeconds;
                facehour = "" + facebooktemp1;

                // --------------------------------

                if (faceDSeconds < 10) {
                    facesec = "0" + faceDSeconds;
                }

                if (faceDMinutes < 10) {
                    facemin = "0" + faceDMinutes;
                }

                if (facebooktemp1 < 10) {
                    facehour = "0" + facebooktemp1;
                }



                SavePreferences("faceSeconds", facesec);
                SavePreferences("faceMinutes", facemin);
                SavePreferences("faceHours", facehour);

                faceControl.postDelayed(this, 0);

            }
        }

    };

    private Runnable updateTimerThreadtwitter = new Runnable() {

        String tsec, tmin, thour;

        public void run() {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (twitterB = true) {

                twitDSeconds++;

                if (twitDSeconds >= 60) {
                    twitDSeconds = 0;
                    twitDMinutes += 1;
                }

                if (twitDMinutes >= 60) {
                    twitDMinutes = 0;
                    twittertemp1 += 1;
                }

                tmin = "" + twitDMinutes;
                tsec = "" + twitDSeconds;
                thour = "" + twittertemp1;



                if (twitDSeconds < 10) {
                    tsec = "0" + twitDSeconds;
                }

                if (twitDMinutes < 10) {
                    tmin = "0" + twitDMinutes;
                }

                if (twittertemp1 < 10) {
                    thour = "0" + twittertemp1;
                }



                SavePreferences("twitSeconds", tsec);
                SavePreferences("twitMinutes", tmin);
                SavePreferences("twitHours", thour);

                twitControl.postDelayed(this, 0);

            }
        }

    };


    private Runnable updateTimerThreadinstagram = new Runnable() {

        String isec, ihour, imin;

        public void run() {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (instagramB = true) {

                instDSeconds++;

                if (instDSeconds >= 60) {
                    instDSeconds = 0;
                    instDMinutes += 1;
                }
                if (instDMinutes >= 60) {
                    instDMinutes = 0;
                    instagramtemp1 += 1;
                }

                imin = "" + instDMinutes;
                isec = "" + instDSeconds;
                ihour = "" + instagramtemp1;


                if (instDSeconds < 10) {
                    isec = "0" + instDSeconds;
                }

                if (instDMinutes < 10) {
                    imin = "0" + instDMinutes;
                }

                if (instagramtemp1 < 10) {
                    ihour = "0" + instagramtemp1;
                }

                SavePreferences("instaSeconds", isec);
                SavePreferences("instaMinutes", imin);
                SavePreferences("instaHours", ihour);

                instControl.postDelayed(this, 0);

            }
        }

    };
}

