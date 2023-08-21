package com.video.music.downloader.Activity;

import static com.video.music.downloader.statusandgallery.utils.Utils.ShareApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.video.music.downloader.AdsUtils.FirebaseADHandlers.AdUtils;
import com.video.music.downloader.AdsUtils.Interfaces.AppInterfaces;
import com.video.music.downloader.AdsUtils.Utils.Constants;
import com.video.music.downloader.BuildConfig;
import com.video.music.downloader.R;
import com.video.music.downloader.VideoDownloader.DownloadsActivity;
import com.video.music.downloader.databinding.ActivitySettingsBinding;
import com.video.music.downloader.statusandgallery.activity.WhatsAppStatusSaverActivity;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;
    private SettingsActivity activity;
    String activityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        AdUtils.showNativeAd(SettingsActivity.this, Constants.adsJsonPOJO.getParameters().getNative_id().getDefaultValue().getValue(), findViewById(R.id.native_ads), true);

        activityName = getIntent().getStringExtra("activity");

        binding.downloadsSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdUtils.showInterstitialAd(SettingsActivity.this, new AppInterfaces.InterStitialADInterface() {
                    @Override
                    public void adLoadState(boolean isLoaded) {
                        String[] arrPermissionsEdit = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                        if (Build.VERSION.SDK_INT >= 29)
                            arrPermissionsEdit = new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
                        Dexter.withContext(SettingsActivity.this).withPermissions(arrPermissionsEdit).withListener(new MultiplePermissionsListener() {
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                    startActivity(new Intent(getApplicationContext(), DownloadsActivity.class));
                                }

                                if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                                    DetailsDialog.showDetailsDialog(SettingsActivity.this);
//                            Toast.makeText(MainActivity.this, "Please Provide Permission", Toast.LENGTH_SHORT).show();
                                }
                            }


                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }

                        }).withErrorListener(dexterError -> Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show()).onSameThread().check();

//                startActivity(new Intent(DashboardActivity.this, MainActivity.class));
                    }
                });
            }
        });

        binding.shareAppSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareApp();
//                ShareApp(SettingsActivity.this);
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (activityName=="dash"){
//                    startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
//                } else {
                onBackPressed();
//                }

            }
        });

        binding.ppSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://firebasemedia.blogspot.com/p/privacy-policy.html");

            }
        });

        binding.rateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateApp();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AdUtils.showInterstitialAd(SettingsActivity.this, new AppInterfaces.InterStitialADInterface() {
            @Override
            public void adLoadState(boolean isLoaded) {
                SettingsActivity.super.onBackPressed();
            }
        });
    }

    public void shareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
            String shareMessage = "Are you tired of losing your favorite videos and statuses on social media? Download our video downloader app today! \n" +
                    "With features such as Status Downloader, Status Saver, Video Player and Music Player, you'll be able to save and enjoy all your favorite content in one place. \n" +
                    "Don't miss out on this must-have app - Download it now!\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Share Via"));
        } catch (Exception e) {
            //e.toString();
        }
    }


    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    public void rateApp() {
        try {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }

}