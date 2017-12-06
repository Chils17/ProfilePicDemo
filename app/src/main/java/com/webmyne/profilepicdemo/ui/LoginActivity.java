package com.webmyne.profilepicdemo.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.webmyne.profilepicdemo.R;
import com.webmyne.profilepicdemo.api.call.DoLogin;
import com.webmyne.profilepicdemo.api.model.DeviceInfo;
import com.webmyne.profilepicdemo.api.model.LoginReq;
import com.webmyne.profilepicdemo.api.model.User;
import com.webmyne.profilepicdemo.helper.AppConstants;
import com.webmyne.profilepicdemo.helper.Functions;
import com.webmyne.profilepicdemo.helper.MyApplication;
import com.webmyne.profilepicdemo.helper.PrefUtils;
import com.webmyne.profilepicdemo.helper.ProgressBarHelper;

public class LoginActivity extends AppCompatActivity {

    private Context context;
    private LinearLayout llGoogleLogin;
    private LinearLayout llFacebookLogin;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleApiClient mGoogleApiClient;
    private LoginManager loginManager;
    private ProgressBarHelper progressBar;
    private CallbackManager callbackManager;
    private static final int RESPONSE_CODE_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        actionListener();
    }

    private void init() {
        context = this;
        progressBar = new ProgressBarHelper(context, false);
        callbackManager = CallbackManager.Factory.create();
        llGoogleLogin = (LinearLayout) findViewById(R.id.llGoogleLogin);
        llFacebookLogin = (LinearLayout) findViewById(R.id.llFacebookLogin);
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(context).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();
        //   loginManager = LoginManager.getInstance();

        /*if (Functions.isConnected(context)) {
            if (PrefUtils.isUserLoggedIn(context)) {
                Functions.fireIntent(context, MainActivity.class);
            }
            finish();
        } else {
            Toast.makeText(context, getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
        }*/

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    private void actionListener() {
        llGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Functions.isConnected(context)) {
                    progressBar.showProgressDialog();
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, RESPONSE_CODE_SIGN_IN);
                } else {
                    Toast.makeText(context, getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESPONSE_CODE_SIGN_IN) {
            progressBar.hideProgressDialog();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }

    private void handleResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            assert account != null;
            String name = account.getDisplayName();
            String email = account.getEmail();

            LoginReq loginReq = new LoginReq();
            loginReq.setFullName(name);
            loginReq.setEmail(email);
            loginReq.setReferID(PrefUtils.getReferID(LoginActivity.this));
            loginReq.setSocialType(AppConstants.GOOGLE);
            loginReq.setGID(account.getId());
            if (account.getPhotoUrl() != null) {
                loginReq.setProfileUrl(account.getPhotoUrl().toString());
            }

            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.setDeviceID(Functions.getDeviceId(context));
            deviceInfo.setDeviceModel(Functions.getDeviceName());
            deviceInfo.setDeviceOS(android.os.Build.VERSION.RELEASE);
            deviceInfo.setDeviceType(AppConstants.ANDROID);

            loginReq.setDeviceInfo(deviceInfo);

            new DoLogin(context, loginReq, new DoLogin.OnResponse() {
                @Override
                public void onSuccess(User data) {
                    googleSignOut();
                    PrefUtils.setUserFullProfileDetails(context, data);
                    PrefUtils.setUserSkip(context, false);
                    PrefUtils.setLoggedIn(context, true);
                    PrefUtils.setReferID(LoginActivity.this, "");
                    startActivity(new Intent(context, MainActivity.class));
                    finish();
                }

                @Override
                public void onFail(String responseMsg) {
                    Toast.makeText(context, responseMsg, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, "Login Cancel", Toast.LENGTH_SHORT).show();
        }
    }

    private void googleSignOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        // ...
                    }
                });
    }
}
