package com.udacity.norbi930523.manutdapp.application;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.udacity.norbi930523.manutdapp.BuildConfig;
import com.udacity.norbi930523.manutdapp.R;
import com.udacity.norbi930523.manutdapp.logging.ReleaseTree;

import timber.log.Timber;

public class ManUtdApp extends Application {

    private static GoogleAnalytics analytics;
    private static Tracker gaTracker;

    @Override
    public void onCreate() {
        super.onCreate();

        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new ReleaseTree());
        }

        analytics = GoogleAnalytics.getInstance(this);
    }

    synchronized public static Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (gaTracker == null) {
            gaTracker = analytics.newTracker(R.xml.global_tracker);
        }

        return gaTracker;
    }

}
