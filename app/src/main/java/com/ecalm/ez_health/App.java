package com.ecalm.ez_health;

import android.app.Application;

import com.ecalm.ez_health.data.preferences.PreferencesProvider;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PreferencesProvider.init(this);
    }
}
