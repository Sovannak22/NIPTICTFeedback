package com.example.niptictfeedback.apis;

import com.example.niptictfeedback.models.News;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface NewsApi {

    @Headers({"Accept: application/json"})
    @GET("api/news/place/{id}")
    Call<List<News>> getNewsWithId(@Header("Authorization") String header, @Path("id") int placeId);

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

    @FormUrlEncoded
    @Headers({"Accept: application/json"})
    @POST("api/news/{id}")
    Call<News> deleteNews(@Header("Authorization") String header, @Path("id") String newsId, @Field("_method") String method);

    @Multipart
    @Headers({"Accept: application/json"})
    @POST("api/news/{id}")
    Call<News> updateNews(
            @Header("Authorization") String header,
            @Path("id") String newId,
            @Part MultipartBody.Part image,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part("_method") RequestBody method
    );
}
