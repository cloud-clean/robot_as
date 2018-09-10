package com.clean.lot.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import org.greenrobot.eventbus.EventBus;


public class ControlFragment extends Fragment {
    private ToggleButton lampBtn;
    private ImageView imageView;
    private RadioGroup radioGroup;
    private String pos = "lot1";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.control_fragment,container,false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lampBtn = getActivity().findViewById(R.id.lamp_btn);
        imageView = getActivity().findViewById(R.id.lamp);
        radioGroup = getActivity().findViewById(R.id.radis_pos);
        lampBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//        SharedPreferences reader = getActivity().getSharedPreferences("status",getActivity().MODE_PRIVATE);
//        String pos = reader.getString("position","").toString();
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
            }
        });
    }

}
