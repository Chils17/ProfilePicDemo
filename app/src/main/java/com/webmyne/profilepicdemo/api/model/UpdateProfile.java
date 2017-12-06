package com.webmyne.profilepicdemo.api.model;

/**
 * Created by chiragpatel on 05-12-2017.
 */

public class UpdateProfile {
    private String Area;
    private String Email;
    private String FullName;
    private String Mobile;
    private String ProfileUrl;
    private int UserID;
    private DeviceInfo DeviceInfo;

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getProfileUrl() {
        return ProfileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        ProfileUrl = profileUrl;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public com.webmyne.profilepicdemo.api.model.DeviceInfo getDeviceInfo() {
        return DeviceInfo;
    }

    public void setDeviceInfo(com.webmyne.profilepicdemo.api.model.DeviceInfo deviceInfo) {
        DeviceInfo = deviceInfo;
    }
}
