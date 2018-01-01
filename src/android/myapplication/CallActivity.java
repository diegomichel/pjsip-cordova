package org.nov.pjsip;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.apache.cordova.CordovaActivity;

import io.cordova.hellocordova.R;

public class CallActivity extends CordovaActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.getApplication().getResources().getIdentifier("activity_call", "layout", this.getApplication().getPackageName()));
        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    PjsipActions.getInstance().acceptCall(getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        final Button buttonEndCall = findViewById(R.id.button2);
        buttonEndCall.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    PjsipActions.getInstance().endCall(getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
