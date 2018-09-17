package com.clean.lot.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.clean.lot.entity.MessageEvent;
import com.clean.lot.tools.DeviceUtils;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MqttService extends Service {

    private MqttAndroidClient mqttClient;
    private String ClientId = "lot_";
    final String mqttServer = "";
    private static final String TAG ="MQTT";
    final String pushTopic = "lot";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        SharedPreferences reader = getSharedPreferences("settings",MODE_PRIVATE);
        String mqttServer = reader.getString("mqtt_server","tcp://202.182.118.148:61613").toString();
        ClientId  = ClientId+ DeviceUtils.readTelephoneSerialNum(getApplicationContext());
        mqttClient = new MqttAndroidClient(getApplicationContext(),mqttServer,ClientId);
        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("connect lost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                System.out.println("receive msg");
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                System.out.println("push msg");
            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName("lot2");
        mqttConnectOptions.setPassword("cloudhai".toCharArray());
        try{
            mqttClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    System.out.println("connect success");
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttClient.setBufferOpts(disconnectedBufferOptions);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    System.out.println("connect faild");

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventBus(MessageEvent event){
        if(event != null){
            String msg = JSON.toJSONString(event);
            try {
                MqttMessage message = new MqttMessage();
                message.setPayload(msg.getBytes());
                message.setQos(0);
                IMqttDeliveryToken token = mqttClient.publish(pushTopic, message);
                System.out.println("send out msg:"+msg);
            } catch (MqttException e) {
                System.err.println("Error Publishing: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        try {
            if(mqttClient!=null){
                //服务退出时client断开连接
                mqttClient.disconnect();
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "MqttService onDestroy executed");
    }
}
