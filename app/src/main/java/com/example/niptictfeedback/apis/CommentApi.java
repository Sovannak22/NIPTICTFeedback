package com.example.niptictfeedback.apis;

import com.example.niptictfeedback.models.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface CommentApi {

    @Headers({"Accept: application/json"})
    @GET("api/comments/feedback/{id}")
    Call<List<Comment>> getCommentById(@Header("Authorization") String header, @Path("id") int feedbackId);
}
