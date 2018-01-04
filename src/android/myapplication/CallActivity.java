package org.nov.pjsip;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.apache.cordova.CordovaActivity;

public class CallActivity extends CordovaActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.getApplication().getResources().getIdentifier("activity_call", "layout", this.getApplication().getPackageName()));
        final Button button = findViewById(this.getApplication().getResources().getIdentifier("button1", "id", this.getApplication().getPackageName()));
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    PjsipActions.getInstance().acceptCall(getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        final Button buttonEndCall = findViewById(this.getApplication().getResources().getIdentifier("button2", "id", this.getApplication().getPackageName()));
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
