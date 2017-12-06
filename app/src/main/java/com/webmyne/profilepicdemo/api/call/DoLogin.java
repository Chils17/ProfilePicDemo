package com.webmyne.profilepicdemo.api.call;


import android.content.Context;
import android.util.Log;

import com.webmyne.profilepicdemo.R;
import com.webmyne.profilepicdemo.api.AppApi;
import com.webmyne.profilepicdemo.api.model.LoginReq;
import com.webmyne.profilepicdemo.api.model.LoginRes;
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

public class DoLogin {
    private ProgressBarHelper progress;
    private OnResponse onResponse;
    private Context context;
    private AppApi appApi;

    public DoLogin(Context context, LoginReq loginReq, OnResponse onResponse) {
        this.context = context;
        this.onResponse = onResponse;
        progress = new ProgressBarHelper(context, false);
        appApi = MyApplication.getRetrofit().create(AppApi.class);
        callApi(loginReq);
    }

    private void callApi(LoginReq loginReq) {
        Log.e("login_req", MyApplication.getGson().toJson(loginReq));
        progress.showProgressDialog();
        appApi.doLogin(loginReq).enqueue(new Callback<LoginRes>() {
            @Override
            public void onResponse(Call<LoginRes> call, Response<LoginRes> response) {
                progress.hideProgressDialog();
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("login_res", MyApplication.getGson().toJson(response.body()));
                    com.webmyne.profilepicdemo.api.model.Response baseRes = response.body();
                    if (baseRes != null && baseRes.getResponseCode() == AppConstants.IS_SUCCESS) {
                        if (response.body().getResponseData().getData() != null && response.body().getResponseData().getData().size() > 0) {
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
                progress.hideProgressDialog();
                onResponse.onFail(context.getResources().getString(R.string.try_again));
            }
        });
    }

    public interface OnResponse {
        void onSuccess(User data);

        void onFail(String responseMsg);
    }
}
