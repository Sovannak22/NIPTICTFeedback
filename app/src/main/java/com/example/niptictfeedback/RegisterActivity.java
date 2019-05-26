package com.example.niptictfeedback;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.niptictfeedback.apis.UserApi;
import com.example.niptictfeedback.models.User;
import com.victor.loading.rotate.RotateLoading;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private TextView tvLogin;
    private EditText txtFullname;
    private EditText txtStudentId;
    private EditText txtPassword;
    private EditText txtCPassword;
    private String fullName,stuId,password,cPassword,gender;
    private RadioGroup radioGroupGender;
    private RadioButton radioButtonGender;
    private Button btnRegister;
    //Declare for access user api
    private UserApi userApi;

    private RotateLoading rotateLoading;
    private LinearLayout lodingBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tvLogin = findViewById(R.id.tv_login);

        txtFullname = findViewById(R.id.txt_fullname);
        txtStudentId = findViewById(R.id.txt_student_id);
        txtPassword = findViewById(R.id.txt_password);
        txtCPassword = findViewById(R.id.txt_c_password);

        rotateLoading = findViewById(R.id.rotate_loading_register);
        lodingBackground = findViewById(R.id.loding_bg_register);
        lodingBackground.setVisibility(View.GONE);

        btnRegister = findViewById(R.id.btn_register);

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        String baseUrl=((MyApplication) getApplicationContext()).getBaseUrl();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userApi = retrofit.create(UserApi.class);

//        Get text from edit text

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullName = txtFullname.getText().toString();
                stuId = txtStudentId.getText().toString();
                password = txtPassword.getText().toString();
                cPassword = txtCPassword.getText().toString();
                //        Test cPassword and Password
                if (password.equals(cPassword)){
                    addListenerButton();
                    lodingBackground.setVisibility(View.VISIBLE);
                    rotateLoading.start();
                    createPost();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Password and Comfirm password must be the same",Toast.LENGTH_LONG).show();
                    Log.e("compare:: ","0");
                }
                Log.e("compare::","Password:"+password+",Cpassword: "+cPassword);
            }
        });




    }

    private void createPost(){

        Call<User> call= userApi.createUser(fullName,stuId,gender,password,cPassword);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()){
                    Log.w("Register:: ",response.code()+""+response.message());
                    lodingBackground.setVisibility(View.GONE);
                    rotateLoading.stop();
                    return;
                }

                User userResponce = response.body();
                String content = "";
                content+="Code: "+response.code()+"\n";
                content+="Message: "+userResponce.getMessage()+"\n";
                content+="Token: "+userResponce.getToken()+"\n";

                Log.w("Register:: ","Successfully "+content+response);

                String tokenType = "Bearer";
                String accessToken = userResponce.getToken();
                ((MyApplication) getApplicationContext()).setAuthorization(tokenType+" "+accessToken);
                Intent intent = new Intent(RegisterActivity.this,AppActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.w("Register fail::",t.getMessage());
                lodingBackground.setVisibility(View.GONE);
                rotateLoading.stop();
            }
        });
    }

//    Add radio box lister
    public void addListenerButton(){
        radioGroupGender = findViewById(R.id.radio_group_gender);
        int selectedId = radioGroupGender.getCheckedRadioButtonId();
        radioButtonGender = findViewById(selectedId);
        gender = radioButtonGender.getText().toString();
    }



}
