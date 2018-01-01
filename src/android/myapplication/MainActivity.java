package org.nov.pjsip;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.apache.cordova.CordovaActivity;

public class MainActivity extends CordovaActivity {
/*    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Permissions permissions = new Permissions();
        permissions.request(this);
        
        Intent mServiceIntent = new Intent(this, BackgroundService.class).putExtra(BackgroundService.EXTRA_FOREGROUND, true);
        mServiceIntent.setData(Uri.parse("http://diegomichel.org"));
        mServiceIntent.putExtra("PKG_NAME", BuildConfig.APPLICATION_ID);

        startService(mServiceIntent);

        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.button3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CallActivity.class));
            }
        });
    }
    */
}
