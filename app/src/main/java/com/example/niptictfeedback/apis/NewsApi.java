package com.example.niptictfeedback.apis;

import com.example.niptictfeedback.models.News;
import com.example.niptictfeedback.models.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface NewsApi {

    @Headers({"Accept: application/json"})
    @GET("api/news")
    Call<List<News>> getNews(@Header("Authorization") String header);


    @Multipart
    @Headers({"Accept: application/json"})
    @POST("api/news")
    Call<News> createNews(
            @Header("Authorization") String header,
            @Part MultipartBody.Part image,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description
    );
}
