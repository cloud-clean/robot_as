package com.clean.lot.entity;

public class MessageEvent {
    private String pos;
    private String status;

    public MessageEvent(String pos,String status){
        this.pos = pos;
        this.status = status;
    }

    public MessageEvent(){}

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
