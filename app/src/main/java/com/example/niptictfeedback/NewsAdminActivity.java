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
import android.widget.TextView;
import android.widget.Toast;

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
    RecyclerView recyclerView;
    NewsAdapter newsAdapter;
    List<News> news;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_admin);

        toolbar = findViewById(R.id.toolbar_admin_news);
        setSupportActionBar(toolbar);
        news = new ArrayList<>();
        recyclerView = findViewById(R.id.rcl_news);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewsApi newsApi = retrofit.create(NewsApi.class);
        String auth=((MyApplication) getApplicationContext()).getAuthorization();
        Call<List<News>> call = newsApi.getNews(auth);
        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                if (!response.isSuccessful()){
                    Log.w("Call ::",response.body()+"");
                    return;
                }
                news= response.body();
                Log.w("imageUrl:: ",news.get(0).getImage_url());
                newsAdapter = new NewsAdapter(news,NewsAdminActivity.this,recyclerView);
                recyclerView.setAdapter(newsAdapter);
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                Log.w("Error",t.getMessage());
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
            return true;
        }
            return super.onOptionsItemSelected(item);
    }
}
