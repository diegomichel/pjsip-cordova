package com.example.diego.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class WakeUpReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("BJ", "onReceive trying to restart server");
        Intent service = new Intent(context, BackgroundService.class);
        startWakefulService(context, service);
    }
}
