package com.webmyne.profilepicdemo.api;

/**
 * Created by sagartahelyani on 27-10-2017.
 */

public class ApiConstants {

//            public static final String BASE_URL = "http://209.126.235.28:141/Services/";
    public static final String BASE_URL = "http://ws-srv-net.in.webmyne.com/Applications/Election2017/Services/";

    // User.svc
    public static final String LOGIN = "User.svc/json/Login";
    public static final String PROFILE = "User.svc/json/GetProfile/{USERID}";
    public static final String UPDATE_PROFILE = "User.svc/json/UpdateProfile";
}