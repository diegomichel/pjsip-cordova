package org.nov.myplugin;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by diego on 10/31/17.
 */

public class WakeUpReceiver extends WakefulBroadcastReceiver {
    String TAG="WakeUpReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        String pkgName = intent.getStringExtra("PKG_NAME");
        Log.i(TAG, "pkgName" + pkgName);
        //This part only restarts the background service...
        Log.i(TAG, "onReceive trying to restart server");
        Intent service = new Intent(context, BackgroundService.class);
        service.putExtra("PKG_NAME", pkgName);
        startWakefulService(context, service);

        Log.i(TAG, "onReceive trying reopen app");
        //This part reopens the app... useful if you are reciving a call for example.
        Intent intento = new Intent("android.intent.category.LAUNCHER");

        intento.setClassName(pkgName, pkgName + ".MainActivity");
        intento.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intento);
    }
}
