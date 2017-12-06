package com.webmyne.profilepicdemo.api.call;

import android.content.Context;

import com.webmyne.profilepicdemo.R;
import com.webmyne.profilepicdemo.api.AppApi;
import com.webmyne.profilepicdemo.api.model.LoginRes;
import com.webmyne.profilepicdemo.api.model.User;
import com.webmyne.profilepicdemo.helper.AppConstants;
import com.webmyne.profilepicdemo.helper.MyApplication;
import com.webmyne.profilepicdemo.helper.PrefUtils;
import com.webmyne.profilepicdemo.helper.ProgressBarHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by chiragpatel on 05-12-2017.
 */

public class GetProfile {
    private ProgressBarHelper progress;
    private OnResponse onResponse;
    private Context context;

    public GetProfile(Context context, OnResponse onResponse) {
        this.context = context;
        this.onResponse = onResponse;
        progress = new ProgressBarHelper(context, false);
        callApi(PrefUtils.getUserID(context));
    }

    private void callApi(int userId) {
//        Log.e("profilr req", userId + "");
        progress.showProgressDialog();
        AppApi api = MyApplication.getRetrofit().create(AppApi.class);
        api.getProfile(userId).enqueue(new Callback<LoginRes>() {
            @Override
            public void onResponse(Call<LoginRes> call, Response<LoginRes> response) {
                progress.hideProgressDialog();
                if (response.isSuccessful() && response.body() != null) {
//                    Log.e("profile res", MyApplication.getGson().toJson(response.body()));
                    if (response.body().getResponseCode() == AppConstants.IS_SUCCESS) {
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
