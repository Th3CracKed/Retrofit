package com.retrofitsample.api.service;

import com.retrofitsample.api.model.RepositoriesModel;

import java.util.Map;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface API {
    String BASE_URL = "https://api.github.com/";
    @GET("/search/repositories")
    Single<RepositoriesModel> getRepositories(@QueryMap Map<String,String> params);
}
