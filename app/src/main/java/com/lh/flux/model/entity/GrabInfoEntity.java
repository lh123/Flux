package com.lh.flux.model.entity;

public class GrabInfoEntity {
    private String returnCode;
    private String msg;
    private WelfareEnvelopEntity data;

    public String getReturnCode() {
        return returnCode;
    }

    @SuppressWarnings("unused")
    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public WelfareEnvelopEntity getData() {
        return data;
    }

    public void setData(WelfareEnvelopEntity data) {
        this.data = data;
    }
}
