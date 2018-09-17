package com.clean.lot.entity;


import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public class WebResult implements Serializable {

    private int code;
    private String msg;
    private JSONObject result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JSONObject getResult() {
        return result;
    }

    public void setResult(JSONObject result) {
        this.result = result;
    }
}
