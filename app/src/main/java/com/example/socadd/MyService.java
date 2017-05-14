package com.example.socadd;

/**
 * Created by пк on 10.05.2017.
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

    private Handler mainhandler;
    private Handler customHandlerfacebook = new Handler();
    private Handler customHandlertwitter = new Handler();
    private Handler customHandlerinstagram = new Handler();

    Boolean facebook, twitter, instagram;

    double total, totalsec, totalmin, totalall, totalall1, totalall2;

    SharedPreferences sharedPreferences;

    int f, t, tu, w, i, k, b, a, s, km;

    int x = 60;

    int y = 3600;

    double addict;

    public static int facebooktemp1, facebooktemp2, facebooktemp3, twittertemp1,
            twittertemp2, twittertemp3, instagramtemp1, instagramtemp2,
            instagramtemp3, servicetemp1, servicetemp2, servicetemp3;

    public static String fbcheck, instacheck,
            twcheck, starti, fb1, fb2, fb3,
            twittr1, twittr2, twittr3,
            insta1, insta2, insta3, ser1, ser2, ser3, packageName,
            not, clear, rp, rich, state, totals, start;

    @Override
    public IBinder onBind(Intent arg0) {

        return null;
    }

    /**
     * Этот метод загружает значения времени из sharedPreferences.
     * Значения хранятся в строках и преобразуются в integer потом используются в handler
     **/

    public void load() {

        fbcheck = sharedPreferences.getString("facebook", "true");
        twcheck = sharedPreferences.getString("twitter", "true");
        instacheck = sharedPreferences.getString("instagram", "true");
        rich = sharedPreferences.getString("rich", "false");
        totals = sharedPreferences.getString("allSocials", "0.0");

        ser1 = sharedPreferences.getString("servicehour", "0");
        ser2 = sharedPreferences.getString("servicemin", "0");
        ser3 = sharedPreferences.getString("servicesec", "0");

        starti = sharedPreferences.getString("start", "false");

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
        facebooktemp2 = Integer.parseInt(fb2);
        facebooktemp3 = Integer.parseInt(fb1);

        twittertemp1 = Integer.parseInt(twittr3);
        twittertemp2 = Integer.parseInt(twittr2);
        twittertemp3 = Integer.parseInt(twittr1);

        instagramtemp1 = Integer.parseInt(insta3);
        instagramtemp2 = Integer.parseInt(insta2);
        instagramtemp3 = Integer.parseInt(insta1);

        servicetemp3 = Integer.parseInt(ser3);
        servicetemp2 = Integer.parseInt(ser2);
        servicetemp1 = Integer.parseInt(ser1);

    }

    @Override
    public void onCreate() {


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        not = sharedPreferences.getString("not", "false");
        clear = sharedPreferences.getString("clear", "false");

        if (not.isEmpty()) {
            save("not", "false");
        }

        if (clear.isEmpty()) {
            save("clear", "false");
        }

        if (!not.equals("true")) {
            showNotification("m");
        }

    }

    @SuppressLint({"HandlerLeak", "SimpleDateFormat"})
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mainhandler = new Handler() {

            @SuppressWarnings("deprecation")
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                try {

                    if (clear.equals("true")) {
                        //TODO запилиить очищение , которое не чекает время
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        String strDate = sdf.format(c.getTime());

                        if (strDate.equals("00:00:00")) {

                            save("faceSeconds", "00");
                            save("faceMinutes", "00");
                            save("faceHours", "00");

                            save("twitSeconds", "00");
                            save("twitMinutes", "00");
                            save("twitHours", "00");

                            save("instaSeconds", "00");
                            save("instaMinutes", "00");
                            save("instaHours", "00");

                            save("servicesec", "00");
                            save("servicemin", "00");
                            save("servicehour", "00");

                            save("myStatementNow", "low");
                            save("allSocials", "0");

                        }

                    }

                    load();


                    totalmin = (double) (facebooktemp2 + twittertemp2
                            + instagramtemp2);

                    totalsec = (double) (facebooktemp3 + twittertemp3
                            + instagramtemp3);

                    totalall1 = ((double) totalmin / (double) x);

                    totalall2 = ((double) totalsec / (double) y);

                    totalall = (double) (totalall1 + totalall2);

                    total = (double) (facebooktemp1 + twittertemp1
                            + instagramtemp1 + totalall);

                    save("allSocials", String.valueOf(total));

                    servicetemp3++;

                    if (servicetemp3 >= 60) {
                        servicetemp3 = 00;
                        servicetemp2 += 1;

                    }

                    if (servicetemp2 >= 60) {
                        servicetemp2 = 00;
                        servicetemp1 += 1;

                    }

                    save("servicesec", String.valueOf(servicetemp3));
                    save("servicemin", String.valueOf(servicetemp2));
                    save("servicehour", String.valueOf(servicetemp1));

                    addict = ((double) total / (double) servicetemp1);

                } catch (NullPointerException nullPointerException) {

                }

                if (servicetemp1 > 4) {

                    if (addict < 0.1) {

                        save("myStatementNow", "low");

                        showNotification("State : Low");

                    } else {

                        if (addict < 0.2 && addict > 0.1) {

                            save("myStatementNow", "Average");

                            showNotification("State : Average");

                        } else {

                            if (addict < 0.3 && addict > 0.2) {

                                save("myStatementNow", "attention");

                                showNotification("State : Attention");

                            } else {

                                if (addict < 0.5 && addict > 0.4) {

                                    save("myStatementNow", "Addicted");

                                    showNotification("State : Addicted");

                                } else {
                                    //TODO пофиксить баг для значения 0.55
                                    if (addict > 0.6) {

                                        save("myStatementNow", "DANGER");

                                        showNotification("State : DANGER");

                                    }
                                }
                            }
                        }
                    }
                }
                //Основная реализация задумки
                ActivityManager am = (ActivityManager) getApplicationContext()
                        .getSystemService(Activity.ACTIVITY_SERVICE);

                packageName = am.getRunningTasks(1).get(0).topActivity
                        .getPackageName();

                Log.d("MyLogs", packageName);

                if (starti.equals("true")) {

                    //if (fbcheck.equals("true")) {
                        //TODO убрать calendar
                        if (packageName.equals("com.facebook.katana")
                                || packageName.equals("com.android.calendar")
                                || packageName.equals("app.fastfacebook.com")
                                || packageName
                                .equals("com.rapid.facebook.magicdroid")
                                || packageName.equals("com.androdb.fastlitefb")
                                || packageName.equals("com.abewy.klyph_beta")
                                || packageName
                                .equals("uk.co.senab.blueNotifyFree")
                                || packageName
                                .equals("com.platinumapps.facedroid")
                                || packageName.equals("com.spatiolabs.spatio")
                                || packageName.equals("com.for_wd.streampro")) {

                            if (f == 1) {

                            } else {

                                f = 1;
                                facebook = true;

                                customHandlerfacebook.postDelayed(
                                        updateTimerThreadfacebook, 0);
                            }

                        } else {

                            if (f == 1) {

                                facebook = false;

                                customHandlerfacebook
                                        .removeCallbacks(updateTimerThreadfacebook);
                                f = 2;
                            }
                        }
                    //TODO такое можно везде убрать и чеки убрать!
                    /*}  else {

                        if (f == 1) {

                            facebook = false;

                            customHandlerfacebook
                                    .removeCallbacks(updateTimerThreadfacebook);
                            f = 2;
                        }

                    }*/

                    if (twcheck.equals("true")) {
                        if (packageName.equals("com.twitter.android")
                                || packageName.equals("com.levelup.touiteur")
                                || packageName
                                .equals("com.handmark.tweetcaster")
                                || packageName
                                .equals("com.hootsuite.droid.full")
                                || packageName.equals("com.echofon")
                                || packageName.equals("org.mariotaku.twidere")) {

                            if (t == 1) {

                            } else {

                                t = 1;
                                twitter = true;
                                customHandlertwitter.postDelayed(
                                        updateTimerThreadtwitter, 0);
                            }
                        } else {
                            if (t == 1) {

                                twitter = false;

                                customHandlertwitter
                                        .removeCallbacks(updateTimerThreadtwitter);
                                t = 2;
                            }
                        }
                    } else {
                        if (t == 1) {

                            twitter = false;

                            customHandlertwitter
                                    .removeCallbacks(updateTimerThreadtwitter);
                            t = 2;
                        }
                    }


                    if (instacheck.equals("true")) {
                        if (packageName.equals("com.instagram.android")) {

                            if (i == 1) {

                            } else {

                                i = 1;
                                instagram = true;
                                customHandlerinstagram.postDelayed(
                                        updateTimerThreadinstagram, 0);
                            }
                        } else {
                            if (i == 1) {

                                instagram = false;

                                customHandlerinstagram
                                        .removeCallbacks(updateTimerThreadinstagram);
                                i = 2;
                            }
                        }
                    } else {
                        if (i == 1) {

                            instagram = false;

                            customHandlerinstagram
                                    .removeCallbacks(updateTimerThreadinstagram);
                            i = 2;
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
                        mainhandler.sendEmptyMessage(0);

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

    public void save(String key, String value) {
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

            if (facebook = true) {

                facebooktemp3++;

                if (facebooktemp3 >= 60) {
                    facebooktemp3 = 0;
                    facebooktemp2 += 1;

                }
                if (facebooktemp2 >= 60) {
                    facebooktemp2 = 0;
                    facebooktemp1 += 1;
                }

                facemin = "" + facebooktemp2;
                facesec = "" + facebooktemp3;
                facehour = "" + facebooktemp1;

                // --------------------------------

                if (facebooktemp3 < 10) {
                    facesec = "0" + facebooktemp3;
                }

                if (facebooktemp2 < 10) {
                    facemin = "0" + facebooktemp2;
                }

                if (facebooktemp1 < 10) {
                    facehour = "0" + facebooktemp1;
                }



                save("faceSeconds", facesec);
                save("faceMinutes", facemin);
                save("faceHours", facehour);

                customHandlerfacebook.postDelayed(this, 0);

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

            if (twitter = true) {

                twittertemp3++;

                if (twittertemp3 >= 60) {
                    twittertemp3 = 0;
                    twittertemp2 += 1;
                }

                if (twittertemp2 >= 60) {
                    twittertemp2 = 0;
                    twittertemp1 += 1;
                }

                tmin = "" + twittertemp2;
                tsec = "" + twittertemp3;
                thour = "" + twittertemp1;



                if (twittertemp3 < 10) {
                    tsec = "0" + twittertemp3;
                }

                if (twittertemp2 < 10) {
                    tmin = "0" + twittertemp2;
                }

                if (twittertemp1 < 10) {
                    thour = "0" + twittertemp1;
                }



                save("twitSeconds", tsec);
                save("twitMinutes", tmin);
                save("twitHours", thour);

                customHandlertwitter.postDelayed(this, 0);

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

            if (instagram = true) {

                instagramtemp3++;

                if (instagramtemp3 >= 60) {
                    instagramtemp3 = 0;
                    instagramtemp2 += 1;
                }
                if (instagramtemp2 >= 60) {
                    instagramtemp2 = 0;
                    instagramtemp1 += 1;
                }

                imin = "" + instagramtemp2;
                isec = "" + instagramtemp3;
                ihour = "" + instagramtemp1;


                if (instagramtemp3 < 10) {
                    isec = "0" + instagramtemp3;
                }

                if (instagramtemp2 < 10) {
                    imin = "0" + instagramtemp2;
                }

                if (instagramtemp1 < 10) {
                    ihour = "0" + instagramtemp1;
                }

                save("instaSeconds", isec);
                save("instaMinutes", imin);
                save("instaHours", ihour);

                customHandlerinstagram.postDelayed(this, 0);

            }
        }

    };
}

