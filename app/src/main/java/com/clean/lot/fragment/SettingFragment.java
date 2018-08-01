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
    private EditText statusEt;

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
        statusEt = getActivity().findViewById(R.id.setting_status);
        SharedPreferences reader = getActivity().getSharedPreferences("status",getActivity().MODE_PRIVATE);
        String pos = reader.getString("position","").toString();
        String status = reader.getString("status","").toString();
        posEt.setText(pos);
        statusEt.setText(status);



        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pos = posEt.getText().toString();
               if(StringUtil.isEmpty(pos)){
                   Toast.makeText(getActivity(),"位置不能为空",Toast.LENGTH_SHORT).show();
                   return;
               }
               String status = statusEt.getText().toString();
               if(StringUtil.isEmpty(status)){
                   Toast.makeText(getActivity(),"状态不能为空",Toast.LENGTH_SHORT).show();
                   return;
               }
               if(!"1".equals(status) && !"0".equals(status)){
                   Toast.makeText(getActivity(),"状态只能是0和1",Toast.LENGTH_SHORT).show();
                   return;
               }
               SharedPreferences.Editor editor = getActivity().getSharedPreferences("status",getActivity().MODE_PRIVATE).edit();
               editor.putString("status",status);
               editor.putString("position",pos);
               editor.commit();
                Toast.makeText(getActivity(),"保存成功",Toast.LENGTH_SHORT).show();
            }
        });
    }


}