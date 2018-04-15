package com.udacity.norbi930523.manutdapp.application;

import android.app.Application;

import com.udacity.norbi930523.manutdapp.BuildConfig;
import com.udacity.norbi930523.manutdapp.logging.ReleaseTree;

import timber.log.Timber;

public class ManUtdApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new ReleaseTree());
        }
    }

}
