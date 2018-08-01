package com.clean.lot;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.clean.lot.fragment.ControlFragment;
import com.clean.lot.fragment.IndexFragment;
import com.clean.lot.fragment.SettingFragment;

public class MainActivity extends AppCompatActivity {


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_contrainer, new IndexFragment(),"index")
                .commit();
    }



}
