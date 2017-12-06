package com.webmyne.profilepicdemo.ui;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.droidbyme.dialoglib.DroidDialog;
import com.webmyne.profilepicdemo.R;
import com.webmyne.profilepicdemo.api.model.User;
import com.webmyne.profilepicdemo.custom.TfTextView;
import com.webmyne.profilepicdemo.helper.PrefUtils;

public class MainActivity extends AppCompatActivity {

    private AppBarLayout appbar;
    private Toolbar toolbar;
    private TfTextView txtTitle;
    private ImageView imgProfile;
    private Context context;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        actionListener();
    }

    private void init() {
        context = this;
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        initToolbar();

        user = PrefUtils.getUserProfileDetails(context);

        if (user != null && user.getProfileUrl() != null && user.getProfileUrl().trim().length() > 0) {
            Glide.with(context)
                    .load(user.getProfileUrl())
                    .asBitmap()
                    .placeholder(R.drawable.user)
                    .into(new BitmapImageViewTarget(imgProfile) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            imgProfile.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        } else {
            Glide.with(context)
                    .load(R.drawable.logo)
                    .asBitmap()
                    .into(new BitmapImageViewTarget(imgProfile) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            imgProfile.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        txtTitle = (TfTextView) findViewById(R.id.txtTitle);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        txtTitle.setText("Profile Demo");
    }

    private void actionListener() {
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PrefUtils.isUserLoggedIn(context)) {
                    ActivityOptions options = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,
                                new Pair[]{Pair.create(imgProfile, "imgProfile")});
                        startActivity(new Intent(context, ProfileActivity.class), options.toBundle());
                    } else {
                        startActivity(new Intent(context, ProfileActivity.class));
                    }
                } else {
                    new DroidDialog.Builder(context)
                            .title(getString(R.string.login_required))
                            .content(getString(R.string.login_required_content))
                            .cancelable(true, true)
                            .positiveButton(getString(R.string.ok), new DroidDialog.onPositiveListener() {
                                @Override
                                public void onPositive(Dialog dialog) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .typeface("regular.ttf")
                            .negativeButton(getString(R.string.cancel), new DroidDialog.onNegativeListener() {
                                @Override
                                public void onNegative(Dialog dialog) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        user = PrefUtils.getUserProfileDetails(context);

        if (user != null && user.getProfileUrl() != null && user.getProfileUrl().trim().length() > 0) {
            Log.e("profile url", user.getProfileUrl());
            Glide.with(context)
                    .load(user.getProfileUrl())
                    .asBitmap()
                    .placeholder(R.drawable.user)
                    .into(new BitmapImageViewTarget(imgProfile) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            imgProfile.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        } else {
            Glide.with(context)
                    .load(R.drawable.logo)
                    .asBitmap()
                    .into(new BitmapImageViewTarget(imgProfile) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            imgProfile.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }
    }
}
