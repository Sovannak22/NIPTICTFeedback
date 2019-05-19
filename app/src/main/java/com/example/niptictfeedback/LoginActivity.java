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
    private TextView txtAdmin;
    private Dialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dialog = new Dialog(this);
        txtStuId = findViewById(R.id.txt_id);
        txtPassword = findViewById(R.id.txt_password);
        tvSignup = findViewById(R.id.tv_signup);
        btnLogin = findViewById(R.id.btn_login);
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
                login();


            }
        });

        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userApi = retrofit.create(UserApi.class);

//        Go to admin page test
        txtAdmin = findViewById(R.id.tv_to_admin);
        txtAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LoginActivity.this,NewsAdminActivity.class);
                login();
                finish();
            }
        });
    }

    //Login method
    public void login(){
        String stuId = txtStuId.getText()+"";
        String password = txtPassword.getText()+"";
        Call<User> call = userApi.loginUser(stuId,password,"2","l6P1n39KciDaL45ihBbFX2YsdHzqUxQPtzpx70CE","password");

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()){
                    showPopupIncorrectIdOrPassword();
                    Log.w("Login:: ",response.code()+""+response.message());

                    return;
                }

                User userResponce = response.body();
                String tokenType = userResponce.getToken_type();
                String accessToken = userResponce.getAccess_token();

                ((MyApplication) getApplicationContext()).setAuthorization(tokenType+" "+accessToken);
                getUserInfo();
                finish();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showPopupLoginFail();
                Log.w("Register fail::",t.getMessage());
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
                startActivity(intent);
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
