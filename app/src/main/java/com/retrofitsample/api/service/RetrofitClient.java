package com.retrofitsample.api.service;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.retrofitsample.api.service.API.BASE_URL;

public class RetrofitClient {
    private static Retrofit ourInstance;
	//TODO Replace Singleton with simple Factory
    public static synchronized Retrofit getInstance() {
        if(ourInstance == null)
            ourInstance = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return ourInstance;
    }
    //to prevent instantiation of this class
    private RetrofitClient() {
    }
}
