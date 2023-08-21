# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep class com.video.music.downloader.** { *; }


#Dependenciy Start
# keep all classes and methods in the Hydra SDK

-keep class com.anchorfree.hydrasdk.** { *; }
# keep all classes and methods in the media library
-keep class androidx.media.** { *; }

# keep all classes and methods in the Apache Commons libraries
-keep class org.apache.commons.** { *; }

# keep all classes and methods in the m3u8lib module
-keep class com.example.m3u8lib.** { *; }

# keep all classes and methods in the RxJava library
-keep class io.reactivex.** { *; }

# keep all classes and methods in the Google Play Services Ads Lite library
-keep class com.google.android.gms.ads.** { *; }

# keep all classes and methods in the Clans FAB library
-keep class com.github.clans.** { *; }

# keep all classes and methods in the appcompat, material, constraintlayout, and room libraries
-keep class androidx.appcompat.** { *; }
-keep class com.google.android.material.** { *; }
-keep class androidx.constraintlayout.** { *; }
-keep class androidx.room.** { *; }

# keep all classes and methods in the navigation library
-keep class androidx.navigation.** { *; }

# keep all classes and methods in the Firebase messaging and config libraries
-keep class com.google.firebase.messaging.** { *; }
-keep class com.google.firebase.remoteconfig.** { *; }

# keep all classes and methods in the YouTube Android Player API
-keep class com.google.android.youtube.player.** { *; }

# keep all classes and methods in the Retrofit library
-keep class retrofit2.** { *; }

# keep all classes and methods in the GSON converter and RxJava2 adapter libraries for Retrofit
-keep class com.google.gson.** { *; }
-keep class io.reactivex.** { *; }

# keep all classes and methods used by JUnit and Espresso for testing
-dontwarn org.hamcrest.**
-dontwarn junit.**
-keep class org.hamcrest.** { *; }
-keep class org.junit.** { *; }
-keep class androidx.test.espresso.** { *; }

# CircleImageView
-keep class de.hdodenhof.circleimageview.** { *; }

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# SweetAlert
-keep class cn.pedant.SweetAlert.** { *; }

# FrescoImageViewer
-keep class com.stfalcon.frescoimageviewer.** { *; }

# Fresco
-keep class com.facebook.imagepipeline.** { *; }
-keep class com.facebook.drawee.** { *; }

# Jsoup
-keep class org.jsoup.** { *; }

# AndroidNetworking
-keep class com.androidnetworking.** { *; }

# Lottie
-keep class com.airbnb.lottie.** { *; }

# Dexter
-keep class com.karumi.dexter.** { *; }

# Material Intro
-keep class com.heinrichreimersoftware.materialintro.** { *; }

# ButterKnife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**

# prevent classes from being removed due to optimization
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose