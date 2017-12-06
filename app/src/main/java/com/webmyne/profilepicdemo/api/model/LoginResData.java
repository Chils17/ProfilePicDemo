package com.webmyne.profilepicdemo.api.model;

import java.util.List;

/**
 * Created by chiragpatel on 05-12-2017.
 */

public class LoginResData {
    private List<User> Data;

    public List<User> getData() {
        return Data;
    }

    public void setData(List<User> data) {
        Data = data;
    }
}
