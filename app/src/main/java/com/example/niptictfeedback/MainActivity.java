package com.example.niptictfeedback;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.niptictfeedback.apis.UserApi;
import com.example.niptictfeedback.models.User;
import com.example.niptictfeedback.sqlite.UserDBHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    UserDBHelper userDBHelper;
    User user;
    Intent intent;
    UserApi userApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userDBHelper = new UserDBHelper(this);
        userDBHelper.createNewTable();
        user = userDBHelper.getLoginUser();

        String baseUrl=((MyApplication) getApplicationContext()).getBaseUrl();
        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userApi = retrofit.create(UserApi.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user==null){
                   toLoginActivity();
                }
                else {
                    login();
                }
            }
        },1000);

    }

    public void toLoginActivity(){

        intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();

    }
    public void login(){
        String stuId=user.getStu_id();
        String password=user.getPassword();
        Call<User> call = userApi.loginUser(stuId,password,"2","oo3qQ0Bfh1mtcFEPZwUozf1uWGG0dtxYOkAhxom6","password");

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()){
                    Log.w("Login:: ",response.code()+""+response.message());
                    toLoginActivity();
                    return;
                }

                User userResponce = response.body();
                String tokenType = userResponce.getToken_type();
                String accessToken = userResponce.getAccess_token();

                ((MyApplication) getApplicationContext()).setAuthorization(tokenType+" "+accessToken);
                intent = new Intent(MainActivity.this,AppActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.w("Register fail::",t.getMessage());
                toLoginActivity();
            }
        });
    }


}
