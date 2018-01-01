package org.nov.pjsip;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class CallActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
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
