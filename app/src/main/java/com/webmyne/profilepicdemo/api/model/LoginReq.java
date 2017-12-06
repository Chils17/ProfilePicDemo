package com.webmyne.profilepicdemo.api.model;

/**
 * Created by chiragpatel on 05-12-2017.
 */

public class LoginReq {
    private String Email;
    private String FBID;
    private String FullName;
    private String GID;
    private String Mobile;
    private String ProfileUrl;
    private String ReferID;
    private int SocialType;
    private DeviceInfo DeviceInfo;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFBID() {
        return FBID;
    }

    public void setFBID(String FBID) {
        this.FBID = FBID;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getGID() {
        return GID;
    }

    public void setGID(String GID) {
        this.GID = GID;
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

    public String getReferID() {
        return ReferID;
    }

    public void setReferID(String referID) {
        ReferID = referID;
    }

    public int getSocialType() {
        return SocialType;
    }

    public void setSocialType(int socialType) {
        SocialType = socialType;
    }

    public DeviceInfo getDeviceInfo() {
        return DeviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.DeviceInfo = deviceInfo;
    }
}
