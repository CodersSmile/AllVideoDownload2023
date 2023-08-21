package com.video.music.downloader.Activity;


import static android.icu.util.ULocale.getCountry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anchorfree.partner.api.response.AvailableCountries;
import com.anchorfree.partner.api.ClientInfo;
import com.anchorfree.partner.api.data.Country;
import com.anchorfree.partner.api.response.AvailableCountries;
import com.anchorfree.reporting.TrackingConstants;
import com.anchorfree.sdk.SessionConfig;
import com.anchorfree.sdk.UnifiedSDK;
import com.anchorfree.sdk.rules.TrafficRule;
import com.anchorfree.vpnsdk.callbacks.Callback;
import com.anchorfree.vpnsdk.exceptions.VpnException;
import com.anchorfree.vpnsdk.transporthydra.HydraTransport;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.northghost.caketube.OpenVpnTransportConfig;
import com.video.music.downloader.AdsUtils.FirebaseADHandlers.AdUtils;
import com.video.music.downloader.AdsUtils.FirebaseADHandlers.AdsJsonPOJO;
import com.video.music.downloader.AdsUtils.FirebaseADHandlers.FirebaseUtils;
import com.video.music.downloader.AdsUtils.Interfaces.AppInterfaces;
import com.video.music.downloader.AdsUtils.PreferencesManager.AppPreferencesManger;
import com.video.music.downloader.AdsUtils.Utils.Constants;
import com.video.music.downloader.AdsUtils.Utils.Global;
import com.video.music.downloader.AdsUtils.Utils.StringUtils;
import com.video.music.downloader.R;
import com.video.music.downloader.vpn.Country_Code;
import com.video.music.downloader.vpn.LoginListener;
import com.video.music.downloader.vpn.SharePreferences;
import com.video.music.downloader.vpn.Utilss;
import com.video.music.downloader.vpn.VPNInitiator_Handler;
import com.video.music.downloader.vpn.VpnConnectListener;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.anchorfree.vpnsdk.callbacks.CompletableCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    Boolean isVpnConnected;
    ArrayList<Country_Code> list_country;
    List<Country> list;
    private String selected = "";
    private ClientInfo clientInfo;
    SharePreferences sp;
    private Integer VPN_STATUS = 0;
    public boolean vpn_connection = false;
    static FirebaseRemoteConfig mFirebaseRemoteConfig;
    private String vpnKey, backendURL;
    private Boolean vpnEnabled = false;
    AppPreferencesManger manger;
    TextView turen_btn;
    Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_splash);
        sp = new SharePreferences(this);
        activity = SplashActivity.this;
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(1).build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

        new VPNInitiator_Handler(activity, new AppInterfaces.VPNInterface() {
            @Override
            public void handlePostAd() {
                boolean isFirstRun = activity.getSharedPreferences(activity.getResources().getString(R.string.app_name), MODE_PRIVATE).getBoolean("isFirstRun", true);
                if (isFirstRun) {
                    Intent intent = new Intent(activity, PrivacyPolicy.class);
                    activity.startActivity(intent);
                } else {
                    Intent intent = new Intent(activity, DashboardActivity.class);
                    activity.startActivity(intent);
                }

                activity.getSharedPreferences(activity.getResources().getString(R.string.app_name), MODE_PRIVATE).edit().putBoolean("isFirstRun", false).commit();
            }
        });

        manger = new AppPreferencesManger(SplashActivity.this);

    }
}