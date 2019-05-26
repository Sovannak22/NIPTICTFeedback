package com.example.niptictfeedback;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.niptictfeedback.apis.UserApi;
import com.example.niptictfeedback.models.User;
import com.victor.loading.rotate.RotateLoading;

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
    private TextView txtAdmin;
    private Dialog dialog;
    private RotateLoading rotateLoading;
    private LinearLayout lodingBackground;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dialog = new Dialog(this);
        txtStuId = findViewById(R.id.txt_id);
        txtPassword = findViewById(R.id.txt_password);
        tvSignup = findViewById(R.id.tv_signup);
        btnLogin = findViewById(R.id.btn_login);
        rotateLoading = findViewById(R.id.rotate_loading_login);
        lodingBackground = findViewById(R.id.loding_bg_login);
        lodingBackground.setVisibility(View.GONE);
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((txtStuId.getText().toString()).isEmpty() || (txtPassword.getText().toString()).isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please input all field below",Toast.LENGTH_LONG).show();
                    return;
                }
                lodingBackground.setVisibility(View.VISIBLE);
                rotateLoading.start();
                login();


            }
        });

        String baseUrl=((MyApplication) getApplicationContext()).getBaseUrl();
        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userApi = retrofit.create(UserApi.class);

    }

    //Login method
    public void login(){
        String stuId = txtStuId.getText()+"";
        String password = txtPassword.getText()+"";
        Call<User> call = userApi.loginUser(stuId,password,"2","oo3qQ0Bfh1mtcFEPZwUozf1uWGG0dtxYOkAhxom6","password");

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()){
                    showPopupIncorrectIdOrPassword();
                    Log.w("Login:: ",response.code()+""+response.message());
                    lodingBackground.setVisibility(View.GONE);
                    rotateLoading.stop();
                    return;
                }

                User userResponce = response.body();
                String tokenType = userResponce.getToken_type();
                String accessToken = userResponce.getAccess_token();

                ((MyApplication) getApplicationContext()).setAuthorization(tokenType+" "+accessToken);
                getUserInfo();
//                finish();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showPopupLoginFail();
                Log.w("Register fail::",t.getMessage());
                lodingBackground.setVisibility(View.GONE);
                rotateLoading.stop();
            }
        });
    }

    public void getUserInfo(){
        String auth=((MyApplication) getApplicationContext()).getAuthorization();
        Call<User> call = userApi.getUserInfo(auth);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()){
                    Log.w("Call ::",response.body()+"");
                    return;
                }
                User userResponce = response.body();
                Log.w("user",""+userResponce.getUser_role_id());
                loginDirect(userResponce);
                rotateLoading.stop();
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    public void loginDirect(User user){
        if (Integer.parseInt(user.getUser_role_id())==1){
            intent = new Intent(LoginActivity.this,HomeAdmin.class);
        }else{
            intent = new Intent(LoginActivity.this,AppActivity.class);
        }
    }

    public void showPopupIncorrectIdOrPassword(){
        dialog.setContentView(R.layout.incorrect_id_or_password_popup);
        TextView tvRegiser = dialog.findViewById(R.id.tv_register);
        TextView tvCancel = dialog.findViewById(R.id.tv_cancel);

        tvRegiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    public void showPopupLoginFail(){
        dialog.setContentView(R.layout.fail_popup);
        TextView tvOk = dialog.findViewById(R.id.tv_ok);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }
}
