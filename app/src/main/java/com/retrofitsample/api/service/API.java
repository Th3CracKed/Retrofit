package com.retrofitsample.api.service;

import com.retrofitsample.api.model.RepositoryModel;

import java.util.Map;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface API {
    String BASE_URL = "https://api.github.com/";
    @GET("/search/repositories")
    Single<RepositoryModel> getRepositories(@QueryMap Map<String,String> params);
}
