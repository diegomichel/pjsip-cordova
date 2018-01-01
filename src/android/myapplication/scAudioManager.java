package org.nov.pjsip;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.Ringtone;
import android.media.ToneGenerator;
import android.provider.Settings;
import android.util.Log;

import java.io.IOException;

public class scAudioManager {
    private static String TAG = "scAudioManager";
    private static scAudioManager ourInstance = new scAudioManager();
    String tempFileName;
    private MediaPlayer mPlayer = null;
    private MediaRecorder mRecorder = null;
    private ToneGenerator mRingbackTone;
    private Ringtone mRingtone = null;
    private Context mContext;

    private scAudioManager() {
    }

    public static scAudioManager getInstance() {
        return ourInstance;
    }

    void initialise(Context mContext) {
        this.mContext = mContext;
        tempFileName = mContext.getExternalCacheDir().getAbsolutePath() + "/audiorecordtest.3gp";
    }

    public void setSpeakerMode(final Boolean isActive) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                Log.d("SIP", "setSpeakerMode");
                AudioManager am = ((AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE));
                am.setMode(AudioManager.MODE_NORMAL);
                am.setSpeakerphoneOn(isActive);
            }
        });
    }

    public void muteMicrophone(Boolean state) {
        Log.d("SIP", "muteMicrophone: " + state.toString());
        AudioManager am = ((AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE));
        am.setMicrophoneMute(state);
    }

    public synchronized void startRingbackTone() {
        this.playTone(1,true);
    }

    public synchronized void startRingtone() {
        this.playTone(1,true);
    }

    public synchronized void stopRingtone() {
        this.stopTone();
    }

    public synchronized void startBusyTone() {
        this.playTone(1,false);

    }

    public void playDTMF(String num){
        Log.i(TAG,"The dtmf number '"+num+"' will be called.");
        int sound=0;
        switch (num) {
            case "0":
                sound = 1;
                break;
            case "1":
                sound = 1;
                break;
            case "2":
                sound = 1;
                break;
            case "3":
                sound = 1;
                break;
            case "4":
                sound = 1;
                break;
            case "5":
                sound = 1;
                break;
            case "6":
                sound = 1;
                break;
            case "7":
                sound = 1;
                break;
            case "8":
                sound = 1;
                break;
            case "9":
                sound = 1;
                break;
            case "#":
                sound = 1;
                break;
            case "*":
                sound = 1;
                break;
        }
        if (sound!=0)
            this.playTone(sound,false);
    }

    public void checkAudio(Context mContext) {
        this.setSpeakerMode(true);
        mPlayer = MediaPlayer.create(mContext, 2);
        mPlayer.setLooping(false);
        this.startRecording();

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                try {
                    //stop recording
                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;

                    //play back the recorded file
                    mPlayer = new MediaPlayer();
                    mPlayer.setDataSource(tempFileName);
                    mPlayer.prepare();
                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            //callbackContext.success("OK");
                        }
                    });
                    mPlayer.start();
                } catch (IOException e) {
                    Log.e("startPlayRecordedFile", "failed");
                }
            }
        });
        mPlayer.start();

    }

    public synchronized void stopTone() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer = null;
        }
    }

    private synchronized void playTone(int tone,boolean loop){
        if (mPlayer != null)
            this.stopTone();

        //TODO: PLACEHOLDER.
        mPlayer = MediaPlayer.create(mContext, Settings.System.DEFAULT_RINGTONE_URI);
        mPlayer.setLooping(loop);
        mPlayer.start();
    }

    public void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(tempFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("startRecording", "prepare() failed");
        }
        mRecorder.start();
    }
}
