package com.webmyne.profilepicdemo.helper;

import android.content.Context;
import com.webmyne.profilepicdemo.api.model.User;


/**
 * Created by xitij on 17-03-2015.
 */
public class PrefUtils {

    public static String Refer_ID = "Refer_ID";
    public static String USER_ID = "UserId";
    public static String USER_PROFILE_KEY = "USER_PROFILE_KEY";
    public static String LOGGED_IN = "LOGGED_IN";
    public static String ENABLE_NOTIFICATION = "ENABLE_NOTIFICATION";
    public static String FCM = "FCM";
    public static String SKIP = "SKIP";
    public static String NEWS_ID = "NEWS_ID";
    private static String REWARDS = "REWARDS";

    public static void setNotification(Context ctx, boolean value) {
        Prefs.with(ctx).save(ENABLE_NOTIFICATION, value);
    }

    public static boolean isNotification(Context ctx) {
        return Prefs.with(ctx).getBoolean(ENABLE_NOTIFICATION, false);
    }

    public static void setLoggedIn(Context ctx, boolean value) {
        Prefs.with(ctx).save(LOGGED_IN, value);
    }

    public static boolean isUserLoggedIn(Context ctx) {
        return Prefs.with(ctx).getBoolean(LOGGED_IN, false);
    }

    public static void setUserID(Context ctx, int value) {
        Prefs.with(ctx).save(USER_ID, value);
    }

    public static void setRewards(Context ctx, int value) {
        Prefs.with(ctx).save(REWARDS, value);
    }

    public static int getRewards(Context ctx) {
        return Prefs.with(ctx).getInt(REWARDS, 0);
    }

    public static void setReferID(Context ctx, String value) {
        Prefs.with(ctx).save(Refer_ID, value);
    }

    public static int getUserID(Context ctx) {
        return Prefs.with(ctx).getInt(USER_ID, 0);
    }

    public static String getReferID(Context ctx) {
        return Prefs.with(ctx).getString(Refer_ID, "0");
    }

    public static void setUserFullProfileDetails(Context context, User userProfile) {
        String toJson = MyApplication.getGson().toJson(userProfile);
        setUserID(context, userProfile.getUserID());
        setRewards(context, userProfile.getRewards());
        Prefs.with(context).save(USER_PROFILE_KEY, toJson);
    }

    public static User getUserProfileDetails(Context context) {
        User userProfileDetails = null;
        String getUserProfileString = Prefs.with(context).getString(USER_PROFILE_KEY, "");
        try {
            userProfileDetails = MyApplication.getGson().fromJson(getUserProfileString, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userProfileDetails;
    }

    public static void setFCMToken(Context context, String token) {
        Prefs.with(context).save(FCM, token);
    }

    public static String getFCMToken(Context context) {
        return Prefs.with(context).getString(FCM, "");
    }

    public static void setUserSkip(Context ctx, boolean value) {
        Prefs.with(ctx).save(SKIP, value);
    }

    public static boolean isUserSkip(Context ctx) {
        return Prefs.with(ctx).getBoolean(SKIP, false);
    }

    public static void saveNewsId(Context context, String newsId) {
        Prefs.with(context).save(NEWS_ID, newsId);
    }

    public static String getNewsId(Context context) {
        return Prefs.with(context).getString(NEWS_ID, "1");
    }
}
