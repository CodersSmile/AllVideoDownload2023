package com.video.music.downloader.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.video.music.downloader.AdsUtils.FirebaseADHandlers.AdUtils;
import com.video.music.downloader.AdsUtils.Interfaces.AppInterfaces;
import com.video.music.downloader.AdsUtils.Utils.Constants;
import com.video.music.downloader.R;
import com.video.music.downloader.databinding.ActivitySocialMediaSelectBinding;

public class SocialMediaSelectActivity extends AppCompatActivity {

    ActivitySocialMediaSelectBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_social_media_select);
        AdUtils.showNativeAd(SocialMediaSelectActivity.this, Constants.adsJsonPOJO.getParameters().getNative_id().getDefaultValue().getValue(), findViewById(R.id.native_ads), false);


        binding.mTxtcontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdUtils.showInterstitialAd(SocialMediaSelectActivity.this, new AppInterfaces.InterStitialADInterface() {
                    @Override
                    public void adLoadState(boolean isLoaded) {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                    }
                });
            }
        });
    }
}