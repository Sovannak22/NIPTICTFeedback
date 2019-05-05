package com.example.niptictfeedback.apis;

import com.example.niptictfeedback.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserApi {

    @GET("users")
    Call<List<User>> getUser();

    @FormUrlEncoded
    @POST("api/register")
    Call<User> createUser(
            @Field("name") String name,
            @Field("student_id") String stu_id,
            @Field("gender") String gender,
            @Field("password") String password,
            @Field("c_password") String cPassword
    );

    @FormUrlEncoded
    @POST("oauth/token")
    Call<User> loginUser(
            @Field("username") String studentId,
            @Field("password") String password,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("grant_type") String grantType
    );
}
