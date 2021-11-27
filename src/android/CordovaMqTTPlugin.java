package com.arcoirislabs.plugin.mqtt;

import android.util.Base64;
import android.util.Log;

import com.arcoirislabs.core.Core;
import com.arcoirislabs.core.DisconnectCallback;
import com.arcoirislabs.core.GoCallback;
import com.arcoirislabs.core.PublishCallback;
import com.arcoirislabs.core.SubscribeCallback;
import com.arcoirislabs.core.UnsubscribeCallback;

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
        Core.connect(url,
                cid,
                ka,
                cleanSess,
                connTimeOut,
                uname,
                pass,
                willTopic,
                willPayload,
                willQos,
                willRetain,
                version,
                isBinaryWillPayload, new GoCallback() {
                    @Override
                    public void mqttError(Exception e) {

                    }

                    @Override
                    public void onConnect() {
                        connected = true;
                        JSONObject dis = new JSONObject();
                        try {
                            dis.put("type", "connected");
                            dis.put("call", "connected");
                            dis.put("response", "connected");
                            dis.put("connectionStatus", connected);
                            sendUpdate(dis);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onConnectError(Exception e) {
                        connected = false;
                        JSONObject dis = new JSONObject();
                        try {
                            dis.put("type", "failure");
                            dis.put("call", "failure");
                            dis.put("response", "fail to connect");
                            dis.put("message", e.toString());
                            dis.put("connectionStatus", connected);
                            sendUpdate(dis);
                        } catch (JSONException err) {
                            err.printStackTrace();
                        }
                    }

                    @Override
                    public void onConnectionLost(Exception cause) {
                        connected = false;
                        JSONObject dis = new JSONObject();
                        try {
                            dis.put("type", "connectionLost");
                            dis.put("message", cause.toString());
                            dis.put("call", "disconnected");
                            dis.put("connectionStatus", false);
                            sendUpdate(dis);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onDisconnect() {

                    }

                    @Override
                    public void onDisconnectError(Exception e) {

                    }

                    @Override
                    public void onMessageRecevied(String topic, byte[] payload) {
                        JSONObject dis = new JSONObject();
                        try {
                            dis.put("type", "messageArrived");
                            dis.put("topic", topic);
//                            if (isBinaryPayload) {
//                                dis.put("payload", Base64.encodeToString(message.getPayload(), Base64.DEFAULT));
//                            } else {
//                                dis.put("payload", message.toString());
//                            }
//                            dis.put("call", "onPublish");
//                            dis.put("connectionStatus", client.isConnected());
//                            dis.put("qos",message.getQos());
//                            dis.put("isRetained",message.isRetained());
//                            dis.put("isDuplicate",message.isDuplicate());
                            sendUpdate(dis);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onPublish() {

                    }

                    @Override
                    public void onPublishError(Exception e) {

                    }

                    @Override
                    public void onReconnecting() {

                    }

                    @Override
                    public void onSubscribe() {

                    }

                    @Override
                    public void onSubscribeError(Exception e) {

                    }

                    @Override
                    public void onUnsubscribe() {

                    }

                    @Override
                    public void onUnsubscribeError(Exception e) {

                    }
                });
    }
    private void publish(JSONArray args) throws JSONException {
        Core.publish(args.getString(0), args.getString(1), new PublishCallback() {
            @Override
            public void onFailure(Exception e) {
                JSONObject dis = new JSONObject();
                try {
                    dis.put("type", "publish");
                    dis.put("call", "failure");
                    dis.put("response", "not published");
//                    dis.put("isPayloadDuplicate", payload.isDuplicate());
//                    dis.put("qos", payload.getQos());
//                    dis.put("connectionStatus", client.isConnected());
                    sendOnceUpdate(dis);
                } catch (JSONException err) {
                    err.printStackTrace();
                }
            }

            @Override
            public void onSuccess() {
                JSONObject dis = new JSONObject();
                try {
                    dis.put("type", "publish");
                    dis.put("call", "success");
                    dis.put("response", "published");
//                    dis.put("isPayloadDuplicate", payload.isDuplicate());
//                    dis.put("qos", payload.getQos());
//                    dis.put("connectionStatus", client.isConnected());
                    sendOnceUpdate(dis);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    private void subscribe(final JSONArray args) throws JSONException {
        Core.subscribe(args.getString(0), new SubscribeCallback() {
            @Override
            public void onFailure(Exception e) {
                JSONObject dis = new JSONObject();
                try {
                    dis.put("type", "subscribe");
                    dis.put("call", "failure");
                    dis.put("response", "cannot subscribe to " + args.getString(0));
                    dis.put("message", e.getMessage());
                    //dis.put("connectionStatus", client.isConnected());
                    sendOnceUpdate(dis);
                } catch (JSONException err) {
                    err.printStackTrace();
                }
            }

            @Override
            public void onSuccess() {
                JSONObject dis = new JSONObject();
                try {
                    dis.put("type", "subscribe");
                    dis.put("call", "success");
                    dis.put("response", "subscribed to " + args.getString(0));
                    //dis.put("connectionStatus", client.isConnected());
                    sendOnceUpdate(dis);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void disconnect(){
        Core.disconnect(new DisconnectCallback() {
            @Override
            public void onFailure(Exception e) {
                JSONObject res = new JSONObject();
                try {
                    res.put("type","disconnect");
                    res.put("call","failure");
                    //res.put("connectionStatus",client.isConnected());
                    res.put("message",e.toString());
                    sendOnceUpdate(res);
                } catch (JSONException err) {
                    err.printStackTrace();
                }
            }

            @Override
            public void onSuccess() {
                JSONObject res = new JSONObject();
                try {
                    res.put("type","disconnect");
                    res.put("call","success");
                    //res.put("connectionStatus",client.isConnected());
                    sendOnceUpdate(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void unsubscribe(final JSONArray args) throws JSONException {
        Core.unsubscribe(args.getString(0), new UnsubscribeCallback() {
            @Override
            public void onFailure(Exception e) {
                JSONObject res = new JSONObject();
                try {
                    res.put("type","unsubscribe");
                    res.put("call","failure");
                    //res.put("connectionStatus",client.isConnected());
                    res.put("unsubscribedTopic",args.getString(0));
                    res.put("message",e.toString());
                    sendOnceUpdate(res);
                } catch (JSONException err) {
                    err.printStackTrace();
                }
            }

            @Override
            public void onSuccess() {
                JSONObject res = new JSONObject();
                try {
                    res.put("type","unsubscribe");
                    res.put("call","success");
                    //res.put("connectionStatus",client.isConnected());
                    res.put("unsubscribedTopic",args.getString(0));
                    sendOnceUpdate(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
