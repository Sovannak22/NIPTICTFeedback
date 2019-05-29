package com.example.niptictfeedback.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.emredavarci.noty.Noty;
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

public class FragmentNewsCanteen extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private NewsApi newsApi;
    private LinearLayout alertPopUp,noNewFound;
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private List<News> news;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news,container,false);

        noNewFound = v.findViewById(R.id.no_new_found);
        noNewFound.setVisibility(View.GONE);

        swipeRefreshLayout = v.findViewById(R.id.swipe_refresh_news);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        alertPopUp = v.findViewById(R.id.alert_popup_user_news);
        news = new ArrayList<>();
        recyclerView = v.findViewById(R.id.rcl_news_user);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        String baseUrl=((MyApplication)getActivity().getApplication()).getBaseUrl();
        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        newsApi = retrofit.create(NewsApi.class);
        onLoadingRefresh();
        return v;


    }

    public void getNews(){
        swipeRefreshLayout.setRefreshing(true);
        String auth=((MyApplication) getActivity().getApplication()).getAuthorization();
        Call<List<News>> call = newsApi.getNewsWithId(auth,2);
        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                if (!response.isSuccessful()){
                    Noty.init(getContext(), "Oops something went wrong!", alertPopUp,
                            Noty.WarningStyle.SIMPLE)
                            .setAnimation(Noty.RevealAnim.SLIDE_UP, Noty.DismissAnim.BACK_TO_BOTTOM, 400,400)
                            .setWarningInset(0,0,0,0)
                            .setWarningBoxRadius(0,0,0,0)
                            .show();
                    Log.e("Getnews::","!success");
                    swipeRefreshLayout.setRefreshing(false);
                    return;
                }
                news= response.body();
                if (news.size()>0){
                    newsAdapter = new NewsAdapter(news,getContext(),recyclerView);
                    recyclerView.setAdapter(newsAdapter);
                    swipeRefreshLayout.setRefreshing(false);
                    return;
                }
                noNewFound.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                Log.e("Get news::","Fail");
                Noty.init(getContext(), "No internet connection!", alertPopUp,
                        Noty.WarningStyle.SIMPLE)
                        .setAnimation(Noty.RevealAnim.SLIDE_UP, Noty.DismissAnim.BACK_TO_BOTTOM, 400,400)
                        .setWarningInset(0,0,0,0)
                        .setWarningBoxRadius(0,0,0,0)
                        .show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        getNews();
    }

    private void onLoadingRefresh(){
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                getNews();
            }
        });
    }
}
