package com.arcoirislabs.plugin.mqtt;

import android.util.Base64;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class CordovaMqTTPlugin extends CordovaPlugin {
    CallbackContext syncCB,asyncCB;
    
    boolean connected;
    @Override
    public boolean execute(String action, final JSONArray args, CallbackContext callbackContext) throws JSONException {
        if(action.equals("connect")){
            this.asyncCB = callbackContext;
//            if (connection== null){
//
//            }else{
//                sendOnceUpdate("already connected");
//            }

            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        connect(args.getString(0), args.getString(1), args.getInt(2), args.getBoolean(3), args.getInt(4), args.getString(5), args.getString(6), args.getString(7), args.getString(8), args.getInt(9), args.getBoolean(10), args.getString(11), args.getBoolean(12), args.getBoolean(13));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return true;
        }
        if(action.equals("publish")){
            this.syncCB = callbackContext;
//            if (connection!= null){
//
//            }else{
//                sendOnceUpdate("already connected");
//            }
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        publish(args);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return true;
        }
        if(action.equals("subscribe")){

            this.syncCB = callbackContext;
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        subscribe(args);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return true;
        }
        if(action.equals("disconnect")){
            this.syncCB = callbackContext;
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    disconnect();
                }
            });
            return true;
        }
        if(action.equals("unsubscribe")){
            this.syncCB = callbackContext;
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        unsubscribe(args);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return true;
        }

        return false;
    }

    private void connect(String url,String cid,int ka,boolean cleanSess,int connTimeOut,String uname, String pass,String willTopic,String willPayload,int willQos,boolean willRetain,String version,boolean isBinaryPayload,boolean isBinaryWillPayload) {
        
    }
    private void publish(JSONArray args) throws JSONException {
        

    }
    private void subscribe(final JSONArray args) throws JSONException {
        
    }
    private void disconnect(){
        
    }
    private void unsubscribe(final JSONArray args) throws JSONException {
        
    }
    private void sendOnceUpdate(JSONObject message){
        if (syncCB != null) {
            PluginResult result = new PluginResult(PluginResult.Status.OK,message);
            result.setKeepCallback(false);
            syncCB.sendPluginResult(result);

            Log.i("mqttalabs","\nfor subscribe the callback id is "+syncCB.getCallbackId());
        }
    }
    private void sendUpdate(JSONObject message){
        if (asyncCB != null) {
            PluginResult result = new PluginResult(PluginResult.Status.OK,message);
            result.setKeepCallback(true);
            asyncCB.sendPluginResult(result);

            Log.i("mqttalabs","\nfor subscribe the callback id is "+asyncCB.getCallbackId());
        }
    }

}
