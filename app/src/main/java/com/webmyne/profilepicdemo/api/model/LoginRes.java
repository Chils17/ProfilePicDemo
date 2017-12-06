package com.webmyne.profilepicdemo.api.model;

/**
 * Created by chiragpatel on 05-12-2017.
 */

public class LoginRes extends Response {
    private LoginResData ResponseData;

    public LoginResData getResponseData() {
        return ResponseData;
    }

    public void setResponseData(LoginResData responseData) {
        ResponseData = responseData;
    }
}
