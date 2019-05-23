package com.example.niptictfeedback;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.emredavarci.noty.Noty;
import com.example.niptictfeedback.adapter.page_adapter.model_adapter.CommentAdapter;
import com.example.niptictfeedback.apis.CommentApi;
import com.example.niptictfeedback.apis.NewsApi;
import com.example.niptictfeedback.models.Comment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentActivity extends AppCompatActivity {

    Toolbar toolbar;
    private LinearLayout alertPopUp;
    private RecyclerView recyclerView;
    private List<Comment> comments;
    private CommentApi commentApi;
    private CommentAdapter commentAdapter;
    private LinearLayout noComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        noComment = findViewById(R.id.no_comment_show);
        noComment.setVisibility(View.GONE);
        toolbar = findViewById(R.id.tool_bar_comment);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        alertPopUp = findViewById(R.id.alert_popup_comment);
        comments = new ArrayList<>();
        recyclerView = findViewById(R.id.rcl_comment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String baseUrl=((MyApplication) getApplicationContext()).getBaseUrl();
        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        commentApi = retrofit.create(CommentApi.class);
        getComment();

    }

    public void getComment(){
        String auth=((MyApplication) getApplicationContext()).getAuthorization();
        int feedbackId=Integer.parseInt(getIntent().getStringExtra("FeedbackID"));
        Call<List<Comment>> call = commentApi.getCommentById(auth,feedbackId);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (!response.isSuccessful()){
                    Noty.init(CommentActivity.this, "Oops something went wrong!", alertPopUp,
                            Noty.WarningStyle.SIMPLE)
                            .setAnimation(Noty.RevealAnim.SLIDE_UP, Noty.DismissAnim.BACK_TO_BOTTOM, 400,400)
                            .setWarningInset(0,0,0,0)
                            .setWarningBoxRadius(0,0,0,0)
                            .show();
                    Log.e("Getnews::","!success"+response.message());
                    return;
                }
                comments = response.body();

                if (comments.size()>1){
                    Log.w("comment size::",""+comments.size());
                    commentAdapter = new CommentAdapter(comments,CommentActivity.this,recyclerView);
                    recyclerView.setAdapter(commentAdapter);
                    return;
                }

                noComment.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Log.e("Get news::","Fail");
                Noty.init(CommentActivity.this, "No internet connection!", alertPopUp,
                        Noty.WarningStyle.SIMPLE)
                        .setAnimation(Noty.RevealAnim.SLIDE_UP, Noty.DismissAnim.BACK_TO_BOTTOM, 400,400)
                        .setWarningInset(0,0,0,0)
                        .setWarningBoxRadius(0,0,0,0)
                        .show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
