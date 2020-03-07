package com.charles.dramalist.api;

import com.charles.autocachingconveter.GsonCacheableConverter;
import com.charles.autocachingconveter.GsonResponseListener;
import com.charles.dramalist.api.model.Datum;
import com.charles.dramalist.api.model.DramaModel;
import com.charles.dramalist.data.BoxManager;

import java.util.ArrayList;
import java.util.Collection;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

class ServiceFactory {

    static APIService getInstance() {

        String baseUrl = "https://5e6235126f5c7900149bcb19.mockapi.io/api/v1/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonCacheableConverter.create((type, responseBody) -> {
                    if (responseBody instanceof DramaModel) {
                        BoxManager.getStore().boxFor(Datum.class).put(((DramaModel) responseBody).getData());
                    }

//                    if (responseBody instanceof Collection) {
//                        BoxManager.getStore().boxFor(type).put((ArrayList) responseBody);
//                    } else {
//                        BoxManager.getStore().boxFor(type).put(responseBody);
//                    }
                }))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(APIService.class);
    }
}