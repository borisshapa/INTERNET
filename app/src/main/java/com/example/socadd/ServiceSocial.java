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

public class ServiceSocial extends Service {

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

    SharedPreferences spf;

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
     * Этот метод загружает значения времени из spf.
     * Значения хранятся в строках и преобразуются в integer потом используются в handler
     **/

    public void load() {

        fbcheck = spf.getString("facebook", "true");
        twcheck = spf.getString("twitter", "true");
        instacheck = spf.getString("instagram", "true");
        rich = spf.getString("rich", "false");
        totals = spf.getString("total", "0.0");

        ser1 = spf.getString("servicehour", "0");
        ser2 = spf.getString("servicemin", "0");
        ser3 = spf.getString("servicesec", "0");

        starti = spf.getString("start", "false");

        fb1 = spf.getString("facebooksec", "0");
        fb2 = spf.getString("facebookmin", "0");
        fb3 = spf.getString("facebookhour", "0");

        twittr1 = spf.getString("twittersec", "0");
        twittr2 = spf.getString("twittermin", "0");
        twittr3 = spf.getString("twitterhour", "0");

        insta1 = spf.getString("instagramsec", "0");
        insta2 = spf.getString("instagrammin", "0");
        insta3 = spf.getString("instagramhour", "0");

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


        spf = PreferenceManager.getDefaultSharedPreferences(this);

        not = spf.getString("not", "false");
        clear = spf.getString("clear", "false");

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

        spf = PreferenceManager.getDefaultSharedPreferences(this);

        mainhandler = new Handler() {

            @SuppressWarnings("deprecation")
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                try {

                    if (clear.equals("true")) {

                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        String strDate = sdf.format(c.getTime());

                        if (strDate.equals("00:00:00")) {

                            save("facebooksec", "00");
                            save("facebookmin", "00");
                            save("facebookhour", "00");

                            save("twittersec", "00");
                            save("twittermin", "00");
                            save("twitterhour", "00");

                            save("instagramsec", "00");
                            save("instagrammin", "00");
                            save("instagramhour", "00");

                            save("servicesec", "00");
                            save("servicemin", "00");
                            save("servicehour", "00");

                            save("state", "low");
                            save("total", "0");

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

                    save("total", String.valueOf(total));

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

                        save("state", "low");

                        showNotification("State : Low");

                    } else {

                        if (addict < 0.2 && addict > 0.1) {

                            save("state", "Average");

                            showNotification("State : Average");

                        } else {

                            if (addict < 0.3 && addict > 0.2) {

                                save("state", "attention");

                                showNotification("State : Attention");

                            } else {

                                if (addict < 0.5 && addict > 0.4) {

                                    save("state", "Addicted");

                                    showNotification("State : Addicted");

                                } else {

                                    if (addict > 0.6) {

                                        save("state", "DANGER");

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

                    if (fbcheck.equals("true")) {
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
                    } else {

                        if (f == 1) {

                            facebook = false;

                            customHandlerfacebook
                                    .removeCallbacks(updateTimerThreadfacebook);
                            f = 2;
                        }

                    }

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

        Intent deleteIntent = new Intent(this, close.class);
        PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(this, 0,
                deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);




        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this);
        //TODO вставить название
        mBuilder.setContentTitle("мой проект");


        mBuilder.setSmallIcon(R.drawable.ic_launcher);



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

        String close = spf.getString("close", "no");

        if (start.equals("true") && close.equals("no")) {

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancelAll();

            startService(new Intent(ServiceSocial.this, ServiceSocial.class));

        } else if (start.equals("true") && close.equals("yes")) {

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancelAll();

        } else {

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancelAll();

        }

    }

    public void save(String key, String value) {
        Editor edit = spf.edit();
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

                facemin = "" + facebooktemp1;
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



                save("facebooksec", facesec);
                save("facebookmin", facemin);
                save("facebookhour", facehour);

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



                save("twittersec", tsec);
                save("twittermin", tmin);
                save("twitterhour", thour);

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
                ihour = "" + instagramtemp2;


                if (instagramtemp3 < 10) {
                    isec = "0" + instagramtemp3;
                }

                if (instagramtemp2 < 10) {
                    imin = "0" + instagramtemp2;
                }

                if (instagramtemp1 < 10) {
                    ihour = "0" + instagramtemp1;
                }

                save("instagramsec", isec);
                save("instagrammin", imin);
                save("instagramhour", ihour);

                customHandlerinstagram.postDelayed(this, 0);

            }
        }

    };
}

