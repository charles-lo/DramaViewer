package com.charles.dramalist.api;

import com.charles.dramalist.api.model.DramaModel;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface APIService {

    @GET("drama")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Single<DramaModel> fetchDrama();
}