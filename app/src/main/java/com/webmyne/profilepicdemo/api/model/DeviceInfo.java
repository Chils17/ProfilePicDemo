package com.webmyne.profilepicdemo.api.model;

/**
 * Created by chiragpatel on 05-12-2017.
 */

public class DeviceInfo {
    private String DeviceID;
    private String DeviceModel;
    private String DeviceOS;
    private String DeviceToken;
    private int DeviceType;

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public String getDeviceModel() {
        return DeviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        DeviceModel = deviceModel;
    }

    public String getDeviceOS() {
        return DeviceOS;
    }

    public void setDeviceOS(String deviceOS) {
        DeviceOS = deviceOS;
    }

    public String getDeviceToken() {
        return DeviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        DeviceToken = deviceToken;
    }

    public int getDeviceType() {
        return DeviceType;
    }

    public void setDeviceType(int deviceType) {
        DeviceType = deviceType;
    }
}
