package com.retrofitsample.api.service;

import com.retrofitsample.api.model.RepositoryModel;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface API {
    String BASE_URL = "https://api.github.com/";
    @GET("/search/repositories")
    Observable<RepositoryModel> getRepositories(@QueryMap Map<String,String> params);
}
