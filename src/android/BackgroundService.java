package org.nov.myplugin;

/**
 * Created by diego on 11/7/17.
 */

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.support.v4.app.NotificationCompat;
import android.app.Notification;
import android.app.NotificationManager;

/**
 * Created by diego on 10/31/17.
 */
public class BackgroundService extends IntentService {
  String pkgName = "";
  public BackgroundService() {
    super("?");
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.d("BJ", "onStartCommand()");

    startForeground(1337, buildForegroundNotification());
    return(super.onStartCommand(intent, flags, startId));
  }

  @Override
  protected void onHandleIntent(Intent workIntent) {
    // Gets data from the incoming Intent
    String dataString = workIntent.getDataString();

    pkgName = workIntent.getStringExtra("PKG_NAME");
    Log.i("BJ", pkgName);
    // Do work here, based on the contents of dataString
    while(true) {
      Log.i("BJ", "The background service is running!!!");
      SystemClock.sleep(1000);
    }
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
}
