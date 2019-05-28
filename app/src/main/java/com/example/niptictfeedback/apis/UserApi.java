package com.example.niptictfeedback.apis;

import com.example.niptictfeedback.models.User;

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
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserApi {

    @GET("users")
    Call<List<User>> getUser();

    @Headers({"Accept: application/json"})
    @GET("api/get_current_user_info")
    Call<User> getUserInfo(@Header("Authorization") String header);

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

    @Multipart
    @Headers({"Accept: application/json"})
    @POST("api/change_profile_picture")
    Call<User> updateProfilePicture(@Header("Authorization") String header,
                                    @Part MultipartBody.Part image,
                                    @Part ("_method")RequestBody method);
}
