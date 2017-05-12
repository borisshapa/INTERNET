package com.example.socadd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;



/**
 *
 * This is a broadcast receiver, used to trigger the service in boot time, if user choose to do this.
 *
 **/

public class ServiceStarter extends BroadcastReceiver {

    SharedPreferences spf;

    @Override
    public void onReceive(Context context, Intent intent) {

        spf = PreferenceManager.getDefaultSharedPreferences(context);

        //Принимает значения от spf
        String bootchk = spf.getString("boot", "true");
        String starti = spf.getString("start", "false");

        if (bootchk.equals("true") && starti.equals("true")) {
                context.startService(new Intent(context, ServiceSocial.class));
                Toast.makeText(context, "... started",Toast.LENGTH_SHORT).show();
        }

    }
}