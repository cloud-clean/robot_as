package com.clean.lot.handler;

import android.view.View;

import com.clean.lot.entity.WebResult;

public abstract class ViewHandler {
    protected View view;
    protected WebResult data;
    public abstract void handler();

    public WebResult getData() {
        return data;
    }

    public void setData(WebResult data) {
        this.data = data;
    }
}
