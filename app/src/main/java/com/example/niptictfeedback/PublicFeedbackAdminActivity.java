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
import com.example.niptictfeedback.R;
import com.example.niptictfeedback.adapter.page_adapter.model_adapter.FeedbackAdapter;
import com.example.niptictfeedback.adapter.page_adapter.model_adapter.PrivateFeedbackAdapter;
import com.example.niptictfeedback.apis.FeedbackApi;
import com.example.niptictfeedback.models.FeedBack;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PublicFeedbackAdminActivity extends AppCompatActivity {

    private FeedbackApi feedbackApi;
    private LinearLayout alertPopUp,noNewFound;
    private RecyclerView recyclerView;
    private FeedbackAdapter feedbackAdapter;
    private List<FeedBack> feedBacks;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_feedback_admin);

        toolbar = findViewById(R.id.toolbar_admin_public_feedback);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        noNewFound = findViewById(R.id.no_new_found_public_feedback);
        noNewFound.setVisibility(View.GONE);

        alertPopUp = findViewById(R.id.alert_popup_public_feedback_admin);
        feedBacks = new ArrayList<>();
        recyclerView = findViewById(R.id.rcl_public_feedback_admin);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        String baseUrl=((MyApplication) getApplicationContext()).getBaseUrl();
        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        feedbackApi = retrofit.create(FeedbackApi.class);
        getNews();
    }

    public void getNews(){
        String auth=((MyApplication) getApplicationContext()).getAuthorization();
        Call<List<FeedBack>> call = feedbackApi.getPublicFeedbackAdmin(auth);
        call.enqueue(new Callback<List<FeedBack>>() {
            @Override
            public void onResponse(Call<List<FeedBack>> call, Response<List<FeedBack>> response) {
                if (!response.isSuccessful()){
                    Noty.init(getApplicationContext(), "Oops something went wrong!", alertPopUp,
                            Noty.WarningStyle.SIMPLE)
                            .setAnimation(Noty.RevealAnim.SLIDE_UP, Noty.DismissAnim.BACK_TO_BOTTOM, 400,400)
                            .setWarningInset(0,0,0,0)
                            .setWarningBoxRadius(0,0,0,0)
                            .show();
                    Log.e("Getnews::","!success "+response.code());
                    return;
                }
                feedBacks= response.body();
                if (feedBacks.size()>0){
                    feedbackAdapter = new FeedbackAdapter(feedBacks,getApplicationContext(),recyclerView);
                    recyclerView.setAdapter(feedbackAdapter);
                    return;
                }
                noNewFound.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<List<FeedBack>> call, Throwable t) {
                Log.e("Get news::","Fail");
                Noty.init(getApplicationContext(), "No internet connection!", alertPopUp,
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
