package com.example.niptictfeedback;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.emredavarci.noty.Noty;
import com.example.niptictfeedback.adapter.page_adapter.model_adapter.NewsAdapter;
import com.example.niptictfeedback.apis.NewsApi;
import com.example.niptictfeedback.models.News;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NewsAdminActivity extends AppCompatActivity {

    Toolbar toolbar;
    private NewsApi newsApi;
    private LinearLayout alertPopUp;
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private List<News> news;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_admin);
        toolbar = findViewById(R.id.toolbar_admin_news);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        alertPopUp = findViewById(R.id.alert_popup_admin_news);
        news = new ArrayList<>();
        recyclerView = findViewById(R.id.rcl_news_admin);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String baseUrl=((MyApplication) getApplicationContext()).getBaseUrl();
        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        newsApi = retrofit.create(NewsApi.class);
        getNews();
    }

    public void getNews(){
        String auth=((MyApplication) getApplicationContext()).getAuthorization();
        Call<List<News>> call = newsApi.getNews(auth);
        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                if (!response.isSuccessful()){
                    Noty.init(NewsAdminActivity.this, "Oops something went wrong!", alertPopUp,
                            Noty.WarningStyle.SIMPLE)
                            .setAnimation(Noty.RevealAnim.SLIDE_UP, Noty.DismissAnim.BACK_TO_BOTTOM, 400,400)
                            .setWarningInset(0,0,0,0)
                            .setWarningBoxRadius(0,0,0,0)
                            .show();
                    Log.e("Getnews::","!success");
                }
                news= response.body();
                newsAdapter = new NewsAdapter(news,NewsAdminActivity.this,recyclerView);
                recyclerView.setAdapter(newsAdapter);
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                Log.e("Get news::","Fail");
                Noty.init(NewsAdminActivity.this, "No internet connection!", alertPopUp,
                        Noty.WarningStyle.SIMPLE)
                        .setAnimation(Noty.RevealAnim.SLIDE_UP, Noty.DismissAnim.BACK_TO_BOTTOM, 400,400)
                        .setWarningInset(0,0,0,0)
                        .setWarningBoxRadius(0,0,0,0)
                        .show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.news_admin_action_bar,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_news){
            Intent intent = new Intent(NewsAdminActivity.this,AddNewsActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
