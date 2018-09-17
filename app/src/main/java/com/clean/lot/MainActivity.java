package com.clean.lot;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.alibaba.fastjson.JSON;
import com.clean.lot.entity.MessageEvent;
import com.clean.lot.fragment.ControlFragment;
import com.clean.lot.fragment.IndexFragment;
import com.clean.lot.fragment.SettingFragment;
import com.clean.lot.service.MqttService;
import com.clean.lot.tools.PermissionsChecker;
import com.clean.lot.util.StringUtil;

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
    private Fragment currentFragment;
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };

    private PermissionsChecker mPermissionsChecker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        currentFragment = new IndexFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_contrainer, currentFragment,"index")
                .commit();
        Intent intentMqttService = new Intent(this, MqttService.class);
        startService(intentMqttService);
    }


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
                    switchFragment(R.id.fragment_contrainer,"index",index);
                    return true;
                case R.id.navigation_dashboard:
                    Fragment control = getSupportFragmentManager().findFragmentByTag("control");
                    if(control == null){
                        control = new ControlFragment();
                    }
                    switchFragment(R.id.fragment_contrainer,"control",control);
                    return true;
                case R.id.navigation_notifications:
                    Fragment settings = getSupportFragmentManager().findFragmentByTag("settings");
                    if(settings == null){
                        settings = new SettingFragment();
                    }
                    switchFragment(R.id.fragment_contrainer,"settings",settings);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }





    private void switchFragment(int id,String targetName,Fragment targetFragment){
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if(targetFragment.isAdded()){
            transaction.hide(currentFragment).show(targetFragment);
        }else{
            transaction.hide(currentFragment);
            transaction.add(id,targetFragment,targetName);
        }
        currentFragment = targetFragment;
        transaction.commit();
    }


}
