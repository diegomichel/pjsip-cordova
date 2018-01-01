package org.nov.pjsip;


import android.content.Context;

public class PjsipActions {
    public static PjsipActivity pjsipActivity = null;
    public static PjsipActions ourInstance = new PjsipActions();
    final scAudioManager scAudio = scAudioManager.getInstance();
    public Utils utils = null;
    private Context mContext;

    private PjsipActions() {
    }

    public static PjsipActions getInstance() {
        return ourInstance;
    }

    public void initialise(Context context){
        if (pjsipActivity == null){
            pjsipActivity = new PjsipActivity();
            pjsipActivity.initialise(mContext, "/");

            mContext = context;

            scAudio.initialise(context);

            utils = new Utils();
            Utils.init();
        }

    }

    public void connect(final String id, final String user, final String pass, final String domain, Context context){
        pjsipActivity.connect(id,user,pass,domain, context);

    }
    public void disconnect(Context context){
        pjsipActivity.disconnect(context);
    }


    public synchronized void acceptCall(Context context){
        scAudio.stopRingtone();
        PjsipActivity.acceptCall(context);
    }

    public synchronized void declineCall(Context context){
        scAudio.stopRingtone();
        PjsipActivity.declineCall(context);
    }

    public synchronized void endCall(Context context){
        scAudio.stopTone();
        PjsipActivity.endCall(context);
    }

    public synchronized void makeCall(final String number, final Context context){
        scAudio.startRingbackTone();
        pjsipActivity.makeCall(number, context);
    }

    public void setSpeakerMode(Boolean isActive){
        scAudio.setSpeakerMode(isActive);
    }

    public void muteMicrophone(Boolean isActive){
        scAudio.muteMicrophone(isActive);
    }

    public synchronized void holdCall(final Boolean isActive, Context context){
        pjsipActivity.holdCall(isActive);
    }

    public synchronized void sendDTMF(final String num, Context context){
        scAudio.playDTMF(num);
        pjsipActivity.sendDTMF(num);
    }
}
