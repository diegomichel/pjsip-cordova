package com.example.diego.myapplication;

import org.pjsip.pjsua2.pjsip_status_code;

interface MyAppObserver
{
    void notifyRegState(pjsip_status_code code, String reason, int expiration);

    void notifyIncomingCall(MyCall call);

    void notifyCallState(MyCall call);

    void notifyCallMediaState(MyCall call);

    void notifyBuddyState(MyBuddy buddy);
}