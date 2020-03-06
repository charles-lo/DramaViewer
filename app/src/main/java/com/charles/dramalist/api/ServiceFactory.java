package com.charles.dramalist.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

class ServiceFactory {

    static APIService getInstance() {

        String baseUrl = "https://5e6235126f5c7900149bcb19.mockapi.io/api/v1/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(APIService.class);
    }
}