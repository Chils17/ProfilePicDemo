package com.webmyne.profilepicdemo.api.call;

import android.content.Context;
import android.util.Log;

import com.webmyne.profilepicdemo.R;
import com.webmyne.profilepicdemo.api.AppApi;
import com.webmyne.profilepicdemo.api.model.LoginRes;
import com.webmyne.profilepicdemo.api.model.UpdateProfile;
import com.webmyne.profilepicdemo.api.model.User;
import com.webmyne.profilepicdemo.helper.AppConstants;
import com.webmyne.profilepicdemo.helper.MyApplication;
import com.webmyne.profilepicdemo.helper.ProgressBarHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by chiragpatel on 05-12-2017.
 */

public class DoUpdateProfile {
    private final ProgressBarHelper progressBar;
    private OnResponse onResponse;
    private Context context;

    public DoUpdateProfile(Context context, UpdateProfile updateProfile, OnResponse onResponse) {
        this.context = context;
        this.onResponse = onResponse;
        progressBar=new ProgressBarHelper(context,false);
        callApi(updateProfile);
    }

    private void callApi(UpdateProfile updateProfile) {
        Log.e("update profile req", MyApplication.getGson().toJson(updateProfile));
        progressBar.showProgressDialog();
        AppApi api = MyApplication.getRetrofit().create(AppApi.class);
        api.doUpdateProfile(updateProfile).enqueue(new Callback<LoginRes>() {
            @Override
            public void onResponse(Call<LoginRes> call, Response<LoginRes> response) {
                progressBar.hideProgressDialog();
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("update profile res", MyApplication.getGson().toJson(response.body()));
                    if (response.body().getResponseCode() == AppConstants.IS_SUCCESS) {
                        if (response.body().getResponseData().getData() != null && response.body().getResponseData().getData().size()>0) {
                            onResponse.onSuccess(response.body().getResponseData().getData().get(0));
                        } else {
                            onResponse.onFail(response.body().getResponseMsg());
                        }
                    } else {
                        onResponse.onFail(response.body().getResponseMsg());
                    }
                } else {
                    onResponse.onFail(context.getResources().getString(R.string.try_again));
                }
            }

            @Override
            public void onFailure(Call<LoginRes> call, Throwable t) {
                progressBar.hideProgressDialog();
                onResponse.onFail(context.getResources().getString(R.string.try_again));
            }
        });
    }

    public interface OnResponse {
        void onSuccess(User data);

        void onFail(String responseMsg);
    }
}
