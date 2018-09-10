package com.clean.lot.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.clean.lot.R;
import com.clean.lot.util.StringUtil;

public class SettingFragment extends Fragment {

    private Button saveBtn;
    private EditText posEt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        saveBtn = getActivity().findViewById(R.id.setting_save_btn);
        posEt = getActivity().findViewById(R.id.setting_pos);
        SharedPreferences reader = getActivity().getSharedPreferences("settings",getActivity().MODE_PRIVATE);
        String mqurl = reader.getString("mqtt_server","").toString();
        posEt.setText(mqurl);



        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mqurl = posEt.getText().toString();
               if(StringUtil.isEmpty(mqurl)){
                   Toast.makeText(getActivity(),"服务器地址不能为空",Toast.LENGTH_SHORT).show();
                   return;
               }
               SharedPreferences.Editor editor = getActivity().getSharedPreferences("settings",getActivity().MODE_PRIVATE).edit();
               editor.putString("mqtt_server",mqurl);
               editor.commit();
                Toast.makeText(getActivity(),"保存成功",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
