package com.abs.app;

import android.app.Application;
import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class ABSApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
