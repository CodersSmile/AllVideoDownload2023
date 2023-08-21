package com.video.music.downloader.AdsUtils.PreferencesManager;

import android.content.Context;

import com.video.music.downloader.AdsUtils.FirebaseADHandlers.AdsJsonPOJO;
import com.video.music.downloader.AdsUtils.Utils.Constants;
import com.google.gson.Gson;

public class AppPreferencesManger {
    private AppPreferences appPreference;

    public AppPreferencesManger(Context context) {
        appPreference = AppPreferences.getAppPreferences(context);
    }

    public void setAdsModel(AdsJsonPOJO adsJsonPOJO) {
        appPreference.putString(Constants.ADSJSON, new Gson().toJson(adsJsonPOJO));
    }
    public String getAdsModel() {
        return appPreference.getString(Constants.ADSJSON,"");
    }

}
