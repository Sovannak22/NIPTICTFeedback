package com.example.niptictfeedback;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.emredavarci.noty.Noty;
import com.example.niptictfeedback.adapter.page_adapter.model_adapter.CommentAdapter;
import com.example.niptictfeedback.apis.CommentApi;
import com.example.niptictfeedback.apis.NewsApi;
import com.example.niptictfeedback.fragments.FragmentComment;
import com.example.niptictfeedback.models.Comment;
import com.example.niptictfeedback.models.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentActivity extends AppCompatActivity {

    Toolbar toolbar;
    private ImageView btnSubmit;
    private EditText txtComment;
    private CommentApi commentApi;
    private String previousActivtiy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        previousActivtiy = getIntent().getStringExtra("PreviousActivity");

        btnSubmit = findViewById(R.id.img_btn_submit_comment);
        txtComment = findViewById(R.id.txt_comment);

        toolbar = findViewById(R.id.tool_bar_comment);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
         final String feedbackId=getIntent().getStringExtra("FeedbackID");
        Log.w("FeedbackId::",""+feedbackId);
        FragmentComment fragmentComment = new FragmentComment();
        Bundle args = new Bundle();
        args.putString("FeedbackId",feedbackId);
        fragmentComment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.comment_fragment_container, fragmentComment,"FragmentComment").commit();

        String baseUrl=((MyApplication) getApplicationContext()).getBaseUrl();
        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        commentApi = retrofit.create(CommentApi.class);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createComment(feedbackId);
            }
        });
    }


    public void createComment(String feedback_id){
        String description = txtComment.getText()+"";
        String auth=((MyApplication) getApplicationContext()).getAuthorization();
        Call<Comment> call = commentApi.createComment(auth,description,feedback_id);

        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (!response.isSuccessful()){
                    Log.w("Login:: ",response.code()+""+response.message());
                    return;
                }

                Comment comment = response.body();
//                finish();
                Fragment frg = null;
                frg = getSupportFragmentManager().findFragmentByTag("FragmentComment");
                final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
                txtComment.setText("");
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Log.w("Register fail::",t.getMessage());
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (previousActivtiy=="AppActivity"){
            int fragmentSize = getSupportFragmentManager().getFragments().size();
            Fragment fragment =getSupportFragmentManager().getFragments().get(fragmentSize-1);
            final FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
            ft.detach(fragment);
            ft.attach(fragment);
            ft.commit();
        }
        else {
            Intent intent = new Intent(CommentActivity.this,PrivateFeedbackAdminActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
