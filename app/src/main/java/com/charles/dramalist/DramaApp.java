package com.charles.dramalist;

import android.app.Application;

import com.charles.dramalist.api.AppRepository;
import com.charles.dramalist.data.BoxManager;

public class DramaApp extends Application {

    @Override
    public void onCreate() {
        init();
        super.onCreate();
    }

    private void init() {
        BoxManager.init(this);
        AppRepository.getInstance();
    }
}
