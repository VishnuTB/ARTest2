package io.area51.artest2;

import android.app.Application;

import timber.log.Timber;

public class ArTestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

    }
}