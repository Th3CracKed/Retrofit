package com.retrofitsample.api.service;

import com.retrofitsample.api.model.RepositoryModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface RepositoryClient {
    String BASE_URL = "https://api.github.com/";
    @GET("/search/repositories")
    Call<RepositoryModel> getRepositories(@QueryMap Map<String,String> params);
}
