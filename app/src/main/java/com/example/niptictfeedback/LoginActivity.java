package com.example.niptictfeedback;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.niptictfeedback.apis.UserApi;
import com.example.niptictfeedback.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private TextView tvSignup;
    private Button btnLogin;
    private EditText txtStuId,txtPassword;
    private Intent intent;
    private UserApi userApi;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtStuId = findViewById(R.id.txt_student_id);
        txtPassword = findViewById(R.id.txt_password);
        tvSignup = findViewById(R.id.tv_signup);
        btnLogin = findViewById(R.id.btn_login);
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
//                Log.e("Access",""+authAccess);
                intent = new Intent(LoginActivity.this,AppActivity.class);

            }
        });

        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userApi = retrofit.create(UserApi.class);
    }

    //Login method
    public void login(){
        Call<User> call = userApi.loginUser("b20160005","123456","2","Odh9FYiScOCnmwY3jYUdP56r94pqurlkOB8hVCxn","password");

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()){
                    Log.w("Login:: ",response.code()+""+response.message());
                    return;
                }

                User userResponce = response.body();
//                String content = "";
//                content+="Code: "+response.code()+"\n";
//                content+="Token type: "+userResponce.getToken_type()+"\n";
//                content+="Access Token: "+userResponce.getAccess_token()+"\n";
                String tokenType = userResponce.getToken_type();
                String accessToken = userResponce.getAccess_token();
                Log.w("Token type:: ",tokenType);
                Log.w("Access token:: ",accessToken);

                ((MyApplication) getApplicationContext()).setAuthorization(tokenType+" "+accessToken);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.w("Register fail::",t.getMessage());
            }
        });
    }
}
