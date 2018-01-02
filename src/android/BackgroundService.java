package org.nov.myplugin;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.support.v4.app.NotificationCompat;
import android.app.Notification;
import android.app.NotificationManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.nov.pjsip.PJSIP;

public class BackgroundService extends Service {
    String TAG = "BackgroundService";
    String pkgName = "";
    String config = "";
    PJSIP pjsip = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand()");

        this.pkgName = intent.getStringExtra("PKG_NAME");
        this.config = intent.getStringExtra("CONFIG");

        startForeground(1337, buildForegroundNotification());
        pjsip = new PJSIP();

        Log.d(TAG, "onHandleIntent");
        try {
            JSONArray config = new JSONArray(this.config);
            pjsip.execute("connect", config, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return(super.onStartCommand(intent, flags, startId));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent workIntent){
        Log.i(TAG, "BackgroundService being removed");

        Intent intent = new Intent(this, WakeUpReceiver.class);
        intent.putExtra("PKG_NAME", pkgName);
        intent.putExtra("CONFIG", config);
        sendBroadcast(intent);
    }

    private Notification buildForegroundNotification() {
        NotificationCompat.Builder b=new NotificationCompat.Builder(this);

        b.setOngoing(true)
                .setContentTitle("Waiting for incoming calls")
                .setSmallIcon(android.R.drawable.stat_sys_download);

        return(b.build());
    }
}
