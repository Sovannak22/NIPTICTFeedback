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
import com.example.niptictfeedback.CommentActivity;
import com.example.niptictfeedback.MyApplication;
import com.example.niptictfeedback.R;
import com.example.niptictfeedback.adapter.page_adapter.model_adapter.CommentAdapter;
import com.example.niptictfeedback.apis.CommentApi;
import com.example.niptictfeedback.models.Comment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentComment extends Fragment {

    private LinearLayout alertPopUp;
    private RecyclerView recyclerView;
    private List<Comment> comments;
    private CommentApi commentApi;
    private CommentAdapter commentAdapter;
    private LinearLayout noComment;

    public FragmentComment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.comments_fragment,container,false);

        noComment = v.findViewById(R.id.no_comment_found);
        noComment.setVisibility(View.GONE);
        alertPopUp = v.findViewById(R.id.alert_popup_comment);
        comments = new ArrayList<>();
        recyclerView = v.findViewById(R.id.rcl_comment_fragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        String baseUrl=((MyApplication)getActivity().getApplication()).getBaseUrl();
        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        commentApi = retrofit.create(CommentApi.class);
        Log.w("feedbackId::", getArguments().getString("FeedbackId")+"");
        getComment(Integer.parseInt(getArguments().getString("FeedbackId")));
        return v;
    }

    public void getComment(int id){
        String auth=((MyApplication)getActivity().getApplication()).getAuthorization();
        Call<List<Comment>> call = commentApi.getCommentById(auth,id);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (!response.isSuccessful()){
                    Noty.init(getContext(), "Oops something went wrong!", alertPopUp,
                            Noty.WarningStyle.SIMPLE)
                            .setAnimation(Noty.RevealAnim.SLIDE_UP, Noty.DismissAnim.BACK_TO_BOTTOM, 400,400)
                            .setWarningInset(0,0,0,0)
                            .setWarningBoxRadius(0,0,0,0)
                            .show();
                    Log.e("Getnews::","!success"+response.message());
                    return;
                }
                comments = response.body();

                if (comments.size()>0){
                    Log.w("comment size::",""+comments.size());
                    commentAdapter = new CommentAdapter(comments,getContext(),recyclerView);
                    recyclerView.setAdapter(commentAdapter);
                    return;
                }

                noComment.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
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
