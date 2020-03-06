package com.charles.dramalist;

import android.app.Application;

import com.charles.dramalist.api.AppRepository;


public class DramaApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        AppRepository.getInstance(this);
    }
}
