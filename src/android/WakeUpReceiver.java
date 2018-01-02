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
        restartBackgroundService(context, pkgName);
    }

    private void restartMainActivity(Context context, String pkgName) {
        Log.i(TAG, "Restarting App");

        Intent intent = new Intent("android.intent.category.LAUNCHER");
        intent.setClassName(pkgName, pkgName + ".MainActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }

    private void restartBackgroundService(Context context, String pkgName) {
        Log.i(TAG, "Restarting BackgroundService");

        Intent intent = new Intent(context, BackgroundService.class);
        intent.putExtra("PKG_NAME", pkgName);

        startWakefulService(context, intent);
    }
}
