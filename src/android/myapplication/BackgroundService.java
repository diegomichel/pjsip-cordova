package org.nov.pjsip;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

public class BackgroundService extends Service {
    static final String EXTRA_FOREGROUND="fg";
    static final String EXTRA_IMPORTANTISH="importantish";
    private static final String CHANNEL_MIN="channel_min";
    private static final String CHANNEL_LOW="channel_low";
    private static final String TAG = "BackgroundService";
    PJSIP pjsip = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand()");

        boolean foreground = intent.getBooleanExtra(EXTRA_FOREGROUND, false);
        boolean importantish = intent.getBooleanExtra(EXTRA_IMPORTANTISH, false);

        if (foreground) {
            String channel=importantish ? CHANNEL_LOW : CHANNEL_MIN;

            startForeground(1337, buildForegroundNotification(channel));
        }

        pjsip = new PJSIP();

        Log.d("UNIQUE", "onHandleIntent");
        try {
            pjsip.execute("connect", config("1000", "1000", "s3cure", "staging.chronosar.com"), this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent workIntent){
        Log.d("UNIQUE", "onTaskRemoved");
        Intent intent = new Intent(this, WakeUpReceiver.class).putExtra(BackgroundService.EXTRA_FOREGROUND, true);
        sendBroadcast(intent);
    }

    private Notification buildForegroundNotification(String channel) {
        NotificationCompat.Builder b=new NotificationCompat.Builder(this);

        b.setOngoing(true)
                .setContentTitle("Notification title...")
                .setSmallIcon(android.R.drawable.stat_sys_download);

        return(b.build());
    }

    private JSONArray config(String id, String username, String password, String host) {
        JSONArray config = new JSONArray();

        config.put(id);
        config.put(username);
        config.put(password);
        config.put(host);

        return config;
    }
}
