package com.example.niptictfeedback.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.niptictfeedback.MyApplication;
import com.example.niptictfeedback.NewsAdminActivity;
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

public class NewsFragment extends Fragment {
    RecyclerView recyclerView;
    NewsAdapter newsAdapter;
    List<News> news;
    private Button btnDorm,btnCanteen;
    public NewsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news_base_user,container,false);
        btnDorm = v.findViewById(R.id.btn_dorm_news_user);
        btnCanteen = v.findViewById(R.id.btn_canteen_news_user);

        btnDorm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDorm.setBackground(getResources().getDrawable(R.drawable.blue_white_background));
                btnCanteen.setBackground(getResources().getDrawable(R.drawable.white_round_background));
                btnDorm.setTextColor(getResources().getColor(R.color.colorAccent));
                btnCanteen.setTextColor(getResources().getColor(R.color.colorPrimary));

            }
        });
        btnCanteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDorm.setBackground(getResources().getDrawable(R.drawable.white_round_background));
                btnCanteen.setBackground(getResources().getDrawable(R.drawable.blue_white_background));
                btnDorm.setTextColor(getResources().getColor(R.color.colorPrimary));
                btnCanteen.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        });
        return v;
    }
}
