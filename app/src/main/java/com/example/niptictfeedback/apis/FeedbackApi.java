package com.example.niptictfeedback.apis;

import com.example.niptictfeedback.models.FeedBack;

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
import retrofit2.http.Path;

public interface FeedbackApi {

    @Headers({"Accept: application/json"})
    @GET("api/feedbacks")
    Call<List<FeedBack>> getFeedback(@Header("Authorization") String header);

    @Headers({"Accept: application/json"})
    @GET("api/feedbacks/public/place/{id}")
    Call<List<FeedBack>> getFeedbackWithId(@Header("Authorization") String header, @Path("id") int placeId);

    @Headers({"Accept: application/json"})
    @GET("api/feedbacks/private/place")
    Call<List<FeedBack>> getPrivateFeedbackWithId(@Header("Authorization") String header);

    @Multipart
    @Headers({"Accept: application/json"})
    @POST("api/feedbacks")
    Call<FeedBack> createFeedback(
            @Header("Authorization") String header,
            @Part MultipartBody.Part image,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part("place_id") RequestBody placeId,
            @Part("feedback_type_id") RequestBody feedbackTypeId
    );
}
