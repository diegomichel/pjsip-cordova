package org.nov.pjsip;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;


public class PJSIP {
    private static final String TAG = "PjSip";
    private static PjsipActions actions = null;

    static {
        try {
            System.loadLibrary("pjsua2");
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "UnsatisfiedLinkError: " + e.getMessage());
            Log.e(TAG, "This could be safely ignored if you " + "don't need video.");

        }
    }

    private PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
            }
        }
    };

    public PJSIP() {
        Log.d("PJSIP", "constructor");
    }

    public static void dumpIntent(Intent i) {
        Log.d(TAG, "dumpIntent - Intent:" + i);
    }

    public void initialize(Context context) {
        Log.d("PJSIP", "initialize");
        actions = PjsipActions.getInstance();
        actions.initialise(context);
    }


    public void execute(String action, JSONArray args, Context context) throws JSONException {
        Log.d(TAG, "Execute:" + action);
        actions = PjsipActions.getInstance();
        actions.initialise(context);

        if (action.equals("connect")) {
            final String id = args.getString(0);
            final String user = args.getString(1);
            final String pass = args.getString(2);
            final String domain = args.getString(3);
            actions.connect(id, user, pass, domain, context);
        }
            if(action.equals("disconnect"))
                actions.disconnect(context);

            else if(action.equals("declinecall"))
                actions.declineCall(context);
            else if(action.equals("endcall"))
                actions.endCall(context);
            else if(action.equals("makecall")) {
                final String number = args.getString(0);
                actions.makeCall(number, context);
            }
            else if(action.equals("acceptcall"))
                actions.acceptCall(context);
            else if(action.equals("activatespeaker")) {
                Boolean isActive = Boolean.valueOf(args.getString(0));
                actions.setSpeakerMode(isActive);
            }
            else if(actions.equals("mutemicrophone")) {
                Boolean isActive = Boolean.valueOf(args.getString(0));
                actions.muteMicrophone(isActive);
            }
            else if(actions.equals("holdcall")) {
                Boolean isActive = Boolean.valueOf(args.getString(0));
                actions.holdCall(isActive, context);
            }
            else if(actions.equals("dtmfcall")) {
                String num = args.getString(0);
                actions.sendDTMF(num, context);
            }

    }
}
