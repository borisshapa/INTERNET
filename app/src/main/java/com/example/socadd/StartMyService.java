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

public class StartMyService extends BroadcastReceiver {

    SharedPreferences spf;

    @Override
    public void onReceive(Context context, Intent intent) {

        spf = PreferenceManager.getDefaultSharedPreferences(context);

        //Принимает значения от sharedPreferences
        String bootchk = spf.getString("boot", "true");
        String starti = spf.getString("compoundButton", "false");

        if (bootchk.equals("true") && starti.equals("true")) {
                context.startService(new Intent(context, MyService.class));
                Toast.makeText(context, "InternerTime started",Toast.LENGTH_SHORT).show();
        }

    }
}