package com.example.niptictfeedback.apis;

import com.example.niptictfeedback.models.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommentApi {

    @Headers({"Accept: application/json"})
    @GET("api/comments/feedback/{id}")
    Call<List<Comment>> getCommentById(@Header("Authorization") String header, @Path("id") int feedbackId);


    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("api/comments")
    Call<Comment> createComment(@Header("Authorization") String header
            , @Field("description") String description
            ,@Field("feedback_id") String feedback_id);

    @FormUrlEncoded
    @Headers({"Accept: application/json"})
    @POST("api/comments/{id}")
    Call<Comment> deleteComment(@Header("Authorization") String header, @Path("id") String commentId, @Field("_method") String method);
}
