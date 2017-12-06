package com.webmyne.profilepicdemo.api.model;

/**
 * Created by chiragpatel on 05-12-2017.
 */

public class Response {
    private int ResponseCode;
    private String ResponseMsg;

    public int getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(int responseCode) {
        ResponseCode = responseCode;
    }

    public String getResponseMsg() {
        return ResponseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        ResponseMsg = responseMsg;
    }
}
