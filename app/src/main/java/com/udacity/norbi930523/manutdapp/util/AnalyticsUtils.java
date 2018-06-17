package com.udacity.norbi930523.manutdapp.util;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.udacity.norbi930523.manutdapp.application.ManUtdApp;

public class AnalyticsUtils {

    private AnalyticsUtils(){}

    public static void logScreenView(String screenName){
        Tracker tracker = ManUtdApp.getDefaultTracker();
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        tracker.setScreenName(null);
    }

    public static void logEvent(String category, String action){
        ManUtdApp.getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .build());
    }

}
