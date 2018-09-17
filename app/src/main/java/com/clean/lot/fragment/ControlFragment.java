package com.clean.lot.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import com.clean.lot.R;
import com.clean.lot.entity.MessageEvent;
import com.clean.lot.entity.event.LampEvent;
import com.clean.lot.util.HttpUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class ControlFragment extends Fragment {
    private ToggleButton lampBtn;
    private ImageView imageView;
    private RadioGroup radioGroup;
    private String pos = "lot1";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.control_fragment,container,false);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lampBtn = getActivity().findViewById(R.id.lamp_btn);
        imageView = getActivity().findViewById(R.id.lamp);
        radioGroup = getActivity().findViewById(R.id.radis_pos);

        lampBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        MessageEvent msg = null;
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {
                if(b){
                    msg = new MessageEvent(pos,"1");
                }else {
                    msg = new MessageEvent(pos,"0");
                }
                EventBus.getDefault().post(msg);
                imageView.setImageResource(b?R.drawable.lamp_on:R.drawable.lamp_off);
            }
        });

        radioGroup = getActivity().findViewById(R.id.radis_pos);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton choise = getActivity().findViewById(id);
                pos = choise.getText().toString().trim();
//                String url = "http://lot.btprice.com/api/lot?pos="+pos;
                String url = "http://202.182.118.148/api/lot?pos="+pos;
                Log.d("lamp",url);
                HttpUtil.okGet(url);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void subscribe(LampEvent event){
        if("1".equals(event.getStatus())){
            imageView.setImageResource(R.drawable.lamp_on);
            lampBtn.setChecked(true);
        }else {
            imageView.setImageResource(R.drawable.lamp_off);
            lampBtn.setChecked(false);
        }
    }

}
