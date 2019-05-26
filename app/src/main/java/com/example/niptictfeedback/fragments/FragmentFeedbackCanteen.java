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
import android.widget.LinearLayout;

import com.emredavarci.noty.Noty;
import com.example.niptictfeedback.MyApplication;
import com.example.niptictfeedback.R;
import com.example.niptictfeedback.adapter.page_adapter.model_adapter.FeedbackAdapter;
import com.example.niptictfeedback.apis.FeedbackApi;
import com.example.niptictfeedback.models.FeedBack;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentFeedbackCanteen extends Fragment {
    private FeedbackApi feedbackApi;
    private LinearLayout alertPopUp;
    private RecyclerView recyclerView;
    private FeedbackAdapter feedbackAdapter;
    private List<FeedBack> feedBacks;
    private LinearLayout noPostFound;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news,container,false);

        alertPopUp = v.findViewById(R.id.alert_popup_user_news);
        feedBacks = new ArrayList<>();
        recyclerView = v.findViewById(R.id.rcl_news_user);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        noPostFound = v.findViewById(R.id.no_new_found);
        noPostFound.setVisibility(View.GONE);
        String baseUrl=((MyApplication)getActivity().getApplication()).getBaseUrl();
        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        feedbackApi = retrofit.create(FeedbackApi.class);
        getFeedbacks();
        return v;


    }

    public void getFeedbacks(){
        String auth=((MyApplication) getActivity().getApplication()).getAuthorization();
        Call<List<FeedBack>> call = feedbackApi.getFeedbackWithId(auth,2);
        call.enqueue(new Callback<List<FeedBack>>() {
            @Override
            public void onResponse(Call<List<FeedBack>> call, Response<List<FeedBack>> response) {
                if (!response.isSuccessful()){
                    Noty.init(getContext(), "Oops something went wrong!", alertPopUp,
                            Noty.WarningStyle.SIMPLE)
                            .setAnimation(Noty.RevealAnim.SLIDE_UP, Noty.DismissAnim.BACK_TO_BOTTOM, 400,400)
                            .setWarningInset(0,0,0,0)
                            .setWarningBoxRadius(0,0,0,0)
                            .show();
                    Log.e("Getnews::","!success");
                    return;
                }
                feedBacks= response.body();
                if (feedBacks.size()>0){
                    feedbackAdapter = new FeedbackAdapter(feedBacks,getContext(),recyclerView,"AppActivity");
                    recyclerView.setAdapter(feedbackAdapter);
                    return;
                }
                noPostFound.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<List<FeedBack>> call, Throwable t) {
                Log.e("Get news::","Fail");
                Noty.init(getContext(), "No internet connection!", alertPopUp,
                        Noty.WarningStyle.SIMPLE)
                        .setAnimation(Noty.RevealAnim.SLIDE_UP, Noty.DismissAnim.BACK_TO_BOTTOM, 400,400)
                        .setWarningInset(0,0,0,0)
                        .setWarningBoxRadius(0,0,0,0)
                        .show();
            }
        });
    }
}