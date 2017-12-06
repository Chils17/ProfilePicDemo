package com.webmyne.profilepicdemo.api.model;

/**
 * Created by chiragpatel on 05-12-2017.
 */

public class User {
    private String Area;
    private String Email;
    private String FBID;
    private String FullName;
    private String GID;
    private boolean IsActive;
    private boolean IsDelete;
    private boolean IsNewUser;
    private String Mobile;
    private String ProfileUrl;
    private String ReferID;
    private int Rewards;
    private int SocialType;
    private int UserID;

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

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public boolean isDelete() {
        return IsDelete;
    }

    public void setDelete(boolean delete) {
        IsDelete = delete;
    }

    public boolean isNewUser() {
        return IsNewUser;
    }

    public void setNewUser(boolean newUser) {
        IsNewUser = newUser;
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

    public int getRewards() {
        return Rewards;
    }

    public void setRewards(int rewards) {
        Rewards = rewards;
    }

    public int getSocialType() {
        return SocialType;
    }

    public void setSocialType(int socialType) {
        SocialType = socialType;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }
}
