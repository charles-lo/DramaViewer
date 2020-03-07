package com.charles.dramalist.data;

import android.content.Context;

import com.charles.dramalist.BuildConfig;
import com.charles.dramalist.api.model.MyObjectBox;

import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;


public class BoxManager {

    private static BoxManager boxManager;
    private BoxStore boxStore;

    private BoxManager(BoxStore boxStore) {
        this.boxStore = boxStore;
    }

    public static void init(Context context) {

        BoxStore boxStore = MyObjectBox.builder()
                .androidContext(context.getApplicationContext())
                .build();
        boxManager = new BoxManager(boxStore);

        if (BuildConfig.DEBUG) {
            new AndroidObjectBrowser(boxStore).start(context);
        }
    }

    public static BoxStore getStore() {
        return boxManager.boxStore;
    }
}
