package org.nov.pjsip;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AuthCredInfo;
import org.pjsip.pjsua2.AuthCredInfoVector;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.StringVector;
import org.pjsip.pjsua2.pjsip_inv_state;
import org.pjsip.pjsua2.pjsip_role_e;
import org.pjsip.pjsua2.pjsip_status_code;

import java.util.HashMap;
import java.util.Map;

public class PjsipActivity implements MyAppObserver {

    public static MyApp app = null;
    public static MyAccount account = null;
    public static AccountConfig accCfg = null;
    public static MyCall currentCall = null;
    static private String TAG = "PjsipActivity";
    static private String outGoingCallNumber = "";
    static private String inComingCallNumber = "";
    private static Map<String,String> userSettings = new HashMap<>();
    private Context mcontext;

    public static synchronized void acceptCall(Context context) {
        if (currentCall != null) {
            MyApp.checkThread();
            Log.d(TAG, "=====Answer Call=========");
            CallOpParam prm = new CallOpParam();
            prm.setStatusCode(pjsip_status_code.PJSIP_SC_OK);
            try {
                currentCall.answer(prm);
                Utils.executeJavascript("cordova.plugins.PJSIP.callState({state:'established'})");
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
        }
    }

    public static synchronized void declineCall(Context context) {
        if (currentCall != null) {
            MyApp.checkThread();
            Log.d(TAG, "=====Decline Call=========");
            CallOpParam prm = new CallOpParam();
            prm.setStatusCode(pjsip_status_code.PJSIP_SC_DECLINE);
            try {
                currentCall.answer(prm);
                currentCall = null;
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
        }
    }

    public static synchronized void endCall(Context context) {
        if (currentCall != null) {
            MyApp.checkThread();
            CallOpParam prm = new CallOpParam();
            prm.setStatusCode(pjsip_status_code.PJSIP_SC_DECLINE);
            try {
                currentCall.hangup(prm);
                currentCall = null;
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
        }

    }

    public void initialise(Context context, String absPath){
        this.mcontext = context;

        if (app == null){
            app = new MyApp();
            Log.d(TAG,"Logs:"+absPath);
            app.init(this, absPath);
        }

        if (app.accList.size() == 0) {
            accCfg = new AccountConfig();
            accCfg.setIdUri("sip:localhost");
            accCfg.getNatConfig().setIceEnabled(true);
            accCfg.getVideoConfig().setAutoTransmitOutgoing(true);
            accCfg.getVideoConfig().setAutoShowIncoming(true);
            account = app.addAcc(accCfg);
        } else {
            account = app.accList.get(0);
            accCfg = account.cfg;
        }
    }

    public synchronized void disconnect(Context context) {
        String acc_id 	 = "sip:localhost";
        String registrar = "";
        String proxy 	 = "";
        String username  = "";
        String password  = "";

        userSettings.put("user","");
        userSettings.put("password","");
        userSettings.put("systemIP","");
        userSettings.put("proxyIP","");

        Log.d(TAG, "Registration with the following settings: (acc_id,"+acc_id+"),(registrar,"+registrar+"),(proxy,"+proxy+"),(username,"+username+"),(password,"+password+")");

        MyApp.checkThread();
        accCfg.setIdUri(acc_id);
        accCfg.getRegConfig().setRegistrarUri(registrar);
        AuthCredInfoVector creds = accCfg.getSipConfig().getAuthCreds();
        creds.clear();
        if (username.length() != 0) {
            creds.add(new AuthCredInfo("Digest", "*", username, 0, password));
        }
        StringVector proxies = accCfg.getSipConfig().getProxies();
        proxies.clear();
        if (proxy.length() != 0) {
            proxies.add(proxy);
        }

        accCfg.getNatConfig().setIceEnabled(true);

        try {
            account.modify(accCfg);
        } catch (Exception e) {
            Log.e("PJSIP","Logs:"+e.toString());

        }
    }

    public void connect(final String id, final String user, final String pass, final String systemIP, Context context) {
        String acc_id 	 = "sip:"+user+"@"+systemIP;
        String registrar = "sip:"+user+"@"+systemIP;
        String proxy 	 = "";
        String username  = user;
        String password  = pass;

        this.mcontext = context;

        userSettings.put("user",user);
        userSettings.put("password",pass);
        userSettings.put("systemIP",systemIP);
        userSettings.put("proxyIP","");

        Log.d(TAG, "Registration with the following settings: (acc_id,"+acc_id+"),(registrar,"+registrar+"),(proxy,"+proxy+"),(username,"+username+"),(password,"+password+")");

        MyApp.checkThread();
        accCfg.setIdUri(acc_id);
        accCfg.getRegConfig().setRegistrarUri(registrar);
        AuthCredInfoVector creds = accCfg.getSipConfig().getAuthCreds();
        creds.clear();
        if (username.length() != 0) {
            creds.add(new AuthCredInfo("Digest", "*", username, 0, password));
        }
        StringVector proxies = accCfg.getSipConfig().getProxies();
        proxies.clear();
        if (proxy.length() != 0) {
            proxies.add(proxy);
        }

        accCfg.getNatConfig().setIceEnabled(true);

        try {
            account.modify(accCfg);
           // context.success("Successfully registered");
        } catch (Exception e) {
            Log.e("PJSIP","Logs:"+e.toString());

        }

    }

    public void makeCall(final String number, final Context context) {

        if (currentCall != null ){
            Log.w(TAG,"There is already a call");
            return;
        }

        String systemIP=userSettings.get("systemIP");
        String buddy_uri = "sip:"+number+"@"+systemIP;

        outGoingCallNumber = number;

        Log.i(TAG,"A call will be made to:"+buddy_uri);

        MyApp.checkThread();
        MyCall call = new MyCall(account, -1);
        CallOpParam prm = new CallOpParam(true);

        try {

            call.makeCall(buddy_uri, prm);

        } catch (Exception e) {
            call.delete();
            e.printStackTrace();
            return;
        }

        currentCall = call;
    }

    public void sendDTMF(final String num) {
        if (currentCall != null){
            MyApp.checkThread();
            try {
                currentCall.dialDtmf(num);
            } catch (Exception e) {
                Log.d(TAG,e.toString());
            }


        }
    }

    public void holdCall(final Boolean isActive) {
        if (currentCall != null){
            MyApp.checkThread();
            CallOpParam prm = new CallOpParam(true);
            if (isActive){
                try {
                    currentCall.setHold(prm);
                } catch (Exception e) {
                    Log.d(TAG,e.toString());
                }
            } else{
                prm.getOpt().setFlag(1);
                try {
                    currentCall.reinvite(prm);
                } catch (Exception e) {
                    Log.d(TAG,e.toString());
                }
            }
        }
    }

    public synchronized void notifyRegState(pjsip_status_code code, String reason, int expiration){
        Log.d(TAG,"========notifyRegState========"+"Code:"+code+", reason:"+reason+", expiration:"+expiration);
    }

    public synchronized void notifyIncomingCall(MyCall call){
        Log.d(TAG,"=====notifyIncomingCall=========");

        MyApp.checkThread();

        CallOpParam prm = new CallOpParam();

        if (currentCall != null) {
            // TODO: set status code
            call.delete();
            return;
        }
        scAudioManager scAudio = scAudioManager.getInstance();
        scAudio.startRingtone();

        prm.setStatusCode(pjsip_status_code.PJSIP_SC_RINGING); //ringing mode

        try {
            inComingCallNumber = call.getInfo().getRemoteUri();
            call.answer(prm);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        wakeUpDevice(mcontext);

        Intent myIntent = new Intent(this.mcontext, CallActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mcontext.startActivity(myIntent);

        currentCall = call;
    }

    private static void wakeUpDevice(Context mcontext) {
        PowerManager pm = (PowerManager) mcontext.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();
    }

    public synchronized void notifyCallState(MyCall call){
        MyApp.checkThread();

        CallInfo ci;
        try {
            ci = call.getInfo();
        } catch (Exception e) {
            ci = null;
        }

        String call_state = "";

        if (ci.getState().swigValue() < pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED.swigValue()) {

            if (ci.getRole() == pjsip_role_e.PJSIP_ROLE_UAS) {
                Log.d(TAG,"======notifyCallState======Incoming call");
                Utils.executeJavascript("cordova.plugins.PJSIP.callState({state:'incall',inComingCallNumber:'" + inComingCallNumber + "'})");
                /* Default button texts are already 'Accept' & 'Reject' */
            } else if (ci.getRole() == pjsip_role_e.PJSIP_ROLE_UAC){
                if  (ci.getState().swigValue() == pjsip_inv_state.PJSIP_INV_STATE_CALLING.swigValue()){
                    Utils.executeJavascript("cordova.plugins.PJSIP.callState({state:'outcall',outGoingCallNumber:'" + outGoingCallNumber + "'})");
                    Log.d(TAG,"======notifyCallState======PJSIP_INV_STATE_CALLING");
                }else if  (ci.getState().swigValue() == pjsip_inv_state.PJSIP_INV_STATE_EARLY.swigValue()){
                    Log.d(TAG,"======notifyCallState======PJSIP_INV_STATE_EARLY");
                }else if  (ci.getState().swigValue() == pjsip_inv_state.PJSIP_INV_STATE_CONNECTING.swigValue()){
                    Log.d(TAG,"======notifyCallState======PJSIP_INV_STATE_CONNECTING");
                }
            }
        } else if (ci.getState().swigValue() >= pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED.swigValue()) {
            call_state = ci.getStateText();
            if (ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED) {
                Log.d(TAG,"======notifyCallState======PJSIP_INV_STATE_CONFIRMED - state:"+call_state);
                Utils.executeJavascript("cordova.plugins.PJSIP.callState({state:'established'})");
                scAudioManager scAudio = scAudioManager.getInstance();
                scAudio.stopTone();
            } else if (ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED && currentCall != null) {
                currentCall = null;
                Utils.executeJavascript("cordova.plugins.PJSIP.callState({state:'endcall'})");
                scAudioManager scAudio = scAudioManager.getInstance();
                scAudio.stopRingtone();
                Log.d(TAG,"======notifyCallState======PJSIP_INV_STATE_DISCONNECTED - state"+ci.getLastReason());
            }
        }
    }

    public void notifyCallMediaState(MyCall call){
        Log.d(TAG,"======notifyCallMediaState======"+call.toString());
    }

    public void notifyBuddyState(MyBuddy buddy){
        Log.d(TAG,"=======notifyBuddyState=========");
    }
}
