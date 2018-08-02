package com.clean.lot.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.clean.lot.R;
import com.clean.lot.entity.MessageEvent;
import com.clean.lot.util.HttpUtil;
import com.clean.lot.util.StringUtil;

import org.greenrobot.eventbus.EventBus;


public class ControlFragment extends Fragment {
    private ToggleButton lampBtn;
    private ImageView imageView;
    private Button testBtn;

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
        lampBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        SharedPreferences reader = getActivity().getSharedPreferences("status",getActivity().MODE_PRIVATE);
        String pos = reader.getString("position","").toString();
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
//                final Handler handler = new Handler(){
//                    @Override
//                    public void handleMessage(Message msg) {
//                        super.handleMessage(msg);
//                        Bundle data = msg.getData();
//                        String res = data.getString("res");
//                        JSONObject json = JSON.parseObject(res);
//                        if(json != null){
//                            if(json.getInteger("code") == 1){
//                                imageView.setImageResource(b?R.drawable.lamp_on:R.drawable.lamp_off);
//                            }
//                        }
//                    }
//                };

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String res;
//                        String pos = reader.getString("position","").toString();
//                        if(StringUtil.isEmpty(pos)){
//                            Toast.makeText(getActivity(),"位置不能为空,请到设置页面设置",Toast.LENGTH_SHORT).show();
//                            return;
//                        }else{
//                            System.out.println("pos:"+pos);
//                        }
//                        if(b){
//                            res = HttpUtil.okGet(String.format("http://202.182.118.148/api/switch?pos=%s&status=%s",pos,"1"));
//                        }else{
//                            res = HttpUtil.okGet(String.format("http://202.182.118.148/api/switch?pos=%s&status=%s",pos,"0"));
//                        }
//
//                        Message msg = new Message();
//                        Bundle bundle = new Bundle();
//                        bundle.putString("res",res);
//                        msg.setData(bundle);
//                        handler.sendMessage(msg);
//
//                    }
//                }).start();
            }
        });
    }

}
