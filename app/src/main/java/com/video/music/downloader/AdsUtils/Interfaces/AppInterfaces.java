package com.video.music.downloader.AdsUtils.Interfaces;

import com.video.music.downloader.AdsUtils.FirebaseADHandlers.AdsJsonPOJO;

public class AppInterfaces {
    public interface AdDataInterface {
        void getAdData(AdsJsonPOJO adsJsonPOJO);
    }

    public interface InterStitialADInterface {
        void adLoadState(boolean isLoaded);
    }
    public interface AppOpenADInterface {
        void appOpenAdState(boolean state_load);
    }

    public interface VPNInterface{
        void handlePostAd();
    }
}
