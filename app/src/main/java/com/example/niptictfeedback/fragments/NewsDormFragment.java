package com.example.niptictfeedback.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.niptictfeedback.MyApplication;
import com.example.niptictfeedback.R;
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

public class NewsDormFragment extends Fragment {

    RecyclerView recyclerView;
    NewsAdapter newsAdapter;
    List<News> news;
    public NewsDormFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news,container,false);

        View v = inflater.inflate(R.layout.fragment_news,container,false);
        news = new ArrayList<>();
        recyclerView = v.findViewById(R.id.rcl_news);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        String baseUrl=((MyApplication)getActivity().getApplication()).getBaseUrl();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewsApi newsApi = retrofit.create(NewsApi.class);
        String auth=((MyApplication)getActivity().getApplication()).getAuthorization();
        Call<List<News>> call = newsApi.getNewsWithId(auth,1);
        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                if (!response.isSuccessful()){
                    Log.w("Call ::",response.body()+"");
                    return;
                }
                news= response.body();
                Log.w("imageUrl:: ",news.get(0).getImage_url());
                newsAdapter = new NewsAdapter(news,getContext(),recyclerView);
                recyclerView.setAdapter(newsAdapter);
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                Log.w("Error",t.getMessage());
            }
        });

        return view;
    }
}
