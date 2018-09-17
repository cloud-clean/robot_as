package com.clean.lot.handler.viewHandler;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.clean.lot.R;
import com.clean.lot.handler.ViewHandler;

public class LampViewHandler extends ViewHandler {

    public LampViewHandler(View view){
        this.view = view;
    }

    @Override
    public void handler() {
        if(data.getCode() == 1){
            JSONObject json = data.getResult().getJSONObject("data");
            String pos = json.getString("pos");
            String status = json.getString("status");
            ImageView imageView = (ImageView) view;
            if("1".equals(status)){
                imageView.setImageResource(R.drawable.lamp_on);
            }else{
                imageView.setImageResource(R.drawable.lamp_off);
            }
        }else{
            Log.d("lamp",data.getMsg());
        }
    }

}
