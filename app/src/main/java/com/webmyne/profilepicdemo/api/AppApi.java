package com.webmyne.profilepicdemo.api;



import com.webmyne.profilepicdemo.api.model.LoginReq;
import com.webmyne.profilepicdemo.api.model.LoginRes;
import com.webmyne.profilepicdemo.api.model.UpdateProfile;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by sagartahelyani on 27-10-2017.
 */

public interface AppApi {

    @POST(ApiConstants.LOGIN)
    Call<LoginRes> doLogin(@Body LoginReq loginReq);

    @GET(ApiConstants.PROFILE)
    Call<LoginRes> getProfile(@Path("USERID") int userId);

    @POST(ApiConstants.UPDATE_PROFILE)
    Call<LoginRes> doUpdateProfile(@Body UpdateProfile updateProfile);

}
