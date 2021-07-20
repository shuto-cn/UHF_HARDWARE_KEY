package cordova.plugin.hardwearKey;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class hardwearKey extends CordovaPlugin {


    private KeyReceiver keyReceiver;
    @Override
    protected void pluginInitialize() {
        super.pluginInitialize();
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        switch (action) {
            case "startListen":
                this.startListen(callbackContext);
                return true;
            case "stopListen":
                this.stopListen(callbackContext);
                return true;
        }
        return false;
    }

    private void stopListen(CallbackContext callbackContext) {
        cordova.getContext().unregisterReceiver(keyReceiver);
        callbackContext.success("OK");
    }

    private void startListen(CallbackContext callbackContext) {
        keyReceiver = new KeyReceiver(callbackContext);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.rfid.FUN_KEY");
        filter.addAction("android.intent.action.FUN_KEY");
        cordova.getContext().registerReceiver(keyReceiver , filter);
    }

    @Override
    public void onDestroy() {
        cordova.getContext().unregisterReceiver(keyReceiver);
        super.onDestroy();
    }


    private class KeyReceiver extends BroadcastReceiver {
        private CallbackContext cb;

        public KeyReceiver(CallbackContext cb) {
            super();
            this.cb = cb;
        }
        public KeyReceiver() {
            super();
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            int keyCode = intent.getIntExtra("keyCode", 0);
            if (keyCode == 0) {
                keyCode = intent.getIntExtra("keycode", 0);
            }
            boolean keyDown = intent.getBooleanExtra("keydown", false);
            JSONObject jo = new JSONObject();
            try {
                jo.put("keyCode", keyCode);
                jo.put("keyDown", keyDown);
                PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, jo);
                pluginResult.setKeepCallback(true);
                cb.sendPluginResult(pluginResult);
            } catch (JSONException e) {
                e.printStackTrace();
                cb.error(e.getMessage());
            }
        }

    }
}
