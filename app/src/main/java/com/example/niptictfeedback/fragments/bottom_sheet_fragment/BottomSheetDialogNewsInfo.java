package com.example.niptictfeedback.fragments.bottom_sheet_fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.niptictfeedback.MyApplication;
import com.example.niptictfeedback.R;
import com.example.niptictfeedback.UpdateNewsActivity;
import com.example.niptictfeedback.apis.NewsApi;
import com.example.niptictfeedback.models.News;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BottomSheetDialogNewsInfo extends BottomSheetDialogFragment {


    private LinearLayout btnEditNews,btnDeleteNews,btnCancel;
    private Bundle bundle;
    private NewsApi newsApi;
    public static BottomSheetDialogNewsInfo newInstance() {
        return new BottomSheetDialogNewsInfo();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout_news_info,container,false);
        btnEditNews = v.findViewById(R.id.btn_edit_news);
        btnDeleteNews = v.findViewById(R.id.btn_delete_news);
        btnCancel = v.findViewById(R.id.btn_cancel_news_bottom_sheet);

        bundle = getArguments();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("news_more_option_dialog");
                getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        });

        btnEditNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UpdateNewsActivity.class);
                intent.putExtra("ImageUrl",bundle.getString("ImageUrl"));
                intent.putExtra("Title",bundle.getString("Title"));
                intent.putExtra("Description",bundle.getString("Description"));
                intent.putExtra("NewsID",bundle.getString("NewsID"));
                startActivity(intent);
            }
        });
        btnDeleteNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("DELETE THIS NEWS");
                builder.setMessage("You will no longer show this news anymore.");
                builder.setPositiveButton("DELETE",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteNews(bundle.getString("NewsID"));
                            }
                        });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.textColorAlert));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                });
                dialog.show();
            }
        });


        String baseUrl=((MyApplication)getActivity().getApplication()).getBaseUrl();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        newsApi = retrofit.create(NewsApi.class);

        return v;
    }


    private void deleteNews(String id){
        String auth=((MyApplication)getActivity().getApplication()).getAuthorization();

        Call<News> call = newsApi.deleteNews(auth,id,"DELETE");

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (!response.isSuccessful()){
                    Log.w("Upload Profile:: ",response.code()+""+response.message());
                    Toast.makeText(getContext(),"News delete unsuccessfully",Toast.LENGTH_SHORT).show();
                    return;
                }

//                News newsResponce = response.body();
                Toast.makeText(getContext(),"News Delete successfully",Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.w("Upload Profile fail::","Fail "+t.getMessage());
            }
        });

    }

}
