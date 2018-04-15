package com.udacity.norbi930523.manutdapp.logging;

import android.util.Log;

import timber.log.Timber;

public class ReleaseTree extends Timber.DebugTree {

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        if(priority > Log.DEBUG){
            super.log(priority, tag, message, t);
        }
    }

}
