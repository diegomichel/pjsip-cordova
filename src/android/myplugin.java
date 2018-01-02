package org.nov.myplugin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nov.myplugin.BackgroundService;
import org.nov.pjsip.Permissions;

/**
 * This class echoes a string called from JavaScript.
 */
public class MyPlugin extends CordovaPlugin {
    String TAG="MyPlugin";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("coolMethod")) {
            String message = args.getString(0);
            this.coolMethod(message, callbackContext);
            return true;
        }
        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            Permissions permissions = new Permissions();
            permissions.request(cordova.getActivity());
            startBackgroundService();

            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    private void startBackgroundService() {
        Intent mServiceIntent = new Intent(this.cordova.getActivity(), BackgroundService.class);
        mServiceIntent.putExtra("PKG_NAME", cordova.getActivity().getPackageName());
        this.cordova.getActivity().startService(mServiceIntent);
    }
}
