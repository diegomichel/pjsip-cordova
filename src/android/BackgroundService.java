package org.nov.myplugin;

/**
 * Created by diego on 11/7/17.
 */

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

/**
 * Created by diego on 10/31/17.
 */
public class BackgroundService extends Service {
  String pkgName = "";
  PJSIP pjsip = null;

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.d("BJ", "onStartCommand()");

    startForeground(1337, buildForegroundNotification());
    pjsip = new PJSIP();

    Log.d("UNIQUE", "onHandleIntent");
    try {
      pjsip.execute("connect", config("1000", "1000", "s3cure", "staging.chronosar.com"), this);
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
    Log.i("BJ", "BackgroundService being removed");

    Intent intent = new Intent(this, WakeUpReceiver.class);
    intent.putExtra("PKG_NAME", pkgName);
    sendBroadcast(intent);
  }

  private Notification buildForegroundNotification() {
    NotificationCompat.Builder b=new NotificationCompat.Builder(this);

    b.setOngoing(true)
      .setContentTitle("Waiting for incoming calls")
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
