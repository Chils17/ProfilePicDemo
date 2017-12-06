package com.webmyne.profilepicdemo.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.droidbyme.dialoglib.DroidDialog;
import com.gun0912.tedpermission.PermissionListener;
import com.mlsdev.rximagepicker.RxImageConverters;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;
import com.webmyne.profilepicdemo.R;
import com.webmyne.profilepicdemo.api.call.DoUpdateProfile;
import com.webmyne.profilepicdemo.api.call.GetProfile;
import com.webmyne.profilepicdemo.api.model.DeviceInfo;
import com.webmyne.profilepicdemo.api.model.UpdateProfile;
import com.webmyne.profilepicdemo.api.model.User;
import com.webmyne.profilepicdemo.custom.TfEditText;
import com.webmyne.profilepicdemo.custom.TfTextView;
import com.webmyne.profilepicdemo.helper.AppConstants;
import com.webmyne.profilepicdemo.helper.Functions;
import com.webmyne.profilepicdemo.helper.PrefUtils;
import com.webmyne.profilepicdemo.helper.ProgressBarHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

public class ProfileActivity extends AppCompatActivity {

    private TfEditText edtAddress;
    private TfEditText edtMobile;
    private TfEditText edtEmail;
    private TfEditText edtFullName;
    private ProgressBar progressBar;
    private ImageView imgProfile;
    private Toolbar toolbar;
    private TfTextView txtTitle;
    private ProgressBarHelper progress;
    private boolean IsProfileChange = false;
    private User user;
    private ProgressBar preloader;
    private FTPClient ftpClient;
    private String imagePath;
    private Bitmap rotatedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        actionListener();
    }

    private void init() {
        initToolbar();
        progress = new ProgressBarHelper(ProfileActivity.this, false);
        edtAddress = (TfEditText) findViewById(R.id.edtAddress);
        edtMobile = (TfEditText) findViewById(R.id.edtMobile);
        edtEmail = (TfEditText) findViewById(R.id.edtEmail);
        edtFullName = (TfEditText) findViewById(R.id.edtFullName);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);

        if (Functions.isConnected(ProfileActivity.this)) {
            callApi();
        } else {
            setUi();
        }

        ftpClient = new FTPClient();
    }

    private void callApi() {
        new GetProfile(ProfileActivity.this, new GetProfile.OnResponse() {
            @Override
            public void onSuccess(User data) {
                if (data != null) {
                    PrefUtils.setUserFullProfileDetails(ProfileActivity.this, data);
                    setUi();
                } else {
                    setUi();
                }
            }

            @Override
            public void onFail(String responseMsg) {
                Toast.makeText(ProfileActivity.this, responseMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUi() {
        user = PrefUtils.getUserProfileDetails(ProfileActivity.this);

        preloader = (ProgressBar) findViewById(R.id.progressBar);
        if (user != null) {
            edtFullName.setText(Functions.getNotNullableString(user.getFullName()));
            edtEmail.setText(Functions.getNotNullableString(user.getEmail()));
            edtMobile.setText(Functions.getNotNullableString(user.getMobile()));
            edtAddress.setText(Functions.getNotNullableString(user.getArea()));

            if (user.getProfileUrl() != null && user.getProfileUrl().trim().length() > 0) {
                if (IsProfileChange) {
                    preloader.setVisibility(View.VISIBLE);
                    IsProfileChange = false;
                }

                Glide.with(getApplicationContext())
                        .load(user.getProfileUrl())
                        .asBitmap()
                        .thumbnail(0.5f)
                        .listener(new RequestListener<String, Bitmap>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                preloader.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                preloader.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(new BitmapImageViewTarget(imgProfile) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(ProfileActivity.this.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                imgProfile.setImageDrawable(circularBitmapDrawable);
                            }
                        });

            } else {
                if (IsProfileChange) {
                    preloader.setVisibility(View.VISIBLE);
                    IsProfileChange = false;
                }
                Glide.with(getApplicationContext())
                        .load(R.drawable.logo)
                        .asBitmap()
                        .listener(new RequestListener<Integer, Bitmap>() {
                            @Override
                            public boolean onException(Exception e, Integer model, Target<Bitmap> target, boolean isFirstResource) {
                                preloader.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Integer model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                preloader.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(new BitmapImageViewTarget(imgProfile) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(ProfileActivity.this.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                imgProfile.setImageDrawable(circularBitmapDrawable);
                            }
                        });

            }
        } else {
            new DroidDialog.Builder(ProfileActivity.this)
                    .title(getString(R.string.login_required))
                    .content(getString(R.string.login_required_content))
                    .cancelable(true, true)
                    .positiveButton(getString(R.string.ok), new DroidDialog.onPositiveListener() {
                        @Override
                        public void onPositive(Dialog dialog) {
                            dialog.dismiss();
                            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
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

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        txtTitle = (TfTextView) findViewById(R.id.txtTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Functions.hideKeyPad(ProfileActivity.this, findViewById(android.R.id.content));
        switch (item.getItemId()) {
            case R.id.menuLogout:
                new DroidDialog.Builder(ProfileActivity.this)
                        .title(getString(R.string.logout))
                        .content(getString(R.string.logout_content))
                        .cancelable(true, true)
                        .positiveButton(getString(R.string.yes), new DroidDialog.onPositiveListener() {
                            @Override
                            public void onPositive(Dialog dialog) {
                                dialog.dismiss();
                                Functions.logout(ProfileActivity.this);
                            }
                        })
                        .typeface("regular.ttf")
                        .negativeButton(getString(R.string.cancel), new DroidDialog.onNegativeListener() {
                            @Override
                            public void onNegative(Dialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();
                break;

            case R.id.menuUpdate:
                if (Functions.isConnected(ProfileActivity.this)) {

                    if (Functions.toStr(edtFullName).trim().length() > 0) {

                        if (Functions.toStr(edtMobile).trim().length() > 0 && Functions.toStr(edtMobile).trim().length() != 10) {
                            Toast.makeText(ProfileActivity.this, getString(R.string.enter_mobile_number), Toast.LENGTH_SHORT).show();
                        } else {
                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();
                                    progress.showProgressDialog();
                                }

                                @Override
                                protected Void doInBackground(Void... voids) {
                                    if (IsProfileChange) {
                                        uploadFile();
                                    }
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    super.onPostExecute(aVoid);
                                    progress.hideProgressDialog();
                                    updateProfile();
                                }
                            }.execute();
                        }
                    } else {
                        Toast.makeText(ProfileActivity.this, getString(R.string.enter_fullname), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, getString(R.string.check_internet), Toast.LENGTH_SHORT).show();

                }

                break;
            case R.id.menuRefresh:
                callApi();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void updateProfile() {
        UpdateProfile userUpdateProfile = new UpdateProfile();
        userUpdateProfile.setFullName(Functions.toStr(edtFullName));
        userUpdateProfile.setArea(Functions.toStr(edtAddress));
        userUpdateProfile.setMobile(Functions.toStr(edtMobile));
        userUpdateProfile.setUserID(PrefUtils.getUserID(ProfileActivity.this));
        if (IsProfileChange) {
            userUpdateProfile.setProfileUrl(new File(imagePath).getName());
        } else {
            userUpdateProfile.setProfileUrl("");
        }
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceID(Functions.getDeviceId(ProfileActivity.this));
        deviceInfo.setDeviceModel(Functions.getDeviceName());
        deviceInfo.setDeviceOS(android.os.Build.VERSION.RELEASE);
        deviceInfo.setDeviceType(1);

        userUpdateProfile.setDeviceInfo(deviceInfo);

        new DoUpdateProfile(ProfileActivity.this, userUpdateProfile, new DoUpdateProfile.OnResponse() {
            @Override
            public void onSuccess(User data) {
                if (data != null) {
                    PrefUtils.setUserFullProfileDetails(ProfileActivity.this, data);
                    setUi();
                    Toast.makeText(ProfileActivity.this, getString(R.string.profile_update), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail(String responseMsg) {
                Toast.makeText(ProfileActivity.this, responseMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actionListener() {
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.setPermission(ProfileActivity.this, new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        new DroidDialog.Builder(ProfileActivity.this)
                                .content("")
                                .cancelable(true, true)
                                .title("Choose Media")
                                .positiveButton("Camera", new DroidDialog.onPositiveListener() {
                                    @Override
                                    public void onPositive(Dialog droidDialog) {
                                        droidDialog.dismiss();
                                        captureImage();
                                    }
                                })
                                .negativeButton("Gallery", new DroidDialog.onNegativeListener() {
                                    @Override
                                    public void onNegative(Dialog droidDialog) {
                                        droidDialog.dismiss();
                                        fromGallery();
                                    }
                                })
                                .typeface("regular.ttf")
                                .show();
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Toast.makeText(ProfileActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public String compressImage(String filePath) {

        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File(filePath));

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 60, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return (new File(filePath).length() / 1024) + "";

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    private void captureImage() {
        RxImagePicker.with(ProfileActivity.this).requestImage(Sources.CAMERA)
                .flatMap(new Function<Uri, ObservableSource<File>>() {
                    @Override
                    public ObservableSource<File> apply(@NonNull Uri uri) throws Exception {
                        return RxImageConverters.uriToFile(ProfileActivity.this, uri, createTempFile());
                    }
                }).subscribe(new Consumer<File>() {
            @Override
            public void accept(@NonNull final File file) throws Exception {
                // Do something with your file copy
                if ((file.length() / 1024) > 5120) {
                    Toast.makeText(ProfileActivity.this, "Image size should be less than 5 MB", Toast.LENGTH_SHORT).show();

                } else {
                    Glide.with(getApplicationContext())
                            .load(file)
                            .asBitmap()
                            .listener(new RequestListener<File, Bitmap>() {
                                @Override
                                public boolean onException(Exception e, File model, Target<Bitmap> target, boolean isFirstResource) {
                                    preloader.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, File model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    preloader.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(new BitmapImageViewTarget(imgProfile) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    try {
                                        ExifInterface ei = new ExifInterface(file.getAbsolutePath());
                                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                                ExifInterface.ORIENTATION_UNDEFINED);

                                        switch (orientation) {

                                            case ExifInterface.ORIENTATION_ROTATE_90:
                                                rotatedBitmap = rotateImage(resource, 90);
                                                break;

                                            case ExifInterface.ORIENTATION_ROTATE_180:
                                                rotatedBitmap = rotateImage(resource, 180);
                                                break;

                                            case ExifInterface.ORIENTATION_ROTATE_270:
                                                rotatedBitmap = rotateImage(resource, 270);
                                                break;

                                            case ExifInterface.ORIENTATION_NORMAL:
                                            default:
                                                rotatedBitmap = resource;
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Log.e("file size", file.length() + " " + compressImage(file.getAbsolutePath()));

                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(ProfileActivity.this.getResources(), rotatedBitmap);
                                    circularBitmapDrawable.setCircular(true);
                                    imgProfile.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                    IsProfileChange = true;
                }
            }
        });
    }

    private Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private File createTempFile() {
        File file = new File(ProfileActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/Elections/Profile", PrefUtils.getUserID(ProfileActivity.this) + "_" + System.currentTimeMillis() + ".png");
        if (!file.exists()) {
            file.mkdirs();
        }
        imagePath = ProfileActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/Elections/Profile" + PrefUtils.getUserID(ProfileActivity.this) + "_" + System.currentTimeMillis() + ".png";
        return new File(imagePath);
    }

    private void fromGallery() {
        RxImagePicker.with(ProfileActivity.this).requestImage(Sources.GALLERY)
                .flatMap(new Function<Uri, ObservableSource<File>>() {
                    @Override
                    public ObservableSource<File> apply(@NonNull Uri uri) throws Exception {
                        return RxImageConverters.uriToFile(ProfileActivity.this, uri, createTempFile());
                    }
                }).subscribe(new Consumer<File>() {
            @Override
            public void accept(@NonNull final File file) throws Exception {
                // Do something with your file copy
                if ((file.length() / 1024) > 5120) {
                    Toast.makeText(ProfileActivity.this, "Image size should be less than 5 MB", Toast.LENGTH_SHORT).show();

                } else {
                    Glide.with(ProfileActivity.this)
                            .load(file)
                            .asBitmap()
                            .listener(new RequestListener<File, Bitmap>() {
                                @Override
                                public boolean onException(Exception e, File model, Target<Bitmap> target, boolean isFirstResource) {
                                    preloader.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, File model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    preloader.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(new BitmapImageViewTarget(imgProfile) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    Log.e("file size", (file.length() / 1024) + " " + compressImage(file.getAbsolutePath()));
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(ProfileActivity.this.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    imgProfile.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                    IsProfileChange = true;
                }
            }
        });
    }

    private void uploadFile() {
        try {
            if (!ftpClient.isConnected()) {
                ftpClient.connect(AppConstants.HOST_NAME, AppConstants.PORT_NUM);
                ftpClient.login(AppConstants.FTP_USERNAME, AppConstants.FTP_PASSWORD);
                ftpClient.setType(FTPClient.TYPE_BINARY);
//                ftpClient.changeDirectory(AppConstants.FTP_UPLOAD_DIRECTORY);
            }


            try {
//                Log.e("imagePath", imagePath);
                ftpClient.upload(new File(imagePath), new FTPDataTransferListener() {
                    @Override
                    public void started() {

                    }

                    @Override
                    public void transferred(int i) {

//                        Log.e("transfer", "" + i);
                    }

                    @Override
                    public void completed() {

//                        Log.e("complete", "complete");

                    }

                    @Override
                    public void aborted() {

//                        Log.e("abort", "abort");
                    }

                    @Override
                    public void failed() {

//                        Log.e("fail", "fail");
                    }
                });
            } catch (FTPDataTransferException e) {
//                Log.e("error1", e.toString());
                e.printStackTrace();
            } catch (FTPAbortedException e) {
//                Log.e("error2", e.toString());
                e.printStackTrace();
            }
        } catch (IOException e) {
//            Log.e("error3", e.toString());
            e.printStackTrace();
        } catch (FTPIllegalReplyException e) {
//            Log.e("error4", e.toString());
            e.printStackTrace();
        } catch (FTPException e) {
//            Log.e("error5", e.toString());
            e.printStackTrace();
        }

    }
}
