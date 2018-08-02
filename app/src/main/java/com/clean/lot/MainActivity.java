package com.clean.lot;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.clean.lot.entity.MessageEvent;
import com.clean.lot.fragment.ControlFragment;
import com.clean.lot.fragment.IndexFragment;
import com.clean.lot.fragment.SettingFragment;

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

public class MainActivity extends AppCompatActivity {

    private String ClientId = "lot_";
    final String mqttServer = "tcp://202.182.118.148:61613";
    final String pushTopic = "lot";
    private MqttAndroidClient mqttClient;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Fragment index = getSupportFragmentManager().findFragmentByTag("index");
                    if(index == null){
                        index = new IndexFragment();
                }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_contrainer,index,"index")
                            .commit();
                    return true;
                case R.id.navigation_dashboard:
                    Fragment control = getSupportFragmentManager().findFragmentByTag("control");
                    if(control == null){
                        control = new ControlFragment();
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_contrainer,control,"control")
                            .commit();
                    return true;
                case R.id.navigation_notifications:
                    Fragment settings = getSupportFragmentManager().findFragmentByTag("settings");
                    if(settings == null){
                        settings = new SettingFragment();
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_contrainer,settings,"settings")
                            .commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销注册
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_contrainer, new IndexFragment(),"index")
                .commit();

        EventBus.getDefault().register(this);
        ClientId  = ClientId+System.currentTimeMillis();
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

    public void publishMessage(String msg){
        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(msg.getBytes());
            message.setQos(1);
            IMqttDeliveryToken token = mqttClient.publish(pushTopic, message);
        } catch (MqttException e) {
            System.err.println("Error Publishing: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventBus(MessageEvent event){
        if(event != null){
            String msg = JSON.toJSONString(event);
            publishMessage(msg);
        }
    }





}
